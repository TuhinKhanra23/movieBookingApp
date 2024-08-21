package com.cts.fse.dto;

import java.util.Date;
import java.util.Set;

public class MovieSearchResDTO {
    private String movieId;
    private String movieName;
    private Date releaseDate;
    private String theaterName;
    private String theaterLoc;
    private Integer theaterCapacity;
    private Set<Integer> bookedTickets;


    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    @Override
    public String toString() {
        return "MovieSearchResDTO{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", releaseDate=" + releaseDate +
                ", theaterName='" + theaterName + '\'' +
                ", theaterLoc='" + theaterLoc + '\'' +
                ", theaterCapacity=" + theaterCapacity +
                ", bookedTickets=" + bookedTickets +
                '}';
    }
}
