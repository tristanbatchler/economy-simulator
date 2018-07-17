package economy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for the {@link Item} class.
 */
public class ItemTest {
    @Test
    public void randomNameConstructorTest() {
        Item item = new Item();
        assertNotNull(item.getName());
    }

    @Test
    public void chosenNameConstructorTest() {
        Item item = new Item("Item");
        assertEquals("Item", item.getName());
    }

    @Test
    public void toStringTest() {
        Item item = new Item("Item");
        assertEquals("\"Item\"", item.toString());
    }

    @Test
    public void toStringOfItemWithEmptyName() {
        Item item = new Item("");
        assertEquals("\"\"", item.toString());
    }

    @Test
    public void compareFromEmptyName() {
        Item item = new Item("");
        Item other = new Item("Other");
        assertTrue(item.compareTo(other) < 0);
    }

    @Test
    public void compareToEmptyName() {
        Item item = new Item("Item");
        Item other = new Item("");
        assertTrue(item.compareTo(other) > 0);
    }

    @Test
    public void compareToSameName() {
        Item item = new Item("Item");
        Item other = new Item("Item");
        assertEquals(0, item.compareTo(other));
    }

    @Test
    public void itemEqualsNullTest() {
        Item item = new Item();
        assertFalse(item.equals(null));
    }

    @Test
    public void itemEqualsDifferentOtherTest() {
        Item item = new Item("Item");
        Item other = new Item("Other");
        assertFalse(item.equals(other));
        assertFalse(other.equals(item));
    }

    @Test
    public void equalsIsReflexive() {
        Item item = new Item();
        assertTrue(item.equals(item));
    }

    @Test
    public void equalsIsSymmetric() {
        Item item = new Item("Item");
        Item other = new Item("Item");
        assertTrue(item.equals(other));
        assertTrue(other.equals(item));
    }

    @Test
    public void hashCodeSameOnEqualObjects() {
        Item item = new Item("Item");
        Item other = new Item("Item");
        assertEquals(item.hashCode(), other.hashCode());
    }
}
