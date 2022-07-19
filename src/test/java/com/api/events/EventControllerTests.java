package com.api.events;


import com.api.events.controller.EventController;
import com.api.events.entities.Event;
import com.api.events.repositories.EventCustomRepository;
import com.api.events.repositories.EventREPO;
import com.api.events.services.EventsService;
import com.api.events.services.EventsServiceImpl;
import com.api.events.specifications.EventsSpecification;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.security.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.Specification.where;


@SpringBootTest(classes = EventsApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventControllerTests {

    @Autowired
    private EventController eventController;

    @Autowired
    private EventsSpecification eventsSpecification;

    @Test
    @DisplayName("dummy")
    public void test() {
        assertTrue(true);
    }


    @Test
    @DisplayName("get all using mockito /unit testing")
    @Order(1)
    public void getEventsUsingMock() {

        EventREPO eventREPO = mock(EventREPO.class);
        List<Event> eventss = new ArrayList<>();
        eventss.add(new Event(UUID.randomUUID(), "source14", "good", "500", null, null, "ref", "test", "test", "test", "test"));
        eventss.add(new Event(UUID.randomUUID(), "source15", "good", "500", null, null, "ref", "test", "test", "test", "test"));
        System.out.println("******************************************************");
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "epochTimeStamp");
        Page<Event> pages = new PageImpl<>(eventss, pageable, eventss.size());
        //
        Mockito.when(eventREPO.findAll(pageable)).thenReturn(pages);
        EventsServiceImpl eventsServiceImpl = new EventsServiceImpl(eventREPO);
        List<Event> eventList = eventsServiceImpl.findByStatus_RefNum_dateRange(pageable, null, null, null, null).stream().toList();
        Assertions.assertEquals(2, eventList.size());


    }




    @Test
    @DisplayName("find a specific event /unit testing")
    @Order(2)
    public void testGetEventbyMokito() {
        EventREPO eventREPO = mock(EventREPO.class);
        List<Event> eventss = new ArrayList<>();

        eventss.add(new Event(UUID.fromString("21330874-bdf8-4043-8c04-b7cc31c57cc5"), "source14", "good", "500", null, null, "ref", "test", "test", "test", "test"));
        EventsServiceImpl eventsServiceImpl = new EventsServiceImpl(eventREPO);
        Mockito.when(eventREPO.findById(UUID.fromString("21330874-bdf8-4043-8c04-b7cc31c57cc5"))).thenReturn(Optional.ofNullable(eventss.get(0)));
        Event getAllEvent = eventsServiceImpl.getevent(UUID.fromString("21330874-bdf8-4043-8c04-b7cc31c57cc5"));
        Assertions.assertEquals(getAllEvent.getSource(), "source14");

    }

    @Test
    @DisplayName("Find all event / List of Paginated and sorting  /integration testing")
    @Order(3)
    public void testGetAllEvents() {
        ResponseEntity<List<Event>> retrievedCustomer = eventController.getAllEvents(1, 3, null, null, null, null, Optional.ofNullable("source"));
        List<Event> eventList = retrievedCustomer.getBody().stream().toList();
        assertTrue(eventList.size() > 0);
        ResponseEntity<List<Event>> retrievedCustomer1 = eventController.getAllEvents(1, 3, "500", null, null, null, Optional.ofNullable("source"));
        List<Event> eventList1 = retrievedCustomer1.getBody().stream().toList();
        Assertions.assertEquals(3, eventList1.size());
    }

    @Test
    @DisplayName("find a specific event /integration testing")
    @Order(4)
    public void testGetEvent() {
        ResponseEntity<Event> getAllEvent = eventController.getEvents(UUID.fromString("21330874-bdf8-4043-8c04-b7cc31c57cc5"));
        Assertions.assertEquals(getAllEvent.getBody().getSource(), "source12");
        Assertions.assertEquals(getAllEvent.getBody().getEventCode(), "test");
    }

    /**
    Specification<Event> specification = Specification.where(EventsSpecification.hasStatus("500")).and(EventsSpecification.hasrefNum("ref"));
        System.out.println(specification+" ***************");
        Mockito.when(eventREPO.findAll(specification,pageable)).thenReturn(pages);
    List<Event> eventListbyref = eventsServiceImpl.findByStatus_RefNum_dateRange(pageable, "500", null, null, null).stream().toList();
        System.out.println(eventListbyref.size()+"*****");
     */
}