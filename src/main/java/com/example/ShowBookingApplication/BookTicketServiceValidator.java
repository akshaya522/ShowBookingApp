package com.example.ShowBookingApplication;

import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service 
public class BookTicketServiceValidator {

    private final ShowRepository showRepository;
    private final BuyerRepository buyerRepository;

    public BookTicketServiceValidator(
        ShowRepository showRepository,
        BuyerRepository buyerRepository
    ){
        this.showRepository = showRepository;
        this.buyerRepository = buyerRepository;
    }


    /**
     * This function validates show details entered by user 
     * @param show - show entity 
     * @return - returns null if valid Show, returns error string if invalid show  
     */
    public String createShowValidator(Show show) {
        if (show.getNumOfRows() < 1) {
            return "Number of rows in show cannot be less than one!";
        } else if (show.getNumOfRows() > 26) {
            return "Number of rows in show cannot be more than 26!";
        } else if (show.getNumOfSeatsPerRow() < 1) {
            return "Number of seats per row cannot be less than one!";
        } else if (show.getNumOfSeatsPerRow() > 10) {
            return "Number of seats per row cannot be more than 10!";
        } else if (show.getCancellationWindow() < 0) {
            return "Cancelletion window cannot be less than zero minutes!";
        } else if (show.getCancellationWindow() > 120) {
            return "Cancellation window cannot be more than 120 minutes!";
        } else {
            return null;
        }
    }

    /**
     * This function validates buyer's book ticket request 
     * @param buyerDTO - buyer details
     * @param availSeats - available seats in show 
     * @param bookingSeats - seats requested by buyer  
     * @return - returns null if valid request, returns error string if invalid request  
     */
    public String bookTicketValidator(BuyerDTO buyerDTO, List<String> availSeats, List<String> bookingSeats) {
        List<Buyer> buyerList = this.buyerRepository.findByBuyerPhoneNumber(buyerDTO.getBuyerPhoneNumber());
        List<String> invalidSeats = bookingSeats.stream().filter(seat -> !availSeats.contains(seat.toUpperCase())).collect(Collectors.toList());

        if (availSeats.size() == 0) {
            return "There are no more available seats for this show";
        } else if (bookingSeats.size() == 0) {
            return "Seat number lists cannot be empty!";
        } else if (buyerList.size() == 1) {
            return "There is an existing booking with this number. Only one booking per phone number is allowed";
        } else if (invalidSeats.size() > 0) {
            return "There are invalid seat numbers: " + invalidSeats + " Tickets not booked!";
        } else if (!buyerDTO.getBuyerPhoneNumber().toString().matches("^[6|9|8][0-9]{7}")) {
            return "Please enter a valid Singapore phone number Eg: 81234567";
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 409
    class ShowDoesNotExist extends RuntimeException{
        /**
        	 *
        	 */
        private static final long serialVersionUID = 1L;

        public ShowDoesNotExist(long showId) {
        super("Show " + showId + " does not exist.");
    }}

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 409
    class CannotCancel extends RuntimeException{
        /**
        	 *
        	 */
        private static final long serialVersionUID = 1L;

        public CannotCancel(long ticketId) {
        super("Cannot cancel " + ticketId + " beyond cancellation time.");
    }}
}