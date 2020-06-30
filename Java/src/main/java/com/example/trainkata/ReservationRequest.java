package com.example.trainkata;

public class ReservationRequest {
	public final TrainId trainId;
    public final int seatCount;

    public ReservationRequest(TrainId trainId, int seatCount) {
		this.trainId = trainId;
        this.seatCount = seatCount;
    }

}