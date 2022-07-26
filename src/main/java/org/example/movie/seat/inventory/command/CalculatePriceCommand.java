package org.example.movie.seat.inventory.command;

import lombok.Data;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.example.movie.core.common.booking.BookingInformation;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class CalculatePriceCommand {

    @TargetAggregateIdentifier
    private String uniqueId;
    private BookingInformation bookingInformation;
}
