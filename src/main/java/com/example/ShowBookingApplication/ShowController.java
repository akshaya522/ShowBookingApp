package com.example.ShowBookingApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ShowController {

    private final Logger log = LoggerFactory.getLogger(ShowController.class);
    private final BookTicketService bookTicketService;

    @Autowired
    private ShowRepository showRepository;

    @Autowired 
    private BuyerRepository buyerRepository;

    public ShowController(
        BookTicketService bookTicketService
    ){
        this.bookTicketService = bookTicketService;
    }


    @PostMapping("/saveShow")
    public String saveShow(@RequestBody Show show) {
        showRepository.save(show);
        return "Show saved...";
    }

    @GetMapping("/getAllShows")
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @GetMapping("/getAllTickets")
    public List<Buyer> getAllTickets() {
        return buyerRepository.findAll();
    }

    @GetMapping("/getSeats/{showId}")
    public List<String> getSeats(@PathVariable Integer showId) {
        Long id = showId.longValue();
        Show show = showRepository.findById(id).get();
        return bookTicketService.getAllSeats(show.getNumOfRows(), show.getNumOfSeatsPerRow());
    }

    @GetMapping("/getAvailSeats/{showId}")
    public List<String> getAvailSeats(@PathVariable Integer showId) {
        Long id = showId.longValue();
        return bookTicketService.getShowAvailableSeats(id);
    }

    @PostMapping("/bookTicket")
    public String bookTicket(@RequestBody BuyerDTO buyer) {
        String seatList = buyer.getSeatNumberList();
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

    @GetMapping("/cancelBooking/{ticketNo}/{mobileNo}") 
    public String cancelBooking(@PathVariable Integer ticketNo, @PathVariable Integer mobileNo) {
        this.bookTicketService.cancelBooking(ticketNo.longValue(), mobileNo);
        return "Ticket deleted...";
    }

    @GetMapping("/getShowDetails/{showId}") 
    public ShowDTO getShowDetails(@PathVariable Integer showId) {
        return this.bookTicketService.getShowDetails(showId.longValue());
    }
}