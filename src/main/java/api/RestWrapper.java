package api;

import api.objects.UserPojo;
import api.services.BookingService;
import io.restassured.http.ContentType;
import utils.ConfigProvider;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;

public class RestWrapper {
    private String token;

    public BookingService booking;

    private RestWrapper(String authToken) {
        this.token = authToken;
        booking = new BookingService(authToken);
    }

    public static boolean isHealthy() {
        return SC_CREATED == given()
                .baseUri(ConfigProvider.BASE_URL)
                .basePath("/ping")
                .get().statusCode();
    }

    public static RestWrapper createToken(String login, String password) {
        String token = given()
                .baseUri(ConfigProvider.BASE_URL)
                .contentType(ContentType.JSON)
                .basePath("/auth")
                .body(new UserPojo(login, password))
                .post()
                .then().extract().path("token");

        return new RestWrapper(token);
    }
}
