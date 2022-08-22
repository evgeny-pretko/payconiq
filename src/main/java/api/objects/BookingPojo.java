package api.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPojo {
    @JsonProperty(value = "firstname")
    private String firstName;
    @JsonProperty(value = "lastname")
    private String lastName;
    @JsonProperty(value = "totalprice")
    private long totalPrice;
    @JsonProperty(value = "depositpaid")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Boolean depositPaid;
    @JsonProperty(value = "bookingdates")
    private BookingDates bookingDates;
    @JsonProperty(value = "additionalneeds")
    private String additionalNeeds;

}