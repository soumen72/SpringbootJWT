package com.api.events.services;

import com.api.events.entities.Event;
//import com.udacity.jdnd.course3.critter.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public interface EventsService{


    public Event getevent(UUID eventId);



    Event addevent(Event event);
    List<Event> findByDate(String startDate, String endDate);

    String home();




    Page<Event> findByStatus_RefNum_dateRange(Pageable paging, String status, String refNum, String stdate, String eddate);
}
