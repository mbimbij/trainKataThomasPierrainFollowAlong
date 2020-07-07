package com.example.trainkata;

public class TrainId extends ValueType<String>{
    public TrainId(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "TrainId{" +
                "value=" + value +
                '}';
    }
}
