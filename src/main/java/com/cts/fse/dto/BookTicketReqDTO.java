package com.cts.fse.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookTicketReqDTO {

    private String movieId;
    private Integer theaterId;
    private Date bookingDate;
    private List<Integer> seatNumber;

}
