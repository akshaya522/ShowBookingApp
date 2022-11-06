package com.example.ShowBookingApplication;

import java.util.List;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShowShellMethods {

    private final BookTicketService bookTicketService;

    public ShowShellMethods (
        BookTicketService bookTicketService
    ){
        this.bookTicketService = bookTicketService;
    }

    Boolean isAdmin = false;
    Boolean isBuyer = false;
    private final String ADMIN = "admin";
    private final String BUYER = "buyer";


    @ShellMethod(value = "Set User Role", key = "User")
    public String view(String user) {

        if (ADMIN.equals(user.trim().toLowerCase())) {
            isAdmin = true;
            isBuyer = false;
            return "Welcome admin user!";
        } else if (BUYER.equals(user.trim().toLowerCase())) {
            isBuyer = true;
            isAdmin = false;
            return "Welcome buyer user!";
        } else {
            return "Invalid Input! Enter 'Admin' or 'Buyer'";
        }
    }

    @ShellMethod(value = "Create Show", key = "Setup")
    public String setup(Integer rows, Integer seats, Integer cancellationMins) {
        if (isAdmin) {
            Show show = new Show();
            show.setNumOfRows(rows);
            show.setNumOfSeatsPerRow(seats);
            show.setCancellationWindow(cancellationMins);

            String result = this.bookTicketService.saveShow(show);
            return result;
        } else {
            return "Only Admins are allowed to Setup shows";
        }
    }

    @ShellMethod(value = "Get Show Details", key = "View")
    public String view(Integer a) {
        if (isAdmin) {
            ShowDTO show = this.bookTicketService.getShowDetails(a.longValue());
            String result = "Show Id: " + show.getShowId().toString() + "\n" + 
            "Show Cancellation Window Time: " + show.getCancellationWindow() +  "\n" + 
            "Show Available Seats: " + show.getAvailSeats() + "\n";
            return result;
        } else {
            return "Only Admins are allowed to View shows";
        }
    }

    @ShellMethod(value = "Get show availability", key = "Availability")
    public String availability(Integer a) {
        if (isBuyer) {
            return this.bookTicketService.getShowAvailableSeats(a.longValue());
        }  else {
            return "Only Buyers are allowed to check availability of shows";
        }
    }

    @ShellMethod(value = "Book show", key = "Book")
    public String bookShow(Integer showId, String seats, Integer phoneNo) {
        if (isBuyer) {
            BuyerDTO buyer = new BuyerDTO();
            buyer.setShowId(showId.longValue());
            buyer.setSeatNumberList(seats);
            buyer.setBuyerPhoneNumber(phoneNo);

            String ticketIds = this.bookTicketService.bookTicket(buyer);
            String result = "Succesfully Booked! Ticket Ids Booked: " + ticketIds;
            return result;
        } else {
            return "Only Buyers are allowed to book tickets. Login as a Buyer";
        }
    }

    @ShellMethod(value = "Cancel booking", key = "Cancel")
    public String cancelBooking(Integer ticketNo, Integer phoneNo) {
        if (isBuyer) {
            String result = this.bookTicketService.cancelBooking(ticketNo.longValue(), phoneNo);
            return result;
        } else {
            return "Only Buyers are allowed to cancel tickets. Login as a Buyer";
        }
    }
}