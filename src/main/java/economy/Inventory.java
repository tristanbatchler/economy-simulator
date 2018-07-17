package economy;

import java.util.*;

/**
 * A data structure that represents a collection of items with varying quantities.
 *
 * @author Tristan Batchler
 */
public class Inventory implements Iterable<Map.Entry<Item, Long>> {
    private Map<Item, Long> data;

    /**
     * Creates a new empty inventory.
     */
    public Inventory() {
        data = new HashMap<>();
    }

    /**
     * Adds a given item of a given quantity to the current inventory.
     *
     * If the given quantity is not positive, nothing happens.
     * @param item The item to add to the current inventory.
     * @param quantity The quantity to add to the current inventory.
     */
    public void add(Item item, long quantity) {
        if (quantity <= 0) {
            return;
        }
        long currentQuantity = getQuantity(item);
        data.put(item, currentQuantity + quantity);
    }

    /**
     * Removes a given item of a given quantity from the current inventory.
     *
     * If the given quantity is not positive, nothing happens.
     *
     * If the given quantity to remove is greater than the current quantity of the item existing in the
     * current inventory, then the item entry is removed from the inventory altogether (rather than the current inventory
     * reporting a negative quantity of the item).
     * @param item The item to remove from the current inventory.
     * @param quantity The quantity to remove from the current inventory.
     */
    public void remove(Item item, long quantity) {
        if (quantity <= 0) {
            return;
        }

        long newQuantity = getQuantity(item) - quantity;
        if (newQuantity <= 0) {
            data.remove(item);
        } else {
            data.replace(item, newQuantity);
        }
    }

    /**
     * Returns true if and only if the current item has a positive quantity in the current  inventory.
     * @param item The item to check.
     * @return true if the current item has a positive quantity in the current  inventory; false otherwise.
     */
    public boolean contains(Item item) {
        return data.containsKey(item);
    }

    /**
     * Returns the quantity of a given item existing in the current inventory. Returns 0 if the given item does not exist
     * in the current inventory.
     * @param item The item to get the quantity of.
     * @return The quantity of a given item existing in the current inventory, or 0 if the given item does not exist
     * in the current inventory.
     */
    public long getQuantity(Item item) {
        return this.contains(item) ? data.get(item) : 0;
    }

    /**
     * Returns the number of unique items in the current inventory.
     * @return The number of unique items in the current inventory.
     */
    public long size() {
        return data.size();
    }

    /**
     * Returns true if and only if there are no items in the current inventory.
     * @return True if there are no items in the current inventory; false otherwise.
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Returns a randomly selected item from the current inventory, or null if the current inventory is empty.
     * @return A randomly selected item from the current inventory, or null if the current inventory is empty.
     */
    public Item getRandomItem() {
        if (this.isEmpty()) {
            return null;
        }
        int index = (int)(Math.random() * data.size());
        List<Item> items = getItems();
        return items.get(index);
    }

    /**
     * Returns an unmodifiable view of the list of items in the current inventory.
     * @return An unmodifiable view of the list of items in the current inventory.
     */
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (Map.Entry<Item, Long> entry : data.entrySet()) {
            Item item = entry.getKey();
            items.add(item);
        }
        return Collections.unmodifiableList(items);
    }

    /**
     * Adds another inventory to this one. That is, this inventory receives each item from the other inventory. This
     * does not change the other inventory.
     * @param other The other inventory.
     */
    public void add(Inventory other) {
        for (Map.Entry<Item, Long> entry : other) {
            Item item = entry.getKey();
            long qty = entry.getValue();
            this.add(item, qty);
        }
    }

    /**
     * Returns an iterator over the entries of type {@link Map.Entry} representing items and quantities in
     * the current inventory.
     * @return an iterator over the entries of type {@link Map.Entry} representing items and quantities in
     * the current inventory.
     */
    @Override
    public Iterator<Map.Entry<Item, Long>> iterator() {
        return data.entrySet().iterator();
    }

    /**
     * Returns a string representation of the current inventory of the form:
     * <blockquote>
     *     QTY1x"ITEM1", QTY2x"ITEM2", QTY3x"ITEM3", ..., QTYnx"ITEMn"
     * </blockquote>
     * where QTYk is the quantity of ITEMk which is the kth item in the current inventory.
     *
     * Returns "no items" if the current inventory is empty.
     * @return A string representation of the current inventory.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "no items";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Item, Long> entry : this) {
            Item item = entry.getKey();
            long qty = entry.getValue();
            sb.append(String.format("%dx%s, ", qty, item));
        }

        // Remove the last ", "
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    /**
     * Returns true if and only if the current inventory is "equal" to the given object. That is,
     * <ul>
     *     <li>The given object is of type {@link Inventory},</li>
     *     <li>The given other inventory has items equal to the current inventory's items.</li>
     * </ul>
     *
     * @param o The other object to test equality against this inventory.
     * @return True if this inventory is equal to the other object; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Inventory)) {
        return false;
      }

      Inventory other = (Inventory)o;
      return this.data.equals(other.data);
    }

    /**
     * Returns a hashcode for the current inventory using the current inventory's items.
     * @return A hashcode for the current inventory using the current inventory's items.
     */
    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}
