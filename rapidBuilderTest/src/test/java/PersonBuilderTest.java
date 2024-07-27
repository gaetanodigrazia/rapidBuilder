import com.resource.example.Person;
import com.resource.example.PersonBuilder;
import com.resource.example.PersonBuilderRandom;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class PersonBuilderTest {

    @Test
    @DisplayName("buildingTwoDifferentObject")
    public void buildingTwoDifferentObject() {
        Person person = new PersonBuilderRandom().randomize().build();
        Person anotherPerson = new PersonBuilderRandom().randomize().build();
        System.out.println(person.toString());
        System.out.println(anotherPerson.toString());

        assertNotEquals(person, anotherPerson);
    }

    @Test
    @DisplayName("buildingObjectDifferingFromOnlyOneField")
    public void buildingObjectDifferingFromOnlyOneField() {
        Person person = new PersonBuilderRandom().randomize().build();
        Person anotherPerson = new PersonBuilder().fromObject(person).setAge(11).build();

        System.out.println(person.toString());
        System.out.println(anotherPerson.toString());

        assertEquals(person.getName(), anotherPerson.getName());
        assertEquals(person.getPuppy(), anotherPerson.getPuppy());
        assertNotEquals(person.getAge(), anotherPerson.getAge());
    }

    @Test
    @DisplayName("buildingObjectDifferingFromOnlyOneField")
    public void buildingNestedObjectNotNull() {
        Person person = new PersonBuilderRandom().randomize().build();
        Person anotherPerson = new PersonBuilder()
                .fromObject(person).setAge(11).build();

        System.out.println(person.toString());
        System.out.println(anotherPerson.toString());

        assertEquals(person.getName(), anotherPerson.getName());
        assertNotNull(person.getPuppy());
        assertNotNull(anotherPerson.getPuppy());
        assertNotEquals(person.getAge(), anotherPerson.getAge());
    }

}
