package com.example.ShowBookingApplication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private Integer numOfRows;
    private Integer numOfSeatsPerRow;
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

    public List<String> getAllSeats(Integer rows, Integer seatsPerRow) {
        List<String> res = new ArrayList<>();
        if(rows > 0 && rows < 27){
            for(int i = 1; i < rows+1; i++){
                String rowVal = String.valueOf((char)(i+64));
                for(int j = 0; j<seatsPerRow; j++){
                    String seat = rowVal + String.valueOf(j+1);
                    res.add(seat);
                }
            }
        }

        return res;
    }
}