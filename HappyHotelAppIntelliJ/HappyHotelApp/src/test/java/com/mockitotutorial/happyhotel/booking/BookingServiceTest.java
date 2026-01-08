package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingServiceTest {

    private  BookingService bookingService;
    private  PaymentService paymentServiceMock;
    private  RoomService roomServiceMock;
    private  MailSender mailSenderMock;
    private  BookingDAO bookingDaoMock;

    @BeforeEach
    void setup(){
        this.paymentServiceMock = Mockito.mock(PaymentService.class);
        this.roomServiceMock = Mockito.mock(RoomService.class);
        this.bookingDaoMock = Mockito.mock(BookingDAO.class);
        this.mailSenderMock = Mockito.mock(MailSender.class);
        this.bookingService = new BookingService(paymentServiceMock,roomServiceMock, bookingDaoMock,mailSenderMock);
    }

    @Test
    void test1(){
        when(this.roomServiceMock.getAvailableRooms())
                .thenReturn(new ArrayList<>(List.of(new Room("1",10), new Room("2",5))));
        assertEquals(15, bookingService.getAvailablePlaceCount());
    }

    @Test
    void test_with_multiple_then_returns(){
        when(this.roomServiceMock.getAvailableRooms())
                .thenReturn(new ArrayList<>(List.of(new Room("1",10))))
                .thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>(List.of(new Room("1",10),new Room("2",10))));
        assertAll(
                ()-> assertEquals(10,bookingService.getAvailablePlaceCount()),
                ()-> assertEquals(0,bookingService.getAvailablePlaceCount()),
                ()-> assertEquals(20,bookingService.getAvailablePlaceCount())
        );
    }

    @Test
    void test_that_throws_an_Exception(){
        when(this.roomServiceMock.findAvailableRoomId(null))
                .thenThrow(BusinessException.class);

        assertThrows( BusinessException.class    , ()-> bookingService.makeBooking(null));
    }
}