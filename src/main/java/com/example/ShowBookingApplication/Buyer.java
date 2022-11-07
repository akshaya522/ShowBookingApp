package com.example.ShowBookingApplication;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Buyer {

    @Id
    @GeneratedValue
    private Long ticketId; 
    @NotNull
    private Long showId;
    @NotNull
    private Integer buyerPhoneNumber;
    @NotNull
    private String seatNumber;
    @NotNull
    private LocalDateTime bookingTime;

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Integer getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(Integer buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
}