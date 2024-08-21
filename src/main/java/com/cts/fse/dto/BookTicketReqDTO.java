package com.cts.fse.dto;

import java.util.Date;
import java.util.List;

public class BookTicketReqDTO {
    private String loginId;
    private String movieId;
    private Integer theaterId;
    private Date bookingDate;
    private List<Integer> seatNumber;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Integer getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Integer theaterId) {
        this.theaterId = theaterId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public List<Integer> getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(List<Integer> seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "BookTicketReqDTO{" +
                "loginId='" + loginId + '\'' +
                ", movieId='" + movieId + '\'' +
                ", theaterId=" + theaterId +
                ", bookingDate=" + bookingDate +
                ", seatNumber=" + seatNumber +
                '}';
    }
}
