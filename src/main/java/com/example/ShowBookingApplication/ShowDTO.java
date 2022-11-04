package com.example.ShowBookingApplication;

import java.util.List;

public class ShowDTO {
    private Long showId; 
    private List<Buyer> buyerDetails;
    private Integer cancellationWindow;
    private List<String> availSeats;

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public List<Buyer> getBuyerDetails() {
        return buyerDetails;
    }

    public void setBuyerDetails(List<Buyer> buyerDetails) {
        this.buyerDetails = buyerDetails;
    }

    public Integer getCancellationWindow() {
        return cancellationWindow;
    }

    public void setCancellationWindow(Integer cancellationWindow) {
        this.cancellationWindow = cancellationWindow;
    }

    public List<String> getAvailSeats() {
        return availSeats;
    }

    public void setAvailSeats(List<String> availSeats) {
        this.availSeats = availSeats;
    }
}