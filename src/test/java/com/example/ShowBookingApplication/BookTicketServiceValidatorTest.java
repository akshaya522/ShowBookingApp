package com.example.ShowBookingApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BookTicketServiceValidatorTest {

    @Mock 
    private ShowRepository showRepository;

    @Mock 
    private BuyerRepository buyerRepository;

    @InjectMocks 
    private BookTicketServiceValidator bookTicketServiceValidator;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this);}

    @Test 
    public void setupShowValidator_invalidRows_showThrowException() {
        Show show = getMockShow(true, false, false);
        String res = bookTicketServiceValidator.createShowValidator(show);

        Assertions.assertEquals("Number of rows in show cannot be more than 26!", res);
    }

    @Test 
    public void setupShowValidator_invalidSeats_showThrowException() {
        Show show = getMockShow(false, true, false);
        String res = bookTicketServiceValidator.createShowValidator(show);

        Assertions.assertEquals("Number of seats per row cannot be more than 10!", res);
    }

    @Test 
    public void setupShowValidator_invalidCancellation_showThrowException() {
        Show show = getMockShow(false, false, true);
        String res = bookTicketServiceValidator.createShowValidator(show);

        Assertions.assertEquals("Cancellation window cannot be more than 120 minutes!", res);
    }

    @Test 
    public void setupShowValidator_validShow_showNotThrowException() {
        Show show = getMockShow(false, false, false);
        String res = bookTicketServiceValidator.createShowValidator(show);

        Assertions.assertEquals(null, res);
    }

    @Test 
    public void bookTicketValidator_fullShow_showThrowException() {
        BuyerDTO buyerDTO = getMockBuyerDTO();

        String res = bookTicketServiceValidator.bookTicketValidator(buyerDTO, Arrays.asList() , Arrays.asList());
        Assertions.assertEquals("There are no more available seats for this show", res);
    }

    @Test 
    public void bookTicketValidator_emptySeatList_showThrowException() {
        BuyerDTO buyerDTO = getMockBuyerDTO();

        String res = bookTicketServiceValidator.bookTicketValidator(buyerDTO, Arrays.asList("A1") , Arrays.asList());
        Assertions.assertEquals("Seat number lists cannot be empty", res);
    }

    @Test 
    public void bookTicketValidator_existingBooking_showThrowException() {
        BuyerDTO buyerDTO = getMockBuyerDTO();
        Buyer buyer = getMockBuyer();
        List<Buyer> buyerList = new ArrayList<>();
        buyerList.add(buyer);

        Mockito.doReturn(buyerList).when(this.buyerRepository).findByBuyerPhoneNumber(buyerDTO.getBuyerPhoneNumber());

        String res = bookTicketServiceValidator.bookTicketValidator(buyerDTO, Arrays.asList("A1") , Arrays.asList("A1"));
        Assertions.assertEquals("There is an existing booking with this number. Only one booking per phone number is allowed", res);
    }

    @Test 
    public void bookTicketValidator_invalidSeatNo_showThrowException() {
        BuyerDTO buyerDTO = getMockBuyerDTO();

        String res = bookTicketServiceValidator.bookTicketValidator(buyerDTO, Arrays.asList("A1", "A2", "Z1") , Arrays.asList("Z1", "Z2"));
        Assertions.assertEquals("There are invalid seat numbers: [Z2]", res);
    }

    private Show getMockShow(Boolean invalidRows, Boolean invalidSeats, Boolean invalidCancellation) {
        Show show = new Show();
        show.setShowId(200L);
        show.setNumOfRows(invalidRows ? 90 : 5);
        show.setNumOfSeatsPerRow(invalidSeats ? 90 : 5);
        show.setCancellationWindow(invalidCancellation ? 300 : 30);
        return show;
    }

    private Buyer getMockBuyer() {
        Buyer buyer = new Buyer();
        buyer.setBookingTime(LocalDateTime.now());
        buyer.setBuyerPhoneNumber(8231);
        buyer.setSeatNumber("A1");
        buyer.setTicketId(1L);
        buyer.setShowId(200L);
        return buyer;
    }

    private BuyerDTO getMockBuyerDTO() {
        BuyerDTO buyer = new BuyerDTO();
        buyer.setBuyerPhoneNumber(8231);
        buyer.setSeatNumberList("A1, A2");
        buyer.setTicketId(1L);
        buyer.setShowId(200L);
        return buyer;
    }  
}