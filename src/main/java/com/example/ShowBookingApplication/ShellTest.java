package com.example.ShowBookingApplication;

import java.util.List;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellTest {

    private final BookTicketService bookTicketService;

    public ShellTest(
        BookTicketService bookTicketService
    ){
        this.bookTicketService = bookTicketService;
    }

    Boolean isAdmin = false;
    Boolean isBuyer = false;

    @ShellMethod(value = "Set User Role", key = "User")
    public String view(String user) {

        if (user.equals("A")) {
            isAdmin = true;
            isBuyer = false;
        } else {
            isBuyer = true;
            isAdmin = false;
        }

        return "Welcome " +  (isAdmin ? "admin user!" : "buyer user!");
    }

    @ShellMethod(value = "Create Show", key = "Setup")
    public String setup(Integer rows, Integer seats, Integer cancellationMins) {
        if (isAdmin) {
            Show show = new Show();
            show.setNumOfRows(rows);
            show.setNumOfSeatsPerRow(seats);
            show.setCancellationWindow(cancellationMins);

            String result = this.bookTicketService.saveshow(show);
            return result;
        } else {
            return "Only Admins are allowed to Setup shows";
        }
    }

    @ShellMethod(value = "Get Show Details", key = "View")
    public String view(Integer a) {

        ShowDTO show = this.bookTicketService.getShowDetails(a.longValue());
        String result = "Show Id: " + show.getShowId().toString() + "\n" + 
        "Show Cancellation Window Time: " + show.getCancellationWindow() +  "\n" + 
        "Show Available Seats: " + show.getAvailSeats() + "\n";

        return result;
    }

    @ShellMethod(value = "Get show availability", key = "Availability")
    public String availability(Integer a) {

        List<String> seats = this.bookTicketService.getShowAvailableSeats(a.longValue());
        String result = "Available seats for showId: " + a + " are " + seats;
        return result;
    }

    // @ShellMethod(value = "Book show", key = "Book")
    // public String bookShow(Integer a) {

    //     List<String> seats = this.bookTicketService.getShowAvailableSeats(a.longValue());
    //     String result = "Available seats for showId: " + a + " are " + seats;
    //     return result;
    // }
}