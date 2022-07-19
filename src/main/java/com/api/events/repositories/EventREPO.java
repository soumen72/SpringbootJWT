package com.api.events.repositories;

import com.api.events.entities.Event;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface EventREPO extends PagingAndSortingRepository<Event, UUID>, EventCustomRepository,JpaSpecificationExecutor<Event> {


    @Override
    @Query(value = "SELECT * From EVENTS WHERE id = :id", nativeQuery = true)
    Optional<Event> findById(@Param("id") UUID id);

}

