package economy;

import java.util.*;
import common.Utils;

/**
 * A free market economy in which agents can exchange money and physical goods.
 *
 * @author Tristan Batchler
 * @see Agent
 */
public class Market {
    public String name = "Market";
    private List<Agent> agents;
    private Map<Item, Double> prices;
    public final double elasticity;

    /**
     * Creates a new market with a given elasticity with no agents and no items.
     *
     * The elasticity of a market is a non-negative value denoting how responsive the market is to changes in
     * supply. An elasticity of zero (0) means the market does not respond to changes in supply at all.
     * @param elasticity The elasticity of the market to create.
     */
    public Market(double elasticity) {
        agents = new ArrayList<>();
        prices = new HashMap<>();
        this.elasticity = elasticity;
        this.name = "Market";
    }

    /**
     * Creates a new market with a given name and elasticity with no agents and no items.
     *
     * The elasticity of a market is a non-negative value denoting how responsive the market is to changes in
     * supply. An elasticity of zero (0) means the market does not respond to changes in supply at all.
     * @param name The name of the market to create.
     * @param elasticity The elasticity of the market to create.
     */
    public Market(String name, double elasticity) {
        this(elasticity);
        this.name = name;
    }

    /**
     * Returns the quantity of a given item existing in the current market.
     * @param item The item to get the quantity of existing in the current market.
     * @return the quantity of the given item existing in the current market.
     */
    public long getSupply(Item item) {
        return getItems().getQuantity(item);
    }

    /**
     * Returns the current market price of a given item.
     * @param item The item to get the current market price of.
     * @return The current market price of the given item.
     * @throws IllegalStateException If the current market does not already have a price for the given item.
     */
    public double getPrice(Item item) throws IllegalStateException {
        Double price = prices.get(item);
        if (price == null) {
            throw new IllegalStateException("market does not have item " + item + " - cannot get price");
        }
        return price;
    }

    /**
     * Returns a copy of all items which exist in the current market.
     * @return A copy of all items which  exist in the current market.
     */
    public Inventory getItems() {
        Inventory items = new Inventory();
        for (Agent a : getAgents()) {
            items.add(a.inventory);
        }
        return items;
    }

    /**
     * Returns a copy of all items which exist in the current market.
     * @return A copy of all items which  exist in the current market.
     */
    public List<Item> getItemsList() {
        return getItems().getItems();
    }

    /**
     * Sets the price of a given item in the current market.
     * @param item The item to set the price of.
     * @param price The new price to assign to the given item.
     */
    public void setPrice(Item item, double price) {
        prices.put(item, price);
    }

    /**
     * Changes the price of an item in the current market by a given amount.
     * @param item The item to change the price of.
     * @param amount The amount to change the price of the item by.
     * @throws IllegalArgumentException If the item to change the price of does not already have a price in the current
     *                                  market.
     */
    public void changePrice(Item item, double amount) throws IllegalStateException {
        if (!prices.containsKey(item)) {
            throw new IllegalStateException("market does not have item " + item + " - cannot change price");
        }

        double newPrice = getPrice(item) + amount;
        setPrice(item, Math.max(0, newPrice));
    }

    /**
     * Generates a number of agents, all with random name, $0.00 and no items and adds them all to this market.
     *
     * If the number of agents to generative is non-positive, this method does nothing.
     * @param num The positive number of agents to generate.
     */
    public void generateAgents(long num) {
        if (num <= 0) {
            return;
        }

        for (int i = 0; i < num; i++) {
            addAgent(new Agent());
        }
    }

    /**
     * Generates a number of items types, all with random names, and disperses a random quantity of each item type
     * generated randomly among agents in the current market.
     *
     * This method is not guaranteed to generate the given number of <strong>unique</strong> items - especially if there
     * are not enough item names in the item names file. Instead, this method will randomly add quantities of random items
     * to agents' inventories a given number of <emph>times</emph>.
     *
     * If the number of items to generative is non-positive, this method does nothing.
     * @param num The positive number of item types to generate.
     * @param maxQuantity The upper bound (exclusive) on the random quantity of each item type to be dispersed.
     */
    public void generateItems(long num, long maxQuantity) {
        if (num <= 0) {
            return;
        }

        for (int i = 0; i < num; i++) {
            Item item = new Item();

            long quantity = Math.max(1, Utils.getRandomSize(maxQuantity));

            // Generate a random market price for this item for now.
            getRandomAgent().receive(item, quantity);
        }
    }

