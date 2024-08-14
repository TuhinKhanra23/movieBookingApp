package com.cts.fse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document("movie")
public class Movie {

    @Id
    private String movieId;
    private String movieName;
    private Date releaseDate;
    private Map<String, Integer> theaterMap;
    private boolean movieAvailableForBooking;

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

    public Map<String, Integer> getTheaterMap() {
        return theaterMap;
    }

    public void setTheaterMap(Map<String, Integer> theaterMap) {
        this.theaterMap = theaterMap;
    }

    public boolean isMovieAvailableForBooking() {
        return movieAvailableForBooking;
    }

    public void setMovieAvailableForBooking(boolean movieAvailableForBooking) {
        this.movieAvailableForBooking = movieAvailableForBooking;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId='" + movieId + '\'' +
                ", movieName='" + movieName + '\'' +
                ", releaseDate=" + releaseDate +
                ", theaterMap=" + theaterMap +
                ", movieAvailableForBooking=" + movieAvailableForBooking +
                '}';
    }
}
