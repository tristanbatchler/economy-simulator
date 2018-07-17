package economy;

import common.Utils;

/**
 * An immutable item which has a name.
 *
 * @author Tristan Batchler
 */
public final class Item implements Comparable<Item> {
    private final String name;

    /**
     * Creates a new item and assigns a given name to it.
     * @param name The name to assign to the newly created item.
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * Creates a new item and assigns a random name to it.
     */
    public Item() {
        name = Utils.getRandomLineInFile("src/main/resources/items");
    }

    /**
     * Returns the name of this item.
     * @return the name of this item.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of this item of the form:
     * <blockquote>
     *     "NAME"
     * </blockquote>
     * where NAME is the name of this item.
     * @return A string representation of this item.
     */
    @Override
    public String toString() {
        return String.format("\"%s\"", name);
    }

    /**
     * Compares this item to another item using the names of each item.
     * @return A positive integer if this item is greater than the other item, or a negative integer if this item is less
     *         than the other item.
     */
    public int compareTo(Item other) {
        return name.compareTo(other.name);
    }

    /**
     * Tests this item for equality with another object. Returns true if and only if this item is equal to the other
     * object. That is, if the other object is of type {@link Item} and this item's name is equal to the other item's name.
     * @param o The other object to test equality against the current item.
     * @return True if this item is equal to the other object; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return name != null ? name.equals(item.name) : item.name == null;
    }

    /**
     * Returns a hashcode for this item using this item's name.
     * @return A hashcode for this item using this item's name.
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
