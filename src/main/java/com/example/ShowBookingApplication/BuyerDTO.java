package com.example.ShowBookingApplication;

public class BuyerDTO {
    private Long ticketId;
    private Long showId;
    private Integer buyerPhoneNumber;
    private Integer seatNumber;
    private String seatNumberList;

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

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatNumberList() {
        return seatNumberList;
    }

    public void setSeatNumberList(String seatNumberList) {
        this.seatNumberList = seatNumberList;
    }
}