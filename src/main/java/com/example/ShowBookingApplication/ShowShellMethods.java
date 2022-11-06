package com.example.ShowBookingApplication;

import java.util.List;

import org.jline.terminal.TerminalBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.jline.terminal.Terminal;

@ShellComponent
public class ShowShellMethods {

    private final BookTicketService bookTicketService;

    public ShowShellMethods(BookTicketService bookTicketService) {
        this.bookTicketService = bookTicketService;
    }

    Boolean isAdmin = false;
    Boolean isBuyer = false;
    private final String ADMIN = "admin";
    private final String BUYER = "buyer";

    @ShellMethod(value = "Set User Role", key = "User")
    public String view(@ShellOption() String user) {
        if (ADMIN.equals(user.trim().toLowerCase())) {
            isAdmin = true;
            isBuyer = false;
            return "Welcome admin user Enter Setup to start!";
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
            String result = this.bookTicketService.getShowDetails(a.longValue());
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
            return "Only Buyers are allowed to check show Availability";
        }
    }

    @ShellMethod(value = "Book show", key = "Book")
    public String bookShow(Integer showId, String seats, Integer phoneNo) {
        if (isBuyer) {
            BuyerDTO buyer = new BuyerDTO();
            buyer.setShowId(showId.longValue());
            buyer.setSeatNumberList(seats);
            buyer.setBuyerPhoneNumber(phoneNo);

            String result = this.bookTicketService.bookTicket(buyer);
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