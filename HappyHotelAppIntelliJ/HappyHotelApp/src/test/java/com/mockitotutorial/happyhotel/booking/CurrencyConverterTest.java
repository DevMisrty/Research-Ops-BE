package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mockStatic;


class CurrencyConverterTest {

    BookingService bookingService;
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
    @CsvSource({
            "10.0, 8.5",
            "25.0,21.25",
            "50.0,42.5"
    })
    void should_Expect_Only_double_Arguments(double usd, double expected){
        BookingRequest request = new BookingRequest(
                "1", LocalDate.of(2025,2,10),LocalDate.of(2025,2,15)
                ,5,true);

        try(MockedStatic<CurrencyConverter> converterMock = mockStatic(CurrencyConverter.class)){
            converterMock.when(()-> CurrencyConverter.toEuro(usd))
                            .thenAnswer((inv)-> (double)inv.getArgument(0) * 0.85);
            assertEquals(expected,bookingService.calculatePrice(usd));

        }
    }
}