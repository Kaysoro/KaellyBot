import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.MultiKeySearch;
import static org.junit.Assert.assertTrue;

/**
 * Created by steve on 08/11/2016.
 */
public class MultiKeySearchTest {

    private static final int NUMBER_FIELD = 4;
    private MultiKeySearch collection;

    @Before
    public void init() {
        collection = new MultiKeySearch(NUMBER_FIELD);
    }

    @After
    public void end() {
        collection = null;
    }

    @Test
    public void testEmptySize() {
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testAddElement() {
        //TODO
    }

    @Test
    public void testRemoveElement() {
        //TODO
    }
}
