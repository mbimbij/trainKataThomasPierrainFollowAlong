package com.example.trainkata;

import java.util.Objects;

abstract class ValueType<T>{
    protected final T value;

    public ValueType(T value) {
        this.value = value;
    }

    public T getValue(){
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueType<?> valueType = (ValueType<?>) o;
        return Objects.equals(value, valueType.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
