package economy;

import common.Utils;

/**
 * A person or a vendor who is offering an exchange of goods or money in a market.
 *
 * @author Tristan Batchler
 * @see Market
 */
public class Agent implements Comparable<Agent> {
    /**
     * The name of the current agent.
     */
    public String name;
    /**
     * The current agent's inventory.
     */
    public Inventory inventory;
    private double money;
    Market market;

    /**
     * Creates a new agent with a random name, $0.00 and no items belonging to no market.
     */
    public Agent() {
        name = Utils.getRandomLineInFile("src/main/resources/names");
        inventory = new Inventory();
        money = 0L;
        this.market = null;
    }

    /**
     * Creates a new agent with a given name, $0.00 and no items belonging to no market.
     * @param name The name to give the newly created agent.
     */
    public Agent(String name) {
        this();
        this.name = name;
    }

    /**
     * Creates a new agent with a random name, $0.00 and no items belonging to a given market.
     *
     * Adds this agent to the given market's list of agents.
     * @param market The market to add this agent to.
     */
    public Agent(Market market) {
        this();
        market.addAgent(this);
    }

    /**
     * Creates a new agent with a given name, $0.00 and no items belonging to a given market.
     *
     * Adds this agent to the given market's list of agents.
     * @param name The name to give the newly created agent.
     * @param market The market to add this agent to.
     */
    public Agent(String name, Market market) {
        this(name);
        this.market = market;
        market.addAgent(this);
    }

    /**
     * Returns the current agent's amount of money.
     * @return The current agent's amount of money.
     */
    public double getMoney() {
        return money;
    }

    /**
     * Adds quantity to an item in the current agent's inventory. If the current agent belongs to a market, the market
     * responds to the change in supply. See {@link Market#addItem} for more information.
     *
     * Does nothing if the given quantity is non-positive.
     * @param item The item to add quantity to.
     * @param quantity The quantity to add.
     */
    public void receive(Item item, long quantity) {
        if (quantity <= 0) {
            return;
        }

        if (market != null) {
            market.addItem(item, quantity);
        }

        inventory.add(item, quantity);
    }

    /**
     * Adds a given amount of money to the current agent's money pool. If the given amount is not positive, nothing happens.
     * @param amount The amount of money for the current agent to receive.
     */
    public void receive(double amount) {
        if (amount > 0) {
            money += amount;
        }
    }

    /**
     * Removes quantity from an item in the current agent's inventory. If the current agent belongs to a market, the
     * market responds to the change in supply. See {@link Market#removeItem} for more information.
     *
     * Does nothing if the given quantity is non-positive.
     * @param item The item to remove quantity from.
     * @param quantity The quantity to remove. Must be positive.
     */
    public void lose(Item item, long quantity) {
        if (quantity <= 0) {
            return;
        }

        inventory.remove(item, quantity);

        if (market != null) {
            market.removeItem(item, quantity);
        }
    }

    /**
     * Removes a given amount of money from the current agent's money pool. If the given amount is not positive, nothing
     * happens.
     * @param amount The amount of money for the current agent to lose.
     */
    public void lose(double amount) {
        if (amount > 0) {
            money -= amount;
        }
    }

    /**
     * Returns a string representation of the current agent which is of the form:
     * <blockquote>"A" with $B and C.</blockquote>
     * where A is the current agent's name, B is the current agent's amount of money, and C is the current agent's
     * inventory's string representation.
     */
    @Override
    public String toString() {
        return String.format("\"%s\" with $%.2f and " + inventory, name, money);
    }

    /**
     * Compares the current agent to another agent using the amount of money held by each as the comparator.
     * @return A positive integer if this agent is greater than the other agent, or a negative integer if this agent is
     *         less than the other agent.
     */
    public int compareTo(Agent other) {
        return Double.compare(this.money, other.money);
    }

    /**
     * Returns true if and only if the current agent is "equal" to the given object. That is,
     * <ul>
     *     <li>The given object is of type {@link Agent},</li>
     *     <li>The given other agent has a name equal to the current agent's name,</li>
     *     <li>The given other agent's inventory is equal to the current agent's inventory,</li>
     *     <li>The given other agent has money of equal amount to the current agent's money.</li>
     * </ul>
     *
     * @param o The other object to test equality against the current agent.
     * @return True if this agent is equal to the other object; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        if (Double.compare(agent.money, money) != 0) return false;
        if (name != null ? !name.equals(agent.name) : agent.name != null) return false;
        if (inventory != null ? !inventory.equals(agent.inventory) : agent.inventory != null) return false;
        return market != null ? market.equals(agent.market) : agent.market == null;
    }


    /**
     * Returns a hashcode for the current agent using the current agent's name, inventory, and amount of money.
     * @return A hashcode for the current agent using the current agent's name, inventory, and amount of money.
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (inventory != null ? inventory.hashCode() : 0);
        temp = Double.doubleToLongBits(money);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (market != null ? market.hashCode() : 0);
        return result;
    }
}
