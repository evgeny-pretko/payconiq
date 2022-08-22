## Tech Assignment

> The task is:
> - API tests for [Hotel Booking Endpoints](http://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-GetBookings).
> - Create a test automation framework skeleton.
> - Make sure that “partialUpdateBooking”, “deleteBooking” and “getBookingIds” are working.

## How to use

### Locally

You must have Java 11 and Maven installed. Clone the project and type the following command:
```sh
mvn clean test
```
In case you require a report you could run:
```sh
mvn allure:serve
```

### CircleCI

CircleCI is integrated for Continuous Integration. You can find pipeline execution history here: https://app.circleci.com/pipelines/github/evgeny-pretko/payconiq

Allure automation result report is stored in `ARTIFACTS` tab: _allure/index.html_

## Technologies

- Java
- Maven
- JUnit 5
- Rest Assured
- Allure
- CircleCI