package com.example.ShowBookingApplication;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {   

    List<Buyer> findByShowId(Long showId);
}