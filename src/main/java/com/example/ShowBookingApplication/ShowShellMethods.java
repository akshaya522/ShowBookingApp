package com.example.ShowBookingApplication;

import java.util.List;

import com.example.ShowBookingApplication.ShellHelper.PromptColor;

import org.jline.terminal.TerminalBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ShellHelper shellHelper;

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
            shellHelper.print("Welcome Admin user!", PromptColor.WHITE);
            shellHelper.print("--- 1. Use Setup to setup shows (Eg: Setup --rows 10 --seats 5 --cancellationMins 10)", PromptColor.CYAN);
            return shellHelper.getPromptMessage("--- 2. Use View to view shows (Eg. View 1)");
        } else if (BUYER.equals(user.trim().toLowerCase())) {
            isBuyer = true;
            isAdmin = false;
            shellHelper.print("Welcome Buyer user!", PromptColor.WHITE);
            shellHelper.print("--- 1. Use Availability to view show seat availability (Eg: Availability 1)", PromptColor.CYAN);
            shellHelper.print("--- 2. Use Book to book tickets (Eg: Book --showId 1 --seats \"A1,A2\" --phoneNo 81234567)", PromptColor.CYAN);
            return shellHelper.getPromptMessage("--- 3. Use Cancel to cancel tickets (Eg. Cancel --ticketNo 1 --phoneNo 81234567)");
        } else {
            return shellHelper.getErrorMessage("Invalid Input! Enter 'Admin' or 'Buyer'");
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
            return shellHelper.getWarnMessage("Only Admins are allowed to Setup shows. Change to Admin User");
        }
    }

    @ShellMethod(value = "Get Show Details", key = "View")
    public String view(Integer a) {
        if (isAdmin) {
            String result = this.bookTicketService.getShowDetails(a.longValue());
            return result;
        } else {
            return shellHelper.getWarnMessage("Only Admins are allowed to View shows. Change to Admin User");
        }
    }

    @ShellMethod(value = "Get show availability", key = "Availability")
    public String availability(Integer a) {
        if (isBuyer) {
            return this.bookTicketService.getShowAvailableSeats(a.longValue());
        }  else {
            return shellHelper.getWarnMessage("Only Buyers are allowed to check show Availability. Change to Buyer User");
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
            return shellHelper.getWarnMessage("Only Buyers are allowed to book tickets. Change to Buyer User");
        }
    }

    @ShellMethod(value = "Cancel booking", key = "Cancel")
    public String cancelBooking(Integer ticketNo, Integer phoneNo) {
        if (isBuyer) {
            String result = this.bookTicketService.cancelBooking(ticketNo.longValue(), phoneNo);
            return result;
        } else {
            return shellHelper.getWarnMessage("Only Buyers are allowed to cancel tickets. Change to Buyer User");
        }
    }
}