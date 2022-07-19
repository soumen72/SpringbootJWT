package com.api.events.services;

import com.api.events.entities.Event;
import com.api.events.repositories.EventREPO;
import com.api.events.specifications.EventsSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;


@Service
public class EventsServiceImpl implements EventsService {


    private EventREPO eventRepository;

    public EventsServiceImpl(EventREPO eventRepository) {
        this.eventRepository=eventRepository;
    }


    @Override
    public Event getevent(UUID eventId) throws RuntimeException {
        try{
            return eventRepository.findById(eventId).orElseThrow(null);
        }catch (Exception e){
            throw  new RuntimeException("EventService getevent Exception "+ e);
        }
    }


    @Override
    public Event addevent(Event event) {
        return eventRepository.save(event);

    }
    @Override
    public List<Event> findByDate(String startDate, String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.parse(startDate, formatter);
        LocalDate localDate2 = LocalDate.parse(endDate, formatter);
        List<Event> dateRange=eventRepository.findByDateRange(localDate1,localDate2);
        return dateRange;

    }
    @Override
    public String home() {
        return "welcome";
    }



    @Override
    public Page<Event> findByStatus_RefNum_dateRange(Pageable paging ,String status, String refNum,String stdate,String eddate) {


        if(status==null && refNum==null && stdate==null && eddate==null){
            Page<Event> pageResult=eventRepository.findAll(paging);
            System.out.println("print data ");
            return pageResult;
        }else if(stdate==null && eddate==null){
            Page<Event> filterEvents = eventRepository.findAll(where(EventsSpecification.hasStatus(status).and(EventsSpecification.hasrefNum(refNum))),paging);
            return filterEvents;
        }else{

            List<Event> dateRange=findByDate(stdate,eddate);
            List<Event> filterEvents = eventRepository.findAll(where(EventsSpecification.hasStatus(status).and(EventsSpecification.hasrefNum(refNum))));
            Set<Event> result = new HashSet<>();
            Set<Event> allData = new HashSet<>();
            for (Event event : dateRange) {
                if (allData.add(event)) {
                    if (filterEvents.contains(event)) {
                        result.add(event);
                    }
                }
            }

            Page<Event> pages = new PageImpl<>(result.stream().toList(),paging, result.size());

            return pages;

        }
    }



}
