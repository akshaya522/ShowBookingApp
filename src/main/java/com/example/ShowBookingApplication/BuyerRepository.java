package com.example.ShowBookingApplication;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {   

    List<Buyer> findByShowId(Long showId);

    List<Buyer> findByBuyerPhoneNumber(Integer buyerPhoneNumber);

    Optional<Buyer> findByTicketIdAndBuyerPhoneNumber(Long ticketId, Integer buyerPhoneNumber);
}