package com.example.ShowBookingApplication;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.ShowBookingApplication.ShellHelper.PromptColor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class BookTicketServiceTest {

    @Mock 
    private ShowRepository showRepository;

    @Mock 
    private ShellHelper shellHelper;

    @Mock 
    private BuyerRepository buyerRepository;

    @Mock  
    private BookTicketServiceValidator bookTicketServiceValidator;

    @InjectMocks 
    private BookTicketService bookTicketService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this);}

    @Test 
    public void getShowDetails_withInvalidShowId_shouldThrowException() {
        Long mockId = 200L;
        Mockito.doReturn(null).when(this.showRepository).findById(mockId);
        Mockito.doReturn(null).when(this.shellHelper).getErrorMessage(Mockito.anyString());
        String res = this.bookTicketService.getShowDetails(mockId);

        Assertions.assertEquals("Invalid Show Id! Show Id entered: " + mockId, res);
    }

    @Test 
    public void setupShow_successSetup_shouldNotThrowException() {
    
       Show mockShow = getMockShow(false, false, false);
       Mockito.when(showRepository.save(Mockito.any(Show.class)))
                .thenReturn(mockShow);
       String res = this.bookTicketService.saveShow(mockShow);

        Assertions.assertEquals("Show successfully setup! Created Show Id: 200", res);
    }

    @Test 
    public void getAllSeats_withInvalidRows_shouldThrowException() {
       List<String> res = this.bookTicketService.getAllSeats(30, 10);

        Assertions.assertEquals(null, res);
    }

    @Test 
    public void getAvailSeats_withInvalidRows_shouldThrowException() {
        Show mockShow = getMockShow(false, false, false);
        Mockito.doReturn(Collections.emptyList()).when(this.buyerRepository).findByShowId(mockShow.getShowId());

        List<String> res = this.bookTicketService.getAvailSeats(mockShow);

        Assertions.assertEquals("[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, C1, C2, C3, C4, C5, D1, D2, D3, D4, D5, E1, E2, E3, E4, E5]", res.toString());
    }

    @Test 
    public void cancelTicket_withinCancellation_shouldThrowException() {
        Buyer mockBuyer = getMockBuyer();
        Show mockShow = getMockShow(false, false, false);
        Mockito.doReturn(Optional.of(mockBuyer)).when(this.buyerRepository).findByTicketIdAndBuyerPhoneNumber(mockBuyer.getTicketId(), mockBuyer.getBuyerPhoneNumber());
        Mockito.doReturn(Optional.of(mockShow)).when(this.showRepository).findById(mockBuyer.getShowId());
        String res = this.bookTicketService.cancelBooking(mockBuyer.getTicketId(), mockBuyer.getBuyerPhoneNumber());

        Assertions.assertEquals("Ticket 1 cancelled succesfully", res);
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
