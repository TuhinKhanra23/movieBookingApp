package com.cts.fse.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("movie")
public class Movie {

    @Id
    private String movieId;
    private String movieName;

    private List<Integer> theaterIdList;
    private Date releaseDate;

    public Movie(String movieName, String releaseDate, String theaterIdList, String imagePath) {
    }
}
