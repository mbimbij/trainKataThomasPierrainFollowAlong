package com.example.trainkata;

import org.apache.commons.lang3.StringUtils;

public class BookingReferenceId extends ValueType<String> {
    public static final BookingReferenceId NULL = new BookingReferenceIdNull();

    public BookingReferenceId(String value) {
        super(value);
    }

    public boolean isNull(){
        return StringUtils.isBlank(value);
    }

    public static class BookingReferenceIdNull extends BookingReferenceId {
        private BookingReferenceIdNull() {
            super(null);
        }

        @Override
        public boolean isNull(){
            return true;
        }
    }
}
