package com.example.ShowBookingApplication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private Long showId;
    private Integer buyerPhoneNumber;
    private Integer seatNumber;
}