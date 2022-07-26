package org.example.movie.seat.inventory.aggregate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.movie.seat.inventory.command.CalculatePriceCommand;
import org.example.movie.seat.inventory.event.CalculatePriceEvent;
import org.springframework.beans.BeanUtils;

@Slf4j
@Aggregate
@NoArgsConstructor
public class CheckBookingInformationAggregate {

    @AggregateIdentifier
    private String uniqueId;

   @CommandHandler
    public void handleCalculatePricetCommand(CalculatePriceCommand calculatePriceCommand) {
        CalculatePriceEvent calculatePriceEvent = CalculatePriceEvent.of();
        BeanUtils.copyProperties(calculatePriceEvent, calculatePriceEvent);
        AggregateLifecycle.apply(calculatePriceEvent);
    }

    @EventSourcingHandler
    public void on(CalculatePriceEvent calculatePriceEvent) {
        log.info("ReserveSeatEvent Sourcing Done");
        this.uniqueId = calculatePriceEvent.getUniqueId();
    }
}
