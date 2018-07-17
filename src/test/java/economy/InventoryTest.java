package economy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit tests for the {@link Inventory} class.
 */
public class InventoryTest {
    Item[] items = {new Item("Item 0"), new Item("Item 1"), new Item("Item 2")};

    @Test
    public void constructorTest() {
        Inventory inventory = new Inventory();
        assertNotNull(inventory);
        assertTrue(inventory.isEmpty());
        assertEquals(0, inventory.size());
    }

    @Test
    public void addEmptyInventory() {
        Inventory inventory1 = new Inventory();
        inventory1.add(items[0], 10);
        inventory1.add(items[2], 5);

        Inventory inventory2 = new Inventory();

        inventory1.add(inventory2);
        assertEquals(10, inventory1.getQuantity(items[0]));
        assertEquals(0, inventory1.getQuantity(items[1]));
        assertEquals(5, inventory1.getQuantity(items[2]));

        assertTrue(inventory2.isEmpty());
    }

    @Test
    public void addInventory() {
        Inventory inventory1 = new Inventory();
        inventory1.add(items[0], 10);
        inventory1.add(items[2], 5);

        Inventory inventory2 = new Inventory();
        inventory2.add(items[0], 1);
        inventory2.add(items[1], 4);

        inventory1.add(inventory2);
        assertEquals(11, inventory1.getQuantity(items[0]));
        assertEquals(4, inventory1.getQuantity(items[1]));
        assertEquals(5, inventory1.getQuantity(items[2]));

        assertEquals(1, inventory2.getQuantity(items[0]));
        assertEquals(4, inventory2.getQuantity(items[1]));
    }

