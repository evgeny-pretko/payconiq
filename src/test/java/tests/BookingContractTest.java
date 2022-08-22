package tests;

import api.objects.request.UpdateFirstNameRequest;
import api.objects.response.CreateBookingResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.BookingGenerator;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("Contract tests")
public class BookingContractTest extends BaseTest {

    @Test
    @DisplayName("GetBookingIds contract test")
    @Description("Validate JSON schema for all IDs success response")
    @Tag("contract")
    public void testGetBookingIdsContract() {
        Response response = api.booking.getBookingIdsResponse();

        response.then().body(matchesJsonSchemaInClasspath("schemas/get_booking_ids_schema.json"));
    }

    @Test
    @DisplayName("PartialUpdateBooking contract test")
    @Description("Validate JSON schema for one field update success response")
    @Tag("contract")
    public void testUpdateBookingContract() {
        CreateBookingResponse created = api.booking.createBooking(BookingGenerator.getSimpleBooking());
        UpdateFirstNameRequest replace = BookingGenerator.getBookingUpdateFirstName("Evgeny");

        Response response = api.booking.updateBookingResponse(replace, created.getBookingId());

        response.then().body(matchesJsonSchemaInClasspath("schemas/update_booking_schema.json"));
    }
}