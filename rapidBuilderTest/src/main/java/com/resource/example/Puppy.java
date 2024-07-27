package com.resource.example;


import com.rapidBuilder.annotation.field.FieldBuilderProperty;

public class Puppy {


    private int first;
    private String second;

    public int getFirst() {
        return first;
    }

    @FieldBuilderProperty(builder = true, randomize = true)
    public void setFirst(int first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    @FieldBuilderProperty(builder = true, randomize = true)
    public void setSecond(String second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "first=" + first +
                ", second='" + second + '\'' +
                '}';
    }
}