    /**
     * Generates an amount of money and disperses equally among agents in the current market.
     *
     * If the amount of money to generate is non-positive, this method does nothing.
     * @param amount The positive amount of money to generate and disperse.
     */
    public void generateMoney(double amount) {
        if (amount <= 0) {
            return;
        }

        int n = agents.size();
        for (int i = 0; i < n; i++) {
          agents.get(i).receive(amount / (double) n);
        }
    }

    /**
     * Adds a given item of a given quantity to the current market. The current market will then respond to the increase
     * in supply according to the following:
     * <ul>
     *     <li>If the item does not yet exist in the market, the market price of the item will be set to some random
     *     value in the range [0, 10000.00).</li>
     *
     *     <li>If the item already exists in the market, the market price of the item will decrease by a factor of the
     *     market's elasticity for every quantity of item received.</li>
     * </ul>
     * @param item The item to add to the current market.
     * @param quantity The quantity of the given item to add to the current market.
     */
    void addItem(Item item, long quantity) {
        double marketPrice = Utils.getRandomSize(10000.00);
        try {   // Supply goes up, so bring the market price down a bit.
            marketPrice = getPrice(item);
            changePrice(item, -marketPrice * this.elasticity * quantity);
        } catch (IllegalStateException e) { // If this item has no price in the market yet, agent sets price.
            // Just set the price randomly for now.
            setPrice(item, marketPrice);
        }
    }

    /**
     * Removes a given item of a given quantity from the current market. The current market will then respond to the
     * decrease in supply according to the following:
     * <ul>
     *     <li>The market price of the item will increase by a factor of the market's elasticity for every quantity of
     *     item removed.</li>
     * </ul>
     * @param item The item to remove from the current market.
     * @param quantity The quantity of the given item to remove from the current market.
     */
    void removeItem(Item item, long quantity) {
        // TODO: Fix this. Currently the market price drops more when an item leaves than it rises when it enters again.
        // Supply goes down, so bring price up a bit.
        double currentMarketPrice = getPrice(item);
        changePrice(item, currentMarketPrice * this.elasticity * quantity);

    }

    /**
     * Sets a given agent's market to the current market and, in turn, adds the given agent to this market's list of
     * agents.
     * The agent brings their inventory with them, so the supply of those items in the current market go up and
     * therefore the market price of those items come down by a factor of the elasticity and quantity of item.
     * @param agent The agent to add to the current market.
     */
    public void addAgent(Agent agent) {
        agent.market = this;
        agents.add(agent);
        for (Map.Entry<Item, Long> entry : agent.inventory) {
            Item item = entry.getKey();
            long qty = entry.getValue();
            this.addItem(item, qty);
        }
    }

    /**
     * For each agent "agent" passed in: sets "agent"'s market to the current market and, in turn, adds "agent" to this
     * market's list of agents.
     * The agents bring their inventories with them, so the supply of those items in the current market go up and
     * therefore the market price of those items come down by a factor of the elasticity and quantity of item.
     * @param agents The agents to add to the current market.
     */
    public void addAgents(Agent... agents) {
        for (Agent agent : agents) {
            addAgent(agent);
        }
    }

    /**
     * For each agent "agent" passed in: sets "agent"'s market to the current market and, in turn, adds "agent" to this
     * market's list of agents.
     * The agents bring their inventories with them, so the supply of those items in the current market go up and
     * therefore the market price of those items come down by a factor of the elasticity and quantity of item.
     * @param agents The agents to add to the current market.
     */
    public void addAgents(Collection<Agent> agents) {
        for (Agent agent : agents) {
            addAgent(agent);
        }
    }

