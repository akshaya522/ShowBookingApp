package com.example.ShowBookingApplication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Show {

    @Id
    @GeneratedValue
    private Long showId; 
    @NotNull
    private Integer numOfRows;
    @NotNull
    private Integer numOfSeatsPerRow;
    @NotNull
    private Integer cancellationWindow;

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(Integer numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Integer getNumOfSeatsPerRow() {
        return numOfSeatsPerRow;
    }

    public void setNumOfSeatsPerRow(Integer numOfSeatsPerRow) {
        this.numOfSeatsPerRow = numOfSeatsPerRow;
    }

    public Integer getCancellationWindow() {
        return cancellationWindow;
    }

    public void setCancellationWindow(Integer cancellationWindow) {
        this.cancellationWindow = cancellationWindow;
    }
}