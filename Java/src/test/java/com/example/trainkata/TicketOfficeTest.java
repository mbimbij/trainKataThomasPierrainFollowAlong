package com.example.trainkata;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketOfficeTest {
    @Test
    void shouldReserveSeats_whenUnreservedSeatsAreAvailable() {
        BookingReferenceId expectedBookingId = new BookingReferenceId("expectedBookingId");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        doReturn(
                List.of(new SeatWithBookingReference("A", 1, new BookingReferenceId("")),
                        new SeatWithBookingReference("A", 2, new BookingReferenceId("")),
                        new SeatWithBookingReference("A", 3, new BookingReferenceId("")),
                        new SeatWithBookingReference("A", 4, new BookingReferenceId("")))
        )
                .when(trainDataProvider).getSeats(any(TrainId.class));

        TicketOffice ticketOffice = new TicketOffice(bookingReferenceProvider, trainDataProvider);

        TrainId trainId = new TrainId("express_2000");
        ReservationRequest reservationRequest = new ReservationRequest(trainId, 3);
        Reservation reservation = ticketOffice.makeReservation(reservationRequest);

        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(expectedBookingId);
        assertThat(reservation.seats)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new Seat("A", 1),
                        new Seat("A", 2),
                        new Seat("A", 3));
    }

    @Test
    void shouldMarkSeatsAsReserved_whenReserved() {
        // GIVEN
        BookingReferenceId expectedBookingId = new BookingReferenceId("75bcd15");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();

        TrainId trainId = new TrainId("express2000");
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        doReturn(List.of(
                new SeatWithBookingReference("A", 1, new BookingReferenceId("34Dsq")),
                new SeatWithBookingReference("A", 2, BookingReferenceId.NULL),
                new SeatWithBookingReference("A", 3, new BookingReferenceId("34Dsq"))
        ))
                .when(trainDataProvider).getSeats(trainId);

        // WHEN
        TicketOffice ticketOffice = new TicketOffice(bookingReferenceProvider, trainDataProvider);
        Reservation reservation = ticketOffice.makeReservation(new ReservationRequest(trainId, 1));

        // THEN le siège réservé est bien le "A2"
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(expectedBookingId);
        assertThat(reservation.seats).containsExactly(new Seat("A", 2));

        // AND on a appelé la méthode de réservation de l'opérateur historique
        verify(trainDataProvider).reserveSeats(trainId,List.of(new Seat("A", 2)),expectedBookingId);
    }

    private interface IProvideBookingReferences {
        BookingReferenceId getBookingReferenceId();
    }

    public static class TicketOffice {

        private final IProvideBookingReferences bookingReferenceProvider;
        private final IProvideTrainData trainDataProvider;

        public TicketOffice(IProvideBookingReferences bookingReferenceProvider, IProvideTrainData trainDataProvider) {
            this.bookingReferenceProvider = bookingReferenceProvider;
            this.trainDataProvider = trainDataProvider;
        }

        public Reservation makeReservation(ReservationRequest request) {
            List<Seat> reservedSeats = trainDataProvider.getSeats(request.trainId).stream()
                    .filter(SeatWithBookingReference::isAvailable)
                    .limit(request.seatCount)
                    .map(Seat::new)
                    .collect(Collectors.toList());
            if (reservedSeats.size() >= request.seatCount) {
                BookingReferenceId bookingReferenceId = bookingReferenceProvider.getBookingReferenceId();
                trainDataProvider.reserveSeats(request.trainId, reservedSeats, bookingReferenceId);
                return new Reservation(request.trainId, reservedSeats, bookingReferenceId);
            } else {
                return new Reservation(request.trainId, Collections.emptyList(), new BookingReferenceId(""));
            }
        }
    }

    private interface IProvideTrainData {
        Collection<SeatWithBookingReference> getSeats(TrainId trainId);

        void reserveSeats(TrainId trainId, Collection<Seat> seats, BookingReferenceId bookingReferenceid);
    }

}