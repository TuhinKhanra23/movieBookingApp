package com.cts.fse.dto;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieSearchResDTO {
    private String movieId;
    private String movieName;
    private Date releaseDate;
    private Integer theaterId;
    private String theaterName;
    private String theaterLoc;
    private Integer theaterCapacity;
    private Set<String> bookedTickets;

}
