package com.example.trainkata;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketOfficeBaseTest {
    protected List<SeatWithBookingReference> emptySeatsWith1CoachAndNSeatsAvailable(String coach, int nbSeatsAvailable) {
        return IntStream.rangeClosed(1, nbSeatsAvailable)
                .mapToObj(i -> new SeatWithBookingReference(coach, i, BookingReferenceId.NULL))
                .collect(Collectors.toList());
    }

    protected List<SeatWithBookingReference> seatsWith1Coach3SeatsAnd1Available() {
        return List.of(
                new SeatWithBookingReference("A", 1, new BookingReferenceId("34Dsq")),
                new SeatWithBookingReference("A", 2, BookingReferenceId.NULL),
                new SeatWithBookingReference("A", 3, new BookingReferenceId("34Dsq"))
        );
    }
}
