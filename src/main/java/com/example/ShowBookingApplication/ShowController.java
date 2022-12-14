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

import java.util.List;

@RestController
public class ShowController {

    private final Logger log = LoggerFactory.getLogger(ShowController.class);
    private final BookTicketService bookTicketService;
    private final BookTicketServiceValidator bookTicketServiceValidator;

    @Autowired
    private ShowRepository showRepository;

    @Autowired 
    private BuyerRepository buyerRepository;

    public ShowController(
        BookTicketService bookTicketService,
        BookTicketServiceValidator bookTicketServiceValidator
    ){
        this.bookTicketService = bookTicketService;
        this.bookTicketServiceValidator = bookTicketServiceValidator;
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

    @PostMapping("/bookTicket")
    public String bookTicket(@RequestBody BuyerDTO buyer) {
        return this.bookTicketService.bookTicket(buyer);
    }

    @GetMapping("/cancelBooking/{ticketNo}/{mobileNo}") 
    public String cancelBooking(@PathVariable Integer ticketNo, @PathVariable Integer mobileNo) {
        this.bookTicketService.cancelBooking(ticketNo.longValue(), mobileNo);
        return "Ticket deleted...";
    }
}