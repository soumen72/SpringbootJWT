package com.api.events.repositories;

import com.api.events.entities.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventCustomRepository {
    List<Event> findByDateRange(LocalDate stdate, LocalDate eddate);
}
