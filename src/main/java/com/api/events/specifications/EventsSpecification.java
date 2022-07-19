package com.api.events.specifications;


import com.api.events.entities.Event;

import com.api.events.entities.Event_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class EventsSpecification {

    public static Specification<Event> hasStatus(String status){

        return ((root, criteriaQuery, criteriaBuilder) -> {

            if(status==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(Event_.STATUS),status);

        });


    }

    public static Specification<Event> hasrefNum(String refNum){


        return ((root, criteriaQuery, criteriaBuilder) -> {

            if(refNum==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(Event_.REF_NUM),refNum);
        });
    }


}
