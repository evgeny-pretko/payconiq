package api.services;

import api.objects.BookingPojo;
import api.objects.response.CreateBookingResponse;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ConfigProvider;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BookingService {
    private String basePath = "/booking";
    private String specificPath = "/booking/{id}";

    protected RequestSpecification BASE_SPEC;
    protected RequestSpecification AUTHENTICATED_SPEC;


    public BookingService(String authToken) {
        BASE_SPEC = new RequestSpecBuilder()
                .setBaseUri(ConfigProvider.BASE_URL)
                .setContentType(ContentType.JSON)
                .build();

        AUTHENTICATED_SPEC = new RequestSpecBuilder()
                .setBaseUri(ConfigProvider.BASE_URL)
                .setContentType(ContentType.JSON)
                .addHeader("Cookie", "token=" + authToken)
                .build();
    }

    @Step
    public CreateBookingResponse createBooking(BookingPojo booking) {
        return given().spec(BASE_SPEC)
                .body(booking)
                .post(basePath)
                .as(CreateBookingResponse.class);
    }

    @Step
    public Response getBookingIdsResponse() {
        return given().spec(BASE_SPEC)
                .get(basePath)
                .then().extract().response();
    }

    @Step
    public List<Integer> getBookingIds() {
        return given().spec(BASE_SPEC)
                .get(basePath)
                .then().extract().path("bookingid");
    }

    @Step
    public List<Integer> getBookingIdsByFirstName(String firstName) {
        return given().spec(BASE_SPEC)
                .queryParam("firstname", firstName)
                .get(basePath)
                .then().extract().path("bookingid");
    }

    @Step
    public List<Integer> getBookingIdsByFirstAndLastName(String firstName, String lastName) {
        return given().spec(BASE_SPEC)
                .queryParam("firstname", firstName)
                .queryParam("lastname", lastName)
                .get(basePath)
                .then().extract().path("bookingid");
    }

    @Step
    public BookingPojo getBookingById(int bookingId) {
        return given().spec(BASE_SPEC)
                .pathParam("id", bookingId)
                .get(specificPath)
                .as(BookingPojo.class);
    }

    @Step
    public Response deleteBookingById(int bookingId) {
        return given().spec(AUTHENTICATED_SPEC)
                .pathParam("id", bookingId)
                .delete(specificPath)
                .then().extract().response();
    }

    @Step
    public Response deleteBookingByIdWithoutToken(int bookingId) {
        return given().spec(BASE_SPEC)
                .pathParam("id", bookingId)
                .delete(specificPath)
                .then().extract().response();
    }

    @Step
    public BookingPojo updateBooking(Object replace, int bookingId) {
        return given().spec(AUTHENTICATED_SPEC)
                .body(replace)
                .pathParam("id", bookingId)
                .patch(specificPath)
                .as(BookingPojo.class);
    }

    @Step
    public Response updateBookingResponse(Object replace, int bookingId) {
        return given().spec(AUTHENTICATED_SPEC)
                .body(replace)
                .pathParam("id", bookingId)
                .patch(specificPath)
                .then().extract().response();
    }

    @Step
    public Response updateBookingWithoutToken(Object replace, int bookingId) {
        return given().spec(BASE_SPEC)
                .body(replace)
                .pathParam("id", bookingId)
                .patch(specificPath)
                .then().extract().response();
    }
}
