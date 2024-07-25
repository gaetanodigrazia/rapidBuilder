# rapidBuilder

rapidBuilder is a library that aim to simplify the building process of objects for developers.
It generates the builder or the randomizer or even both from an annotations, instead of other library it generate
the source, so you can inspect them.


## Usage
In order to use this library you have to download the project, open it in IntelliJ and perform a clean install.

After that you have to import it as follow in you pom.xml

        <dependency>
            <groupId>com.rapidBuilder</groupId>
            <artifactId>rapidbuilder</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

After this in the class you want to generate the builder you have to annotate the setter method with the library
annotation in this way

```
public class Sample {
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
```

and the generated class will be

```
public class SampleBuilder {

private Sample object = new Sample();
private Faker faker = new Faker();

    public Sample build() {
        return object;
    }

    public SampleBuilder setSecond(java.lang.String value) {
        object.setSecond(value);
        return this;
    }
    public SampleBuilder setFirst(int value) {
        object.setFirst(value);
        return this;
    }

    public SampleBuilder fromObject(Sample object) {
        SampleBuilder builder = new SampleBuilder();
        builder.setSecond(object.getSecond());
        builder.setFirst(object.getFirst());
    return builder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleBuilder builder = (SampleBuilder) o;
        return Objects.equals(object, builder.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }

    @Override
    public String toString() {
    return "Builder{" +
        "object=" + object +
        '}';
    }
}

```
And the randomizer

```

public class SampleBuilderRandom {

private SampleBuilder object = new SampleBuilder();
private Faker faker = new Faker();

    public SampleBuilder buildNullModel() {
        object.setSecond(null);
        object.setFirst(0);
    return object;
    }

    public SampleBuilder randomize() {
        object.setSecond(faker.lorem().characters(faker.random().nextInt(5, 15)));
        object.setFirst(faker.random().nextInt(5, 15));
    return object;
    }
}
```
