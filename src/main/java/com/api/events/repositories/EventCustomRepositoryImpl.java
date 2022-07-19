package com.api.events.repositories;

import com.api.events.entities.Event;
import com.api.events.entities.Event_;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventCustomRepositoryImpl implements  EventCustomRepository{

    @Autowired
    private EntityManager entityManager;
    @Override
    public List<Event> findByDateRange(LocalDate stdate, LocalDate eddate) {

        //create event query
        CriteriaBuilder cb=entityManager.getCriteriaBuilder();
        //criteria query uses criteria builder to create query for/limit set to event class only
        CriteriaQuery<Event> cq=cb.createQuery(Event.class);
        //root is used to get the values passed in arguments
        Root<Event> root=cq.from(Event.class);
        List<Predicate> conditionsList = new ArrayList<>();
        //predicate is used to construct where and check
        Predicate startsAfterBegin=cb.greaterThanOrEqualTo(root.get(Event_.DATE),stdate);
        Predicate endsBeforeEnd=cb.lessThanOrEqualTo(root.get(Event_.DATE),eddate);
        conditionsList.add(startsAfterBegin);
        conditionsList.add(endsBeforeEnd);
        //performing certain query
        cq.select(root).where(conditionsList.toArray(new Predicate[]{}));

        TypedQuery<Event> query=entityManager.createQuery(cq);

        return query.getResultList();


    }
}
