import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.BestMatcher;
import util.Requestable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by steve on 08/11/2016.
 */
class BestMatcherTest {

    private BestMatcher collection;

    @BeforeEach
    void init(){
        collection = new BestMatcher("test");
    }

    @AfterEach
    void end(){
        collection = null;
    }

    @Test
    void testEmptySize(){
        assertTrue(collection.isEmpty());
        assertTrue(collection.getBests().isEmpty());
        assertNull(collection.getBest());
        assertFalse(collection.isUnique());
    }

    @Test
    void testOneElement(){
        Requestable requestable = new Requestable("test", "http://test.tst");
        collection.evaluate(requestable);
        assertEquals(collection.getBests().size(), 1);
        assertSame(collection.getBest(), requestable);
        assertTrue(collection.isUnique());
        assertFalse(collection.isEmpty());
    }

    @Test
    void testDifferentElements(){
        Requestable foo = new Requestable("foo", "http://foo.tst");
        Requestable requestable = new Requestable("test", "http://test.tst");
        Requestable bar = new Requestable("bar", "http://bar.tst");

        collection.evaluate(foo);
        collection.evaluate(requestable);
        collection.evaluate(bar);

        assertEquals(collection.getBests().size(), 1);
        assertSame(collection.getBest(), requestable);
        assertTrue(collection.isUnique());
        assertFalse(collection.isEmpty());
    }

    @Test
    void testSimilarElements(){
        Requestable test1 = new Requestable("test1", "http://test1.tst");
        Requestable test2 = new Requestable("test2", "http://test2.tst");
        Requestable test3 = new Requestable("test3", "http://test3.tst");

        collection.evaluate(test1);
        collection.evaluate(test2);
        collection.evaluate(test3);

        assertSame(collection.getBests().size(), 3);
        assertNull(collection.getBest());
        assertFalse(collection.isUnique());
        assertFalse(collection.isEmpty());
    }

    @Test
    void testSimilarElementsWithExactOne(){
        Requestable test1 = new Requestable("test1", "http://test1.tst");
        Requestable test = new Requestable("test", "http://test.tst");
        Requestable test3 = new Requestable("test3", "http://test3.tst");

        collection.evaluate(test1);
        collection.evaluate(test);
        collection.evaluate(test3);

        assertEquals(collection.getBests().size(), 1);
        assertSame(collection.getBest(), test);
        assertTrue(collection.isUnique());
        assertFalse(collection.isEmpty());
    }

    @Test
    void testSimilarElementsWithBadOne(){
        Requestable test1 = new Requestable("test1", "http://test1.tst");
        Requestable bad = new Requestable("bad", "http://bad.tst");
        Requestable test3 = new Requestable("test3", "http://test3.tst");

        collection.evaluate(test1);
        collection.evaluate(bad);
        collection.evaluate(test3);

        assertEquals(collection.getBests().size(), 2);
        assertNull(collection.getBest());
        assertFalse(collection.isUnique());
        assertFalse(collection.isEmpty());
    }

    @Test
    void testEvaluateAllEmpty(){
        collection.evaluateAll(new ArrayList<>());

        assertEquals(collection.getBests().size(), 0);
        assertNull(collection.getBest());
        assertFalse(collection.isUnique());
        assertTrue(collection.isEmpty());
    }

    @Test
    void testEvaluateAll(){
        collection.evaluateAll(
                new Requestable("test1", "http://test1.tst"),
                new Requestable("tést2", "http://test2.tst"),
                new Requestable("têst tÉst3", "http://test3.tst"),
                new Requestable("TEST4", "http://test4.tst")
        );

        assertEquals(collection.getBests().size(), 4);
        assertNull(collection.getBest());
        assertFalse(collection.isUnique());
        assertFalse(collection.isEmpty());
    }
}