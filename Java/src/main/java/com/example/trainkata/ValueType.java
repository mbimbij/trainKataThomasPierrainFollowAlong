package com.example.trainkata;

class ValueType<T>{
    protected final T value;

    public ValueType(T value) {
        this.value = value;
    }

    public T getValue(){
        return value;
    }
}
