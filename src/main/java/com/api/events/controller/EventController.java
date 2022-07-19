package com.api.events.controller;


import com.api.events.entities.Event;
import com.api.events.services.EventsService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class EventController {



    private final Logger logmsg = LoggerFactory.getLogger(EventController.class);
    @Autowired
    private EventsService eventsService;



    @GetMapping(value = {"/home"})
    public String home(){
        return this.eventsService.home();
    }



    @PostMapping(value={"/events"},consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> addEvent(@RequestBody Event event){

        if(event.getId()!=null){
            logmsg.error("[Fail] unable to insert user passed id for event body : " + event.getId());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Event e= this.eventsService.addevent(event);
        logmsg.info("[Success] able to insert a event at: " + e.getEpochTimeStamp());
        return new ResponseEntity<>(e, HttpStatus.CREATED);
    }





    //http://localhost:8081/api/events?page=0&size=6&sortBy=status
    @GetMapping(value={"/events",""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(name = "page",defaultValue = "1") int page, @RequestParam(name = "size",defaultValue = "4") int size,
                                    @RequestParam(required = false) String status, @RequestParam(required = false) String refNum,
                                    @RequestParam(name = "startDate",required = false) String startdate,
                                    @RequestParam(name = "endDate",required = false)String enddate,
                                    @RequestParam Optional<String> sortBy){
        try {
            Pageable pageable = PageRequest.of(page-1, size,Sort.Direction.DESC,sortBy.orElse("epochTimeStamp"));
            Page<Event>  eventListByKeyword = this.eventsService.findByStatus_RefNum_dateRange(pageable,status,refNum,startdate,enddate);
            List<Event> listEvent = eventListByKeyword.getContent();
            logmsg.info("[Success] able to get a events ,size : " + listEvent.size());
            return new ResponseEntity<>(listEvent, HttpStatus.OK);

        } catch (Exception e) {
            logmsg.error("[Fail] unable to get events: " + e.getLocalizedMessage() +", error log : event not found" );
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping(value="/events/{eventId}")
    public ResponseEntity<Event> getEvents(@PathVariable(name = "eventId") UUID eventId){


        try {
            Event getEvent = this.eventsService.getevent(eventId);
            logmsg.info("[Success] able to get a event : " + getEvent.getId());
            return new ResponseEntity<>(getEvent, HttpStatus.OK);
        }catch (Exception e){
            logmsg.error("[Fail] unable to get event: " + e.getLocalizedMessage() +", error log : event not found" );
            return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);

        }

    }


    /**
     * @Timed(value = "greeting.time", description = "Time taken to return greeting")
     * @Timed(
     *             value = "events.api.post",
     *             histogram = true,
     *             percentiles = {0.95, 0.99},
     *             extraTags = {"version", "1.0"}
     *     )
     *     @Timed(
     *             value = "events.api.getAll",
     *             histogram = true,
     *             percentiles = {0.95, 0.99},
     *             extraTags = {"version", "1.0"}
     *     )@Timed(
     *             value = "events.api.getById",
     *             histogram = true,
     *             percentiles = {0.95, 0.99},
     *             extraTags = {"version", "1.0"}
     *     )
     */

}
