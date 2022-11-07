package com.example.ShowBookingApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

@Service
public class BookTicketService {
    private final BuyerRepository buyerRepository;
    private final ShowRepository showRepository;
    private final BookTicketServiceValidator bookTicketServiceValidator;

    @Autowired
    ShellHelper shellHelper;

    public BookTicketService(
        BuyerRepository buyerRepository,
        ShowRepository showRepository,
        BookTicketServiceValidator bookTicketServiceValidator
    ){
        this.buyerRepository = buyerRepository;
        this.showRepository = showRepository;
        this.bookTicketServiceValidator = bookTicketServiceValidator;
    }

    /**
     * This function validates user show input and saves valid show 
     * @param show - Show entity 
     * @return - returns created showId 
     */
    public String saveShow(Show show){
        /** Validate show input */
        String validator = this.bookTicketServiceValidator.createShowValidator(show);
        if (validator == null) {
            Show show1 = this.showRepository.save(show);
            return "Show successfully setup! Created Show Id: " + show1.getShowId().toString();
        }
        return shellHelper.getErrorMessage(validator);
    }

    /**
     * This function get all seats in a show 
     * @param rows - number of rows in show 
     * @param seatsPerRow - number of seats per row in show 
     * @return - returns list of seat numbers
     */
    public List<String> getAllSeats(Integer rows, Integer seatsPerRow) {
        List<String> res = new ArrayList<>();
        /** Invalid number of seats */
        if(rows > 0 && rows < 27){
            for(int i = 1; i < rows+1; i++){
                String rowVal = String.valueOf((char)(i+64));
                for(int j = 0; j<seatsPerRow; j++){
                    String seat = rowVal + String.valueOf(j+1);
                    res.add(seat);
                }
            }
        } else {
            return null;
        }
        return res;
    }
    

    /**
     * This function get all available seats in a show
     * @param show - show entity  
     * @return - returns list of available seat numbers for show 
     */
    public List<String> getAvailSeats(Show show) {
        List<Buyer> buyerList = buyerRepository.findByShowId(show.getShowId());
        List<String> bookedSeats = buyerList.stream().map(Buyer::getSeatNumber).collect(Collectors.toList());
        List<String> allSeats = this.getAllSeats(show.getNumOfRows(), show.getNumOfSeatsPerRow());
        allSeats.removeAll(bookedSeats);

        return allSeats;
    }

    /**
     * This function validates showId and return list of available seat number
     * @param showId - show Id 
     * @return - returns list of available seat numbers for show 
     */
    public String getShowAvailableSeats(Long showId) {
        Optional<Show> show = showRepository.findById(showId);
        if (show != null && show.isPresent()) {
            List<String> availSeats = getAvailSeats(show.get());
            return shellHelper.getInfoMessage("Available seats for Show Id " + showId + ": ") + availSeats;

        } else {
            return shellHelper.getErrorMessage("Invalid Show Id! Show Id entered: " + showId);
        }
    }

    /**
     * This function validates showId and return show details 
     * @param showId - show Id 
     * @return - return showId, cancellation window minutes, show available seats, and existing buyer list 
     */
    public String getShowDetails(Long showId) {
        Optional<Show> show = showRepository.findById(showId);

        if (show != null && show.isPresent()) {
            List<Buyer> buyerList = buyerRepository.findByShowId(showId);
            /** Sort existing booked seats by seat number */
            buyerList.sort((e1, e2) -> e1.getSeatNumber().compareTo(e2.getSeatNumber())); 
            List<String> buyerStrData = new ArrayList<>();
            buyerList.forEach(i -> {
                buyerStrData.add(shellHelper.getInfoMessage("Ticket #: ") + i.getTicketId() + ", " + shellHelper.getInfoMessage("Buyer Phone #: ") + i.getBuyerPhoneNumber() + ", " + shellHelper.getInfoMessage("Seats Allocated: ") + i.getSeatNumber());
            });

            String result = shellHelper.getInfoMessage("Show Id: ") + showId.toString() + "\n" + 
            shellHelper.getInfoMessage("Show Cancellation Window Time: ") + show.get().getCancellationWindow() +  "\n" + 
            getShowAvailableSeats(showId) + "\n"; 
            
            /** If buyer list exists add buyer data */
            if (buyerStrData.size() > 0) {
                result = result + shellHelper.getInfoMessage("Buyer list: ") + "\n" + StringUtils.join(buyerStrData, "\n");
            }
            return result;
        } else {
            return shellHelper.getErrorMessage("Invalid Show Id! Show Id entered: " + showId);
        }
    }

    /**
     * This function validating user input and books tickets when valid input
     * 
     * @param buyer - BuyerDTO - with seatList, phoneNumber, showId 
     * @return - return list of booked ticket numbers
     */
    public String bookTicket(BuyerDTO buyer) {
        Optional<Show> show = this.showRepository.findById(buyer.getShowId());

        if (show != null && show.isPresent()) {
            List<String> seatsToBook = Arrays.asList(buyer.getSeatNumberList().trim().split("\\s*,\\s*"));
            List<String> availSeats = getAvailSeats(show.get());
            String validator = this.bookTicketServiceValidator.bookTicketValidator(buyer, availSeats, seatsToBook);

            if (validator == null) {
                List<Buyer> buyerList = new ArrayList<>();

                seatsToBook.stream().forEach(
                    seat -> {
                        Buyer ticket = new Buyer();
                        ticket.setSeatNumber(seat.toUpperCase());
                        ticket.setBuyerPhoneNumber(buyer.getBuyerPhoneNumber());
                        ticket.setShowId(buyer.getShowId());
                        ticket.setBookingTime(LocalDateTime.now());
                        buyerList.add(ticket);
                    }
                );

                List<Buyer> list1 = buyerRepository.saveAll(buyerList);
                List<String> buyerStrData = new ArrayList<>();
                list1.forEach(i -> {
                    buyerStrData.add(shellHelper.getInfoMessage("Seat booked: ") + i.getSeatNumber() + ", " + shellHelper.getInfoMessage("Ticket #: ") + i.getTicketId());
                });

                return shellHelper.getInfoMessage("Successfully Booked! Ticket Ids booked: ") + "\n" + StringUtils.join(buyerStrData, "\n");
            } else {
                return shellHelper.getErrorMessage(validator);
            }
        } else {
            return shellHelper.getErrorMessage("Invalid Show Id: ") + buyer.getShowId().toString();
        }
    }

    /**
     * This function cancels valid tickets 
     * 
     * @param ticketNo - ticketNo to be cancelled
     * @param phoneNo - phoneNo of buyer  
     * @return - result of ticket cancellation
     */
    public String cancelBooking(Long ticketNo, Integer phoneNumber) {
        Optional<Buyer> ticket = buyerRepository.findByTicketIdAndBuyerPhoneNumber(ticketNo, phoneNumber);

        if (ticket != null && ticket.isPresent()) {
            LocalDateTime currTime = LocalDateTime.now();
            Integer cancellationWindow = showRepository.findById(ticket.get().getShowId()).get().getCancellationWindow();
            LocalDateTime cancellationTime = ticket.get().getBookingTime().plusMinutes(cancellationWindow);

            if (currTime.isBefore(cancellationTime)){
                buyerRepository.delete(ticket.get());
                return "Ticket " + ticketNo + " cancelled succesfully";
            } else {
                return shellHelper.getErrorMessage("Cannot cancel ticket pass cancellation window!");
            }
        } else {
            return shellHelper.getWarnMessage("No existing booking found for ticket number and phone number!");
        }
    }
}