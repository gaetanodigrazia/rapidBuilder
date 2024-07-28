import com.resource.example.Male;
import com.resource.example.MaleBuilder;
import com.resource.example.Puppy;
import com.resource.example.PuppyBuilder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MaleBuilderTest {

    private static final Logger log = LoggerFactory.getLogger(MaleBuilderTest.class);

    @Test
    @DisplayName("buildingTest")
    public void buildingTest() {
        Puppy puppy = new PuppyBuilder().build();
        puppy.setFirst(13);
        puppy.setSecond("Second");

        Male male = new MaleBuilder().build();
        assertNotNull(male);

        male.setAge(11);
        male.setName("name");
        male.setPuppy(puppy);
        male.setGender("male");

        assertNotNull(male);
        assertEquals(11, male.getAge());
        assertEquals("name", male.getName());
        assertEquals(puppy, male.getPuppy());
        assertEquals("male", male.getGender());
    }

}
