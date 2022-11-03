package com.example.ShowBookingApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class BookTicketService {
    private final BuyerRepository buyerRepository;
    private final ShowRepository showRepository;

    public BookTicketService(
        BuyerRepository buyerRepository,
        ShowRepository showRepository
    ){
        this.buyerRepository = buyerRepository;
        this.showRepository = showRepository;
    }

    public List<String> getShowAvailableSeats(Long showId) {
        Show show = showRepository.findById(showId).orElseThrow(() -> new ShowDoesNotExist(showId));
        List<Buyer> buyerList = buyerRepository.findByShowId(showId);
        List<String> bookedSeats = buyerList.stream().map(Buyer::getSeatNumber).collect(Collectors.toList());

        List<String> availSeats = new ArrayList<>();

        if(show.getNumOfRows() > 0 && show.getNumOfRows() < 27){
            for(int i = 1; i < show.getNumOfRows()+1; i++){
                String rowVal = String.valueOf((char)(i+64));
                for(int j = 0; j<show.getNumOfSeatsPerRow(); j++){
                    String seat = rowVal + String.valueOf(j+1);
                    if(!bookedSeats.contains(seat)){
                        availSeats.add(seat);
                    }
                }
            }
        }

        return availSeats;
    }

    public void cancelBooking(Long ticketNo, Integer phoneNumber) {
        Buyer ticket = buyerRepository.findByTicketIdAndBuyerPhoneNumber(ticketNo, phoneNumber);
        Integer cancellationWindow = showRepository.findById(ticket.getShowId()).get().getCancellationWindow();
        LocalDateTime currTime = LocalDateTime.now();
        LocalDateTime cancellationTime = ticket.getBookingTime().plusMinutes(cancellationWindow);

        if (currTime.isBefore(cancellationTime)){
            buyerRepository.delete(ticket);
        } else {
            new CannotCancel(ticketNo);
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