    /**
     * Sets a given agent's market to null and, in turn, removes the given agent from this market's list of agents.
     * The agent takes their inventory with them, so the supply of those items in the current market go down and
     * therefore the market price of those items go up by a factor of the elasticity and quantity of item.
     * @param agent The agent to remove from the current market.
     */
    public void removeAgent(Agent agent) {
        agent.market = null;
        agents.remove(agent);

        for (Map.Entry<Item, Long> entry : agent.inventory) {
            Item item = entry.getKey();
            long qty = entry.getValue();
            this.removeItem(item, qty);
        }
    }

    /**
     * For each agent "agent" passed in: sets "agent"'s market to null and, in turn, removes "agent" from this
     * market's list of agents.
     * The agents take their inventories with them, so the supply of those items in the current market go down and
     * therefore the market price of those items go up by a factor of the elasticity and quantity of item.
     * @param agents The agents to remove from the current market.
     */
    public void removeAgents(Agent... agents) {
        for (Agent agent : agents) {
            removeAgent(agent);
        }
    }

    /**
     * For each agent "agent" passed in: sets "agent"'s market to null and, in turn, removes "agent" from this
     * market's list of agents.
     * The agents take their inventories with them, so the supply of those items in the current market go down and
     * therefore the market price of those items go up by a factor of the elasticity and quantity of item.
     * @param agents The agents to remove from the current market.
     */
    public void removeAgents(Collection<Agent> agents) {
        for (Agent agent : agents) {
            removeAgent(agent);
        }
    }

    /**
     * Processes a transaction between the current agent and a given "seller" agent wherein:
     * <ol>
     *     <li>The seller loses the given quantity of the given item and the buyer receives the given quantity of the
     *     given item. If the seller does not currently have enough quantity in their inventory, an InsufficientAmountException
     *     is thrown and nothing happens in this method.</li>
     *     <li>The seller receives an amount of money equal to the given buying price per each item multiplied by the
     *     quantity of item to buy. The current agent (the buyer) loses an amount of money equal to the given buying
     *     price per each item multiplied by the quantity of item to buy. If the buyer does not currently have enough money,
     *     an InsufficientAmountException is thrown and nothing happens in this method.</li>
     *     <li>The seller already had an "asking price" which is the item's current market price plus some small random
     *     percentage in the range [0%, 10%). The current market price of the item changes according to the following:
     *         <ul>
     *             <li>If the buying price was <strong>greater</strong> than the asking price, then the market price of
     *             the item increases by 5% of the difference for each quantity of item bought.</li>
     *             <li>If the buying price was <strong>lower</strong> than the asking price, then the market price of
     *             the item decreases by 5% of the difference for each quantity of item bought.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * If the given quantity is not positive, nothing happens.
     * @param buyer The buyer agent.
     * @param seller The seller agent.
     * @param item The item to buy.
     * @param quantity The quantity of the item to buy.
     * @param buyingPriceEach The price per each item to buy at.
     * @throws InsufficientAmountException If the current agent does not have enough money to buy the quantity of items
     *                                     at the buying price or if the seller agent does not have enough quantity to
     *                                     sell the requested quantity of items.
     * @throws IllegalArgumentException If the seller agent is not in the same market as the current (buyer) agent.
     */
    public void buy(Agent buyer, Agent seller, Item item, long quantity, double buyingPriceEach)
            throws InsufficientAmountException, IllegalArgumentException {

        if (quantity <= 0) {
            return;
        }

        if (!this.agents.contains(buyer) || !this.agents.contains(seller)) {
            throw new IllegalArgumentException("buyer and seller must be in the same market");
        }

        double finalPrice = buyingPriceEach * quantity;

        // Can't buy without enough money.
        if (buyer.getMoney() < finalPrice) {
            throw new InsufficientAmountException("not enough money to buy");
        }

        // Can't buy if seller doesn't have enough stock.
        if (seller.inventory.getQuantity(item) < quantity) {
            throw new InsufficientAmountException("not enough stock to sell");
        }

        double marketPrice = this.getPrice(item);

        // For now just make the seller's asking price just the market price plus some small random percentage.
        double askingPriceEach = marketPrice + Utils.getRandomSize(marketPrice * 0.10);

        // Exchange the item.
        seller.lose(item, quantity);
        buyer.receive(item, quantity);

        // Exchange the money.
        buyer.lose(finalPrice);
        seller.receive(finalPrice);

        // If difference > 0, the seller got the better deal. If difference < 0, the buyer got the better deal.
        double difference = buyingPriceEach * quantity - askingPriceEach * quantity;

        // The market price of the item will increase if the seller gets the better deal and decrease if the buyer got the better deal.
        this.changePrice(item, difference * this.elasticity * marketPrice);
    }

