package utils;

import api.objects.BookingDates;
import api.objects.BookingPojo;
import api.objects.request.UpdateFirstNameRequest;
import api.objects.request.UpdateTotalPriceRequest;


public class BookingGenerator {

    private static BookingDates getBookingDates(String checkIn, String checkOut) {
        return BookingDates.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build();
    }

    public static BookingPojo getSimpleBooking() {
        return BookingPojo.builder()
                .firstName("Yauheni")
                .lastName("Test")
                .totalPrice(12345)
                .depositPaid(false)
                .bookingDates(BookingGenerator.getBookingDates("2019-01-01", "2020-01-01"))
                .additionalNeeds("Some text for additional needs")
                .build();
    }

    public static BookingPojo getUpdatedBooking() {
        return BookingPojo.builder()
                .firstName("Evgeny")
                .lastName("Update")
                .totalPrice(100)
                .depositPaid(true)
                .bookingDates(BookingGenerator.getBookingDates("2021-01-01", "2021-01-15"))
                .additionalNeeds("Updated text for additional needs")
                .build();
    }

    public static BookingPojo getBookingWithProvidedName(String firstName, String lastName) {
        return BookingPojo.builder()
                .firstName(firstName)
                .lastName(lastName)
                .totalPrice(12345)
                .depositPaid(false)
                .bookingDates(BookingGenerator.getBookingDates("2019-01-01", "2020-01-01"))
                .additionalNeeds("Some text for additional needs")
                .build();
    }

    public static UpdateFirstNameRequest getBookingUpdateFirstName(String firstName) {
        return UpdateFirstNameRequest.builder()
                .firstName(firstName)
                .build();
    }

    public static UpdateTotalPriceRequest getBookingUpdateTotalPrice(long totalPrice) {
        return UpdateTotalPriceRequest.builder()
                .totalPrice(totalPrice)
                .build();
    }

}