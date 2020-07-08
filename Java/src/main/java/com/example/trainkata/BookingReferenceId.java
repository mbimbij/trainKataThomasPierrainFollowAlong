package com.example.trainkata;

import org.apache.commons.lang3.StringUtils;

public class BookingReferenceId extends ValueType<String> {

    public BookingReferenceId(String value) {
        super(value);
    }

    public boolean isNull() {
        return StringUtils.isBlank(value);
    }

    public static class Null extends BookingReferenceId {
        public Null() {
            super(null);
        }

        @Override
        public boolean isNull() {
            return true;
        }

        public static Null newInstance() {
            return new Null();
        }
    }
}