    /**
     * Returns a random agent in the current market.
     *
     * If the current market has no agents, returns null.
     * @return A random agent in the current market.
     */
    public Agent getRandomAgent() {
        if (agents.isEmpty()) {
            return null;
        }
        int index = (int)(Math.random() * agents.size());
        return agents.get(index);
    }

    /**
     * Returns an unmodifiable view of the list of agents in the current market.
     * @return an unmodifiable view of the list of agents in the current market.
     */
    public List<Agent> getAgents() {
        return Collections.unmodifiableList(agents);
    }

    /**
     * Gets an agent's net wealth in the context of the current market.
     * @param agent The agent to get the net wealth of.
     * @return the agent's net wealth in the context of the current market.
     */
    public double getAgentWealth(Agent agent) {
        double wealth = agent.getMoney();
        for (Map.Entry<Item, Long> entry : agent.inventory) {
            Item item = entry.getKey();
            long qty = entry.getValue();
            double marketPriceEach = this.getPrice(item);
            wealth += marketPriceEach * qty;
        }
        return wealth;
    }

    /**
     * Returns a string representation of the current market of the form:
     * <blockquote>
     *           ITEMS: {                                 <br>
     * &emsp;          ITEM1, ITEM2, ITEM3, ..., ITEMn    <br>
     *           }                                        <br>
     *           AGENTS: {                                <br>
     * &emsp;          AGENT1,                            <br>
     * &emsp;          AGENT2,                            <br>
     * &emsp;          AGENT3,                            <br>
     * &emsp;          ...,                               <br>
     * &emsp;          AGENTn                             <br>
     *           }
     * </blockquote>
     * where ITEMk denotes the string representation of the kth item in the list of items which exist in the current
     * market and AGENTk denotes the string representation of the kth agent in the list of agents which exist in the
     * current market.
     * @return The string representation of the current market.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ITEMS: {\n");
        for (Map.Entry<Item, Long> entry : getItems()) {
            Item item = entry.getKey();
            long qty = entry.getValue();
            sb.append(String.format("\t%dx%s@$%.2fea,\n", qty, item, this.getPrice(item)));
        }
        sb.append("}\nAGENTS: {\n");
        for (Agent a : agents) {
            sb.append(String.format("\t%s worth $%.2f with $%.2f and %s,\n", a.name, getAgentWealth(a), a.getMoney(), a.inventory));
        }
        sb.append("}");
        return sb.toString();
    }

/*    *//**
     * Returns true if and only if the current market is "equal" to the given object. That is,
     * <ul>
     *     <li>The given object is of type {@link Market},</li>
     *     <li>The given other market has agents equal to the current market's agents.</li>
     *     <li>The given other market has prices equal to the current market's prices.</li>
     * </ul>
     *
     * @param o The other object to test equality against this market.
     * @return True if this market is equal to the other object; false otherwise.
     *//*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Market market = (Market) o;

        if (agents != null ? !agents.equals(market.agents) : market.agents != null) {
            return false;
        }

        return prices != null ? prices.equals(market.prices) : market.prices == null;
    }

    *//**
     * Returns a hashcode for the current market using the current market's agents and prices.
     * @return A hashcode for the current market using the current market's agents and prices.
     *//*
    @Override
    public int hashCode() {
        int result = agents != null ? agents.hashCode() : 0;
        result = 31 * result + (prices != null ? prices.hashCode() : 0);
        return result;
    }*/
}
