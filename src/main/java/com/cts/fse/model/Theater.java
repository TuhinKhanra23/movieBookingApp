package com.cts.fse.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("theater")
public class Theater {
    @Id
    private Integer theaterId;

    private String theaterName;

    private String theaterLoc;

    private Integer theaterCapacity;
    private Set<String> bookedTickets;
    private Integer availableSeats;



}
