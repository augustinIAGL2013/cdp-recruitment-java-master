package adeo.leroymerlin.cdp.service;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import adeo.leroymerlin.cdp.entity.Band;
import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.repo.BandRepository;
import adeo.leroymerlin.cdp.repo.EventRepository;
import adeo.leroymerlin.cdp.utils.EventUtils;

@Service
public class EventService {

	private final EventRepository eventRepository;
	private final BandRepository bandRepository;

	public EventService(EventRepository eventRepository, BandRepository bandRepository) {
		this.eventRepository = eventRepository;
		this.bandRepository = bandRepository;
	}

	public List<Event> getEvents() {
		return eventRepository.findAll();
	}

	public void delete(Long id) {
		eventRepository.deleteById(id);
	}

	public List<Event> getFilteredEvents(String query) {
		List<Event> events = eventRepository.findAll();
		// Filter the events list in pure JAVA here
		BiFunction<List<Event>, String, List<Event>> pipeline = EventUtils.filter.andThen(EventUtils.addCounter);
		return pipeline.apply(events, query);
	}

	public void update(Event event) {
		eventRepository.findById(event.getId()).ifPresentOrElse(modifiedEvent -> {
			modifiedEvent.setComment(event.getComment());
			modifiedEvent.setImgUrl(event.getImgUrl());
			modifiedEvent.setNbStars(event.getNbStars());
			modifiedEvent.setTitle(event.getTitle());

			if (event.getBands() == null || event.getBands().isEmpty()) {
				throw new IllegalArgumentException("No event update with empty bands allowed");
			}
			List<String> newBandNameList = event.getBands().stream()
					.map(band -> band.getName())
					.collect(Collectors.toList());

			List<String> oldBandNameList = modifiedEvent.getBands().stream()
					.map(band -> band.getName())
					.collect(Collectors.toList());
			
			if (oldBandNameList == null || !oldBandNameList.containsAll(newBandNameList)) {
				Set<Band> bandList = bandRepository.findByNameIn(newBandNameList);
				if (bandList.size() != newBandNameList.size()) {
					throw new IllegalArgumentException("Some of the bands were not found");
				}
				modifiedEvent.setBands(bandList);
			}
			eventRepository.save(modifiedEvent);
		}, () -> {
			throw new IllegalStateException("No record found");
		});

	}
}
