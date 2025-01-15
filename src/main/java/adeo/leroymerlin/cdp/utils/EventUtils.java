package adeo.leroymerlin.cdp.utils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import adeo.leroymerlin.cdp.entity.Event;

public class EventUtils {

	public static BiFunction<List<Event>, String, List<Event>> filter = (eventList, query) -> eventList.stream()
			.filter(event -> event.getBands().stream()
					.flatMap(brand -> brand.getMembers().stream())
					.filter(member -> member.getName().contains(query))
					.findAny().isPresent())
			.collect(Collectors.toList());

	public static Function<List<Event>, List<Event>> addCounter = eventList -> eventList.stream().map(event -> {
		event.setTitle(String.format(event.getTitle() + " [%s]", event.getBands().size()));
		event.getBands().stream().forEach(band -> {
			band.setName(String.format(band.getName() + " [%s]", band.getMembers().size()));
		});
		return event;
	}).collect(Collectors.toList());

}
