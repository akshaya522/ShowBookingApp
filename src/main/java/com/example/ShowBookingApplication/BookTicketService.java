package com.example.ShowBookingApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class BookTicketService {
    private final BuyerRepository buyerRepository;
    private final ShowRepository showRepository;
    private final BookTicketServiceValidator bookTicketServiceValidator;

    public BookTicketService(
        BuyerRepository buyerRepository,
        ShowRepository showRepository,
        BookTicketServiceValidator bookTicketServiceValidator
    ){
        this.buyerRepository = buyerRepository;
        this.showRepository = showRepository;
        this.bookTicketServiceValidator = bookTicketServiceValidator;
    }

    public String saveshow(Show show){
        String validator = this.bookTicketServiceValidator.createShowValidator(show);
        if (validator == null) {
            Show show1 = this.showRepository.save(show);
            return "Show successfully setup! ShowId: " + show1.getShowId().toString();
        }
        return validator;
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

    public List<String> getShowAvailableSeats(Long showId) {
        Show show = showRepository.findById(showId).get();
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

    public String cancelBooking(Long ticketNo, Integer phoneNumber) {
        Buyer ticket = buyerRepository.findByTicketIdAndBuyerPhoneNumber(ticketNo, phoneNumber);
        Integer cancellationWindow = showRepository.findById(ticket.getShowId()).get().getCancellationWindow();
        LocalDateTime currTime = LocalDateTime.now();
        LocalDateTime cancellationTime = ticket.getBookingTime().plusMinutes(cancellationWindow);

        if (currTime.isBefore(cancellationTime)){
            buyerRepository.delete(ticket);
            return "Cancelled";
        } else {
            return "Cannot cancel pass cancellation window!";
        }

    }

    public ShowDTO getShowDetails(Long showId) {
        Show show = showRepository.findById(showId).get();
        List<Buyer> buyerList = buyerRepository.findByShowId(showId); 

        ShowDTO showDTO = new ShowDTO();
        showDTO.setShowId(showId);
        showDTO.setCancellationWindow(show.getCancellationWindow());
        showDTO.setBuyerDetails(buyerList);
        showDTO.setAvailSeats(getShowAvailableSeats(showId));

        return showDTO;
    }

    public String bookTicket(BuyerDTO buyer) {
        String seatList = buyer.getSeatNumberList().trim();
        List<String> seatNos = Arrays.asList(seatList.split("\\s*,\\s*"));
        List<Buyer> buyerList = new ArrayList<>();

        seatNos.stream().forEach(
            seat -> {
                Buyer buyere = new Buyer();
                buyere.setSeatNumber(seat);
                buyere.setBuyerPhoneNumber(buyer.getBuyerPhoneNumber());
                buyere.setShowId(buyer.getShowId());
                buyere.setBookingTime(LocalDateTime.now());
                buyerList.add(buyere);
            }
        );

        List<Buyer> list1 = buyerRepository.saveAll(buyerList);
        return list1.stream().map(Buyer::getTicketId).collect(Collectors.toList()).toString();
    }
}