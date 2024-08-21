package com.cts.fse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("movie")
public class Movie {

    @Id
    private String movieId;
    private String movieName;
    private Date releaseDate;
    private List<Integer> theaterIdList;


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

    public List<Integer> getTheaterIdList() {
        return theaterIdList;
    }

    public void setTheaterIdList(List<Integer> theaterIdList) {
        this.theaterIdList = theaterIdList;
    }


    @Override
    public String toString() {
        return "Movie{" + "movieId='" + movieId + '\'' + ", movieName='" + movieName + '\'' + ", releaseDate=" + releaseDate + ", theaterIdList=" + theaterIdList +

                '}';
    }
}
