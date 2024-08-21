package com.cts.fse.dto;

import java.util.Date;
import java.util.List;

public class ShowTicketResDto {
    private String name;
    private Date bookingDate;
    private List<Integer> seatNumber;
    private String movieName;
    private String theaterName;
    private String theaterLoc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public List<Integer> getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(List<Integer> seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
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

    @Override
    public String toString() {
        return "ShowTicketResDto{" +
                "name='" + name + '\'' +
                ", bookingDate=" + bookingDate +
                ", seatNumber=" + seatNumber +
                ", movieName='" + movieName + '\'' +
                ", theaterName='" + theaterName + '\'' +
                ", theaterLoc='" + theaterLoc + '\'' +
                '}';
    }
}
