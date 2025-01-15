package adeo.leroymerlin.cdp.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import adeo.leroymerlin.cdp.entity.Band;
import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.entity.Member;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceTest {

	@Autowired
	private EventService eventService;

	@Test
	public void shouldFilterEvents() {
		String query = "Wa";
		List<Event> events = eventService.getFilteredEvents(query);
		assertEquals(events.size(), 1);
		Event event = events.get(0);
		assertEquals(event.getTitle(), "GrasPop Metal Meeting [5]");	
		assertEquals(event.getBands().size(), 5);
		Optional<Band> metalicaBand = event.getBands().stream().filter(e -> e.getName().contains("Metallica")).findAny();
		assertEquals(metalicaBand.get().getMembers().size(), 4);
		assertEquals(metalicaBand.get().getName(), "Metallica [4]");
		assertTrue(metalicaBand.isPresent());
		Optional<Member> any = events.get(0).getBands().stream()
				.flatMap(brand -> brand.getMembers().stream())
				.filter(member -> member.getName().contains(query)).findAny();
		assertTrue(any.isPresent());

	}
	
	@Test
	public void shouldThrowErrorOnUpdateEvent() {
		Event modifiedEvent = new Event();
		modifiedEvent.setId(1000L);
		modifiedEvent.setTitle("newTitle");
		modifiedEvent.setComment("newComment");
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			eventService.update(modifiedEvent);
		});

		String expectedMessage = "No event update with empty bands allowed";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void shouldUpdateEvent() {
		List<Event> events = eventService.getEvents();
		long eventId = 1000L;
		Optional<Event> optEvent = findEventById(events, eventId);

		Event modifiedEvent = optEvent.get();
		String newTitle = "Toto";
		int newStarsNbr = 5;
		String newComment = "newComment";
		String newImageUrl = "//url/image";
		modifiedEvent.setTitle(newTitle);
		modifiedEvent.setNbStars(newStarsNbr);
		modifiedEvent.setComment(newComment);
		modifiedEvent.setImgUrl(newImageUrl);
		Set<Band> newBands = new HashSet<>();
		Band band = new Band();
		String newBandName = "Sum41";
		band.setName(newBandName);
		newBands.add(band);
		modifiedEvent.setBands(newBands);

		eventService.update(modifiedEvent);

		events = eventService.getEvents();
		Optional<Event> modifiedOptEvent = findEventById(events, eventId);

		assertEquals(modifiedOptEvent.get().getTitle(), newTitle);
		assertEquals(modifiedOptEvent.get().getNbStars(), newStarsNbr);
		assertEquals(modifiedOptEvent.get().getComment(), newComment);
		assertEquals(modifiedOptEvent.get().getImgUrl(), newImageUrl);
		Object[] modifiedBands = modifiedOptEvent.get().getBands().toArray();
		assertEquals(modifiedBands.length, 1);
		assertEquals(((Band) modifiedBands[0]).getName(), newBandName);

	}

	@Test
	public void shouldDeleteEvent() {
		List<Event> events = eventService.getEvents();
		long eventId = 1001L;
		Optional<Event> optEvent = findEventById(events, eventId);

		Event modifiedEvent = optEvent.get();
		eventService.delete(modifiedEvent.getId());

		events = eventService.getEvents();
		Optional<Event> modifiedOptEvent = findEventById(events, eventId);

		assertTrue(modifiedOptEvent.isEmpty());

	}

	private Optional<Event> findEventById(List<Event> events, long id) {
		return events.stream().filter(e -> id == e.getId().longValue()).findFirst();
	}
}