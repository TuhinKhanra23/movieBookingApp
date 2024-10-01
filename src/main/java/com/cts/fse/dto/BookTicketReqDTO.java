package com.cts.fse.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookTicketReqDTO {

    private String movieName;
    private String theaterName;

    private List<String> seatNumber;

}
