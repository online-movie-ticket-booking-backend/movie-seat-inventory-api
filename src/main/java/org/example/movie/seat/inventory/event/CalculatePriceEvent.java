package org.example.movie.seat.inventory.event;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.movie.core.common.booking.BookingInformation;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class CalculatePriceEvent {
    private String uniqueId;
    private BookingInformation bookingInformation;
}
