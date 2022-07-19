package com.api.events.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Task 3: Design Entities To Represent Your Data
 * You’ll need to decide how to persist your information. To complete this project, you will need to store the following:
 *
 * Two different kinds of users - Employees and Customers.
 */

@Entity
@Table(name = "events")
public class Event extends AuditModel{


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String source;

    private String type;

    private String status;

    @CreationTimestamp
    private LocalDate date;

    //epoch
    //private Long createdDate;
    private Long epochTimeStamp;


    private String refNum;

    private String errorCode;

    private String errorDescription;

    private String eventCode;

    private String runId;

    public Event(UUID id, String source, String type, String status, LocalDate date, Long epochTimeStamp, String refNum, String errorCode, String errorDescription, String eventCode, String runId) {
        super();
        this.id = id;
        this.source = source;
        this.type = type;
        this.status = status;
        this.date = date;
        this.epochTimeStamp = epochTimeStamp;
        this.refNum = refNum;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.eventCode = eventCode;
        this.runId = runId;
    }

    public Event(){
        super();
    }

    @PrePersist
    protected void onCreate() {
        //createdDate = System.currentTimeMillis();
        epochTimeStamp = System.currentTimeMillis();
    }


    @PreUpdate
    protected void onUpdate() {
        epochTimeStamp = System.currentTimeMillis();
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getEpochTimeStamp() {
        return epochTimeStamp;
    }

    public void setEpochTimeStamp(Long epochTimeStamp) {
        this.epochTimeStamp = epochTimeStamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", epochTimeStamp=" + epochTimeStamp +
                ", refNum='" + refNum + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", eventCode='" + eventCode + '\'' +
                ", runId='" + runId + '\'' +
                '}';
    }
}
