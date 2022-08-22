package api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDates {
    @JsonProperty(value = "checkin")
    private String checkIn;
    @JsonProperty(value = "checkout")
    private String checkOut;

}