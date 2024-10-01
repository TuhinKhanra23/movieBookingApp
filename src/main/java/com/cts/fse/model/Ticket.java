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
@Document("ticket")
public class Ticket {

    @Id
    private String ticketId;
    private String userId;
    private String movieName;
    private String theaterName;
    private Date bookingDate;
    private List<String> seatNumber;


}