    @Test
    public void removeQuantityMoreThanCurrent() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.remove(items[0], 20);
        assertEquals(0, inventory.getQuantity(items[0]));
        assertTrue(inventory.isEmpty());
    }

    @Test
    public void removeNegativeQuantity() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.remove(items[0], -1);
        assertEquals(10, inventory.getQuantity(items[0]));
    }

    @Test
    public void removeOnEmptyInventory() {
        Inventory inventory = new Inventory();
        inventory.remove(items[0], 10);
        assertEquals(0, inventory.getQuantity(items[0]));
        assertTrue(inventory.isEmpty());
    }

    @Test
    public void emptyInventoryContainsItems() {
        Inventory inventory = new Inventory();
        assertFalse(inventory.contains(items[0]));
    }

    @Test
    public void inventoryContainsItemOnceRemoved() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        assertTrue(inventory.contains(items[0]));
        inventory.remove(items[0], 10);
        assertFalse(inventory.contains(items[0]));
    }

    @Test
    public void inventoryContainsItemOnceSmallQuantityRemoved() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.remove(items[0], 5);
        assertTrue(inventory.contains(items[0]));
        assertEquals(5, inventory.getQuantity(items[0]));
    }

    @Test
    public void emptyInventoryHasZeroSize() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.size());
    }

    @Test
    public void inventoryWithOneItemHasOneSize() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        assertEquals(1, inventory.size());
    }

    @Test
    public void getRandomItemInInventory() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.add(items[2], 30);
        for (int i = 0; i < 10000; i++) {
            Item randomItem = inventory.getRandomItem();
            assertTrue(randomItem.equals(items[0]) || randomItem.equals(items[2]));
        }
    }

    @Test
    public void getRandomItemFromEmptyInventory() {
        Inventory inventory = new Inventory();
        Item randomItem = inventory.getRandomItem();
        assertNull(randomItem);
    }

    @Test
    public void getItemsFromEmptyInventory() {
        Inventory inventory = new Inventory();
        List<Item> items = inventory.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    public void getItemsFromInventory() {
        Inventory inventory = new Inventory();

        inventory.add(items[0], 10);
        inventory.add(items[1], 50);

        List<Item> expected = new ArrayList<>();
        expected.add(items[0]);
        expected.add(items[1]);

        List<Item> actual = inventory.getItems();

        assertTrue(listsHaveSameElements(expected, actual));
    }

    @Test
    public void addItemToEmptyInventory() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        assertFalse(inventory.isEmpty());
        assertEquals(1, inventory.size());
        assertEquals(10, inventory.getQuantity(items[0]));
    }

    @Test
    public void addItemToNonEmptyInventory() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.add(items[1], 20);
        assertFalse(inventory.isEmpty());
        assertEquals(2, inventory.size());
        assertEquals(10, inventory.getQuantity(items[0]));
        assertEquals(20, inventory.getQuantity(items[1]));
    }

    @Test
    public void addQuantityToExistingItemInInventory() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.add(items[0], 15);
        assertFalse(inventory.isEmpty());
        assertEquals(1, inventory.size());
        assertEquals(25, inventory.getQuantity(items[0]));
    }

    @Test
    public void addZeroQuantity() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 0);
        assertTrue(inventory.isEmpty());
        assertEquals(0, inventory.getQuantity(items[0]));
    }

    @Test
    public void addNegativeQuantity() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], -1);
        assertTrue(inventory.isEmpty());
        assertEquals(0, inventory.getQuantity(items[0]));
    }

    @Test
    public void iteratorTest() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.add(items[1], 20);
        inventory.add(items[2], 5);

        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(items[0]);
        expectedItems.add(items[1]);
        expectedItems.add(items[2]);
        long expectedSize = 3;
        long expectedQuantity = 35;

        List<Item> actualItems = new ArrayList<>();
        long actualSize = 0;
        long actualQuantity = 0;

        for (Map.Entry<Item, Long> entry : inventory) {
            Item item = entry.getKey();
            long qty = entry.getValue();
            actualItems.add(item);
            actualQuantity += qty;
            actualSize++;
        }

        assertTrue(listsHaveSameElements(expectedItems, actualItems));
        assertEquals(expectedSize, actualSize);
        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    public void emptyInventoryToString() {
        Inventory inventory = new Inventory();
        assertEquals("no items", inventory.toString());
    }

    @Test
    public void nonEmptyInventoryToString() {
        Inventory inventory = new Inventory();
        inventory.add(items[0], 10);
        inventory.add(items[1], 20);
        inventory.add(items[2], 5);

        assertEquals("10x\"Item 0\", 5x\"Item 2\", 20x\"Item 1\"", inventory.toString());
    }

    @Test
    public void testInventoryEqualityIsReflexive() {
        Inventory inventory = new Inventory();
        inventory.add(items[2], 1);
        inventory.add(items[1], 0);
        assertTrue(inventory.equals(inventory));
    }

    @Test
    public void testInventoryEqualityIsSymmetric() {
        Inventory inventory1 = new Inventory();
        inventory1.add(items[2], 1);
        inventory1.add(items[1], 3);

        Inventory inventory2 = new Inventory();
        inventory2.add(items[2], 1);
        inventory2.add(items[1], 3);

        assertTrue(inventory1.equals(inventory2));
        assertTrue(inventory2.equals(inventory1));
    }

    @Test
    public void testInventoryEqualityAfterChanging() {
        Inventory inventory1 = new Inventory();
        inventory1.add(items[2], 1);
        inventory1.add(items[1], 3);

        Inventory inventory2 = new Inventory();
        inventory2.add(items[2], 1);
        inventory2.add(items[1], 3);
        inventory2.remove(items[2], 1);

        assertFalse(inventory1.equals(inventory2));
    }

    @Test
    public void testInventoryEqualityAfterRevertingChange() {
        Inventory inventory1 = new Inventory();
        inventory1.add(items[2], 1);
        inventory1.add(items[1], 3);

        Inventory inventory2 = new Inventory();
        inventory2.add(items[2], 1);
        inventory2.add(items[1], 3);
        inventory2.remove(items[2], 1);
        inventory2.add(items[2], 1);
        assertTrue(inventory1.equals(inventory2));
    }

    @Test
    public void testInventoryNotEqualToNull() {
        Inventory inventory1 = new Inventory();
        assertFalse(inventory1.equals(null));
    }

    @Test
    public void testEqualInventoriesHaveEqualHashCodes() {
        Inventory inventory1 = new Inventory();
        inventory1.add(items[2], 1);
        inventory1.add(items[1], 3);

        Inventory inventory2 = new Inventory();
        inventory2.add(items[1], 2);
        inventory2.add(items[2], 1);
        inventory2.add(items[1], 1);

        assertTrue(inventory1.equals(inventory2));
        assertEquals(inventory1.hashCode(), inventory2.hashCode());
    }

    private static boolean listsHaveSameElements(List listA, List listB) {
        return listA.containsAll(listB) && listB.containsAll(listA);
    }

}
