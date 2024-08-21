package com.cts.fse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("theater")
public class Theater {
    @Id
    private Integer theaterId;

    private String theaterName;

    private String theaterLoc;

    private Integer theaterCapacity;
    private Set<Integer> bookedTickets;
    private Integer availableSeats;

    public Integer getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Integer theaterId) {
        this.theaterId = theaterId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public String getTheaterLoc() {
        return theaterLoc;
    }

    public void setTheaterLoc(String theaterLoc) {
        this.theaterLoc = theaterLoc;
    }

    public Integer getTheaterCapacity() {
        return theaterCapacity;
    }

    public void setTheaterCapacity(Integer theaterCapacity) {
        this.theaterCapacity = theaterCapacity;
    }

    public Set<Integer> getBookedTickets() {
        return bookedTickets;
    }

    public void setBookedTickets(Set<Integer> bookedTickets) {
        this.bookedTickets = bookedTickets;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Theater{" +
                "theaterId=" + theaterId +
                ", theaterName='" + theaterName + '\'' +
                ", theaterLoc='" + theaterLoc + '\'' +
                ", theaterCapacity=" + theaterCapacity +
                ", bookedTickets=" + bookedTickets +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
