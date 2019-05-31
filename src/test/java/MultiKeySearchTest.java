import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.MultiKeySearch;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by steve on 08/11/2016.
 */
class MultiKeySearchTest {

    private static final int NUMBER_FIELD = 4;

    private MultiKeySearch<String> collection;

    @BeforeEach
    void init() {
        collection = new MultiKeySearch<>(NUMBER_FIELD);
    }

    @AfterEach
    void end() {
        collection = null;
    }

    @Test
    void testEmptySize() {
        assertTrue(collection.isEmpty());
        assertSame(collection.size(), 0);
    }

    @Test
    void testIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> collection.containsKeys("test"), String.valueOf(NUMBER_FIELD));
        assertThrows(IllegalArgumentException.class, () -> collection.add("test"), String.valueOf(NUMBER_FIELD));
        assertThrows(IllegalArgumentException.class, () -> collection.remove("test"), String.valueOf(NUMBER_FIELD));
        assertThrows(IllegalArgumentException.class, () -> collection.get("test"), String.valueOf(NUMBER_FIELD));
    }

    @Test
    void testAddElement() {
        collection.add("test", "key1", "key2", "key3", "key4");
        assertFalse(collection.isEmpty());
        assertEquals(collection.size(), 1);
        assertTrue(collection.containsKeys("key1", "key2", "key3", "key4"));
        assertFalse(collection.containsKeys("key2", "key1", "key4", "key3"));
    }

    @Test
    void testRemoveElement() {
        collection.add("test", "key1", "key2", "key3", "key4");
        collection.remove("key1", "key2", "key3", "key4");
        assertTrue(collection.isEmpty());
        assertEquals(collection.size(), 0);
        assertFalse(collection.containsKeys("key1", "key2", "key3", "key4"));
    }

    @Test
    void testGetEmptyElement() {
        List<String> result = collection.get("key1", "key2", "key3", "key4");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetOneElement() {
        String value = "test";
        collection.add(value, "key1", "key2", "key3", "key4");
        List<String> result = collection.get("key1", "key2", "key3", "key4");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertTrue(result.contains(value));
    }

    @Test
    void testPartialKey() {
        String value = "test";
        collection.add(value, "key1", "key2", "key3", "key4");
        List<String> result = collection.get(null, "key2", null, "key4");
        assertEquals(result.size(), 1);
        assertTrue(result.contains(value));
    }

    @Test
    void testPartialKeyWithMultipleEntries() {
        String value1 = "test1";
        String value2 = "test2";
        String value3 = "test3";
        collection.add(value1, "key1", "key2", "key3", "key4");
        collection.add(value2, "key5", "key6", "key7", "key8");
        collection.add(value3, "key1", "key9", "key7", "key10");

        List<String> result = collection.get("key1", "key2", "key3", "key4");
        assertEquals(result.size(), 1);
        assertTrue(result.contains(value1));

        result = collection.get(null, "key2", null, null);
        assertEquals(result.size(), 1);
        assertTrue(result.contains(value1));

        result = collection.get("key1", null, null, null);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(value1));
        assertTrue(result.contains(value3));

        result = collection.get("key1", "key6", null, null);
        assertTrue(result.isEmpty());

        result = collection.get("key1", null, "key7", null);
        assertEquals(result.size(), 1);
        assertTrue(result.contains(value3));

        result = collection.get(null, null, null, null);
        assertEquals(result.size(), 3);
        assertTrue(result.contains(value1));
        assertTrue(result.contains(value2));
        assertTrue(result.contains(value3));
    }
}