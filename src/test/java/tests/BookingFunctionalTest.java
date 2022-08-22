package tests;

import api.objects.BookingPojo;
import api.objects.request.UpdateTotalPriceRequest;
import api.objects.response.CreateBookingResponse;
import api.objects.request.UpdateFirstNameRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.BookingGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_METHOD_NOT_ALLOWED;

@Epic("Functional tests")
public class BookingFunctionalTest extends BaseTest {


    @Test
    @DisplayName("Get All Booking IDs")
    @Tag("smoke") @Tag("regression")
    public void testGetBookingIdsAll() {
        List<Integer> bookingIds = api.booking.getBookingIds();

        Assertions.assertFalse(bookingIds.isEmpty());
    }

    @Test
    @DisplayName("Get Booking IDs with filter by firstname")
    @Tag("smoke") @Tag("regression")
    public void testGetBookingIdsByFirstName() {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);

        List<Integer> bookingIds = api.booking.getBookingIdsByFirstName(booking.getFirstName());

        Assertions.assertTrue(bookingIds.contains(created.getBookingId()));
    }

    @Test
    @DisplayName("Get Booking IDs with two filters by firstname and lastname")
    @Description("Request with two filters returns the entity that satisfies both filters")
    @Tag("regression")
    public void testGetBookingIdsByFirstAndLastName() {
        BookingPojo booking1 = BookingGenerator.getBookingWithProvidedName("Robin", "van Persie");
        BookingPojo booking2 = BookingGenerator.getBookingWithProvidedName("Robin", "Bergkamp");
        BookingPojo booking3 = BookingGenerator.getBookingWithProvidedName("Dennis", "Bergkamp");
        CreateBookingResponse created1 = api.booking.createBooking(booking1);
        CreateBookingResponse created2 = api.booking.createBooking(booking2);
        CreateBookingResponse created3 = api.booking.createBooking(booking3);

        List<Integer> bookingIds = api.booking.getBookingIdsByFirstAndLastName("Robin", "Bergkamp");

        Assertions.assertAll(
                () -> Assertions.assertTrue(bookingIds.contains(created2.getBookingId())),
                () -> Assertions.assertFalse(bookingIds.contains(created1.getBookingId())),
                () -> Assertions.assertFalse(bookingIds.contains(created3.getBookingId()))
        );
    }

    @Test
    @DisplayName("Delete Booking by existing ID")
    @Tag("smoke") @Tag("regression")
    public void testDeleteBookingById() {
        CreateBookingResponse created = api.booking.createBooking(BookingGenerator.getSimpleBooking());
        Response response = api.booking.deleteBookingById(created.getBookingId());

        Assertions.assertEquals(SC_CREATED, response.statusCode());
        Assertions.assertThrows(RuntimeException.class,
                () -> api.booking.getBookingById(created.getBookingId()));
    }

    @Test
    @DisplayName("Delete Booking by non-existing ID")
    @Tag("regression")
    public void testDeleteBookingThatDoesNotExist() {
        Response response = api.booking.deleteBookingById(123456);

        Assertions.assertEquals(SC_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    @DisplayName("Delete Booking by existing ID without auth token")
    @Tag("regression")
    public void testDeleteBookingWithoutToken() {
        CreateBookingResponse created = api.booking.createBooking(BookingGenerator.getSimpleBooking());
        Response response = api.booking.deleteBookingByIdWithoutToken(created.getBookingId());

        Assertions.assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    @DisplayName("Update firstname with valid value")
    @Tag("smoke") @Tag("regression")
    public void testUpdateBookingFirstName() {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);
        UpdateFirstNameRequest replace = BookingGenerator.getBookingUpdateFirstName("Evgeny");

        BookingPojo updated = api.booking.updateBooking(replace, created.getBookingId());

        Assertions.assertAll(
                () -> Assertions.assertEquals("Evgeny", updated.getFirstName()),
                () -> Assertions.assertEquals(booking.getLastName(), updated.getLastName()),
                () -> Assertions.assertEquals(booking.getTotalPrice(), updated.getTotalPrice()),
                () -> Assertions.assertEquals(booking.getBookingDates(), updated.getBookingDates())
        );
    }

    @ParameterizedTest  @Disabled
    @ValueSource(strings = {"<script>alert('xss')</script>", "<a href='javascript:alert(1)'>XXX</a>", "<img src=1 href=1 onerror='javascript:alert(1)'></img>"})
    @DisplayName("Update firstname with vulnerable payload")
    @Description("Firstname value is filtered for vulnerable input")
    @Tag("regression")
    public void testUpdateBookingFirstNameWithVulnerablePayload(String payload) {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);
        UpdateFirstNameRequest replace = BookingGenerator.getBookingUpdateFirstName(payload);

        Response response = api.booking.updateBookingResponse(replace, created.getBookingId());

        Assertions.assertEquals(SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Update totalprice with valid value")
    @Tag("regression")
    public void testUpdateBookingTotalPrice() {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);
        UpdateTotalPriceRequest replace = BookingGenerator.getBookingUpdateTotalPrice(100);

        BookingPojo updated = api.booking.updateBooking(replace, created.getBookingId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(100, updated.getTotalPrice()),
                () -> Assertions.assertEquals(booking.getFirstName(), updated.getFirstName()),
                () -> Assertions.assertEquals(booking.getLastName(), updated.getLastName()),
                () -> Assertions.assertEquals(booking.getBookingDates(), updated.getBookingDates())
        );
    }

    @Test  @Disabled
    @DisplayName("Update totalprice with big number")
    @Description("Boundary check for the field")
    @Tag("regression")
    public void testUpdateBookingTotalPriceWithBigNumber() {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);
        UpdateTotalPriceRequest replace = BookingGenerator.getBookingUpdateTotalPrice(123456789012345678L);

        BookingPojo updated = api.booking.updateBooking(replace, created.getBookingId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(123456789012345678L, updated.getTotalPrice()),
                () -> Assertions.assertEquals(booking.getFirstName(), updated.getFirstName()),
                () -> Assertions.assertEquals(booking.getLastName(), updated.getLastName()),
                () -> Assertions.assertEquals(booking.getBookingDates(), updated.getBookingDates())
        );
    }

    @Test  @Disabled
    @DisplayName("Update totalprice with negative number")
    @Description("There is no ability to update totalprice with negative number")
    @Tag("regression")
    public void testUpdateBookingTotalPriceWithNegativeNumber() {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);
        UpdateTotalPriceRequest replace = BookingGenerator.getBookingUpdateTotalPrice(-100);

        Response response = api.booking.updateBookingResponse(replace, created.getBookingId());

        Assertions.assertEquals(SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Update Booking with all fields using partial update")
    @Description("It is possible to update several fields using partial update")
    @Tag("regression")
    public void testUpdateBookingFullData() {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);
        BookingPojo replace = BookingGenerator.getUpdatedBooking();

        BookingPojo updated = api.booking.updateBooking(replace, created.getBookingId());

        Assertions.assertEquals(replace, updated);
    }

    @Test
    @DisplayName("Update Booking without auth token")
    @Tag("regression")
    public void testUpdateBookingWithoutToken() {
        BookingPojo booking = BookingGenerator.getSimpleBooking();
        CreateBookingResponse created = api.booking.createBooking(booking);
        BookingPojo replace = BookingGenerator.getUpdatedBooking();

        Response response = api.booking.updateBookingWithoutToken(replace, created.getBookingId());

        Assertions.assertEquals(SC_FORBIDDEN, response.statusCode());
    }

}
