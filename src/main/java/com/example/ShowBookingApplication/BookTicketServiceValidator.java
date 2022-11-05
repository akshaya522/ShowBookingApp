package com.example.ShowBookingApplication;

import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

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


    public String createShowValidator(Show show) {
        if (show.getNumOfRows() < 0) {
            return "Number of rows in show cannot be less than zero";
        } else if (show.getNumOfRows() > 26) {
            return "Number of rows in show cannot be more than 26";
        } else if (show.getNumOfSeatsPerRow() < 0) {
            return "Number of seats per row cannot be less than zero";
        } else if (show.getNumOfSeatsPerRow() > 10) {
            return "Number of seats per row cannot be more than 10";
        } else if (show.getCancellationWindow() < 0) {
            return "Cancelletion window minutes cannot be zero";
        } else if (show.getCancellationWindow() > 120) {
            return "Cancellation window minutes cannot be more than 120";
        } else {
            return null;
        }
    }

    public String bookTicketValidator(BuyerDTO buyerDTO) {
        Long showId = buyerDTO.getShowId();
        Optional<Show> show = this.showRepository.findById(showId);
        List<Buyer> buyerList = this.buyerRepository.findByBuyerPhoneNumber(buyerDTO.getBuyerPhoneNumber());
        List<String> seatNumbers = Arrays.asList(buyerDTO.getSeatNumberList().split("\\s*,\\s*"));


        if (!show.isPresent()) {
            return "Invalid show Id";
        } else if (seatNumbers.size() == 0) {
            return "Seat number list cannot be empty";
        } else if (buyerList.size() == 1) {
            return "There is an existing booking with this number. Only one booking per phone number is allowed";
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