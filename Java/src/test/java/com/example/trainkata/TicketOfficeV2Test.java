package com.example.trainkata;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketOfficeV2Test extends TicketOfficeBaseTest {

    private final double THRESHOLD = 0.7;

    @Test
    void shouldReserveSeats_whenUnreservedSeatsAreAvailable() {
        BookingReferenceId expectedBookingId = new BookingReferenceId("expectedBookingId");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);

        doReturn(Optional.of(trainWith1CoachAndNSeatsAvailable("A", 6))).when(trainDataProvider).getTrain(any(TrainId.class));
        TicketOffice ticketOffice = new TicketOfficeV2(bookingReferenceProvider, trainDataProvider, THRESHOLD);

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
        doReturn(Optional.of(trainWith1Coach3SeatsAnd1Available(trainId))).when(trainDataProvider).getTrain(any(TrainId.class));

        // WHEN
        TicketOffice ticketOffice = new TicketOfficeV2(bookingReferenceProvider, trainDataProvider, THRESHOLD);
        Reservation reservation = ticketOffice.makeReservation(new ReservationRequest(trainId, 1));

        // THEN le siège réservé est bien le "A2"
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(expectedBookingId);
        assertThat(reservation.seats).containsExactly(new Seat("A", 2));

        // AND on a appelé la méthode de réservation de l'opérateur historique
        verify(trainDataProvider).reserveSeats(trainId, List.of(new Seat("A", 2)), expectedBookingId);
    }

    @Test
    void shouldNotReserverMore70PercentOfSeatsForOverallTrain() {
        // GIVEN
        BookingReferenceId expectedBookingId = new BookingReferenceId("75bcd15");
        TrainId trainId = new TrainId("express2000");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        TicketOffice ticketOffice = new TicketOfficeV2(bookingReferenceProvider, trainDataProvider, THRESHOLD);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();
        doReturn(Optional.of(emptyTrainWith1CoachAndNSeatsAvailable(trainId, "A", 10)))
                .when(trainDataProvider).getTrain(trainId);

        // WHEN
        assertThatThrownBy(() -> ticketOffice.makeReservation(new ReservationRequest(trainId, 8))).isInstanceOf(MaxReservationThresholdException.class);
    }

    @Test
    void reservationShouldBeUnfulfilled_ifNbOfAvailableSeats_lessThanRequested() {
        // GIVEN
        BookingReferenceId expectedBookingId = new BookingReferenceId("75bcd15");
        TrainId trainId = new TrainId("express2000");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        TicketOffice ticketOffice = new TicketOfficeV2(bookingReferenceProvider, trainDataProvider, THRESHOLD);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();
        doReturn(Optional.of(trainWithXCapacityAndYAvailableSeats(trainId,expectedBookingId, "A", 10,3)))
                .when(trainDataProvider).getTrain(trainId);

        //WHEN
        Reservation reservation = ticketOffice.makeReservation(new ReservationRequest(trainId, 4));

        //THEN
        assertThat(reservation.bookingId).isEqualTo(BookingReferenceId.NULL);
        assertThat(reservation.seats).isEmpty();
    }

    private Train trainWith1CoachAndNSeatsAvailable(String trainId, int seatCount) {
        List<SeatWithBookingReference> seats = emptySeatsWith1CoachAndNSeatsAvailable(trainId, seatCount);
        return new Train(new TrainId(trainId), seats, THRESHOLD);
    }

    protected Train trainWith1Coach3SeatsAnd1Available(TrainId trainId) {
        return new Train(trainId, seatsWith1Coach3SeatsAnd1Available(), THRESHOLD);
    }

    protected Train emptyTrainWith1CoachAndNSeatsAvailable(TrainId trainId, String coach, int nbSeatsAvailable) {
        return new Train(trainId, emptySeatsWith1CoachAndNSeatsAvailable(coach, nbSeatsAvailable), THRESHOLD);
    }

    protected Train trainWithXCapacityAndYAvailableSeats(TrainId trainId, BookingReferenceId bookingReferenceId, String coach, int capacity, int availableSeats){
        List<SeatWithBookingReference> seats = IntStream.rangeClosed(1, capacity)
                .mapToObj(i -> new SeatWithBookingReference(coach, i, i <= availableSeats ? BookingReferenceId.NULL : bookingReferenceId))
                .collect(Collectors.toList());
        return new Train(trainId,seats, THRESHOLD);
    }
}