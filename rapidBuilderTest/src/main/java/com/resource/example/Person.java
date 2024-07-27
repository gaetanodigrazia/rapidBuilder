package com.resource.example;


import com.rapidBuilder.annotation.field.FieldBuilderProperty;

public class Person {
    private int age;
    private String name;
    private Puppy puppy;
    private String builderFalseRandomizeTrue;

    public int getAge() {
        return age;
    }

    @FieldBuilderProperty(builder = true, randomize = true)
    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    @FieldBuilderProperty(builder = true)
    public void setName(String name) {
        this.name = name;
    }

    public Puppy getPuppy() {
        return puppy;
    }

    @FieldBuilderProperty(builder = true, randomize = true)
    public void setPuppy(Puppy puppy) {
        this.puppy = puppy;
    }

    public String getBuilderFalseRandomizeTrue() {
        return builderFalseRandomizeTrue;
    }
    @FieldBuilderProperty(randomize = true)
    public void setBuilderFalseRandomizeTrue(String builderFalseRandomizeTrue) {
        this.builderFalseRandomizeTrue = builderFalseRandomizeTrue;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", animal=" + puppy +
                ", builderFalseRandomizeTrue='" + builderFalseRandomizeTrue + '\'' +
                '}';
    }
}
