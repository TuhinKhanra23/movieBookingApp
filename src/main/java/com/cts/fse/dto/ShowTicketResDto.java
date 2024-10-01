package com.cts.fse.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowTicketResDto {
    private String ticketId;
    private String name;
    private Date bookingDate;
    private List<String> seatNumber;
    private String movieName;
    private String theaterName;
    private String theaterLoc;


}
