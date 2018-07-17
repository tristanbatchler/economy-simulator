package economy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for the {@link Agent} class.
 */
public class AgentTest {
    Item[] items = {new Item("Item 0"), new Item("Item 1"), new Item("Item 2")};

    @Test
    public void newAgentWithMarket() {
        Market market = new Market(0.05);
        Agent agent = new Agent(market);
        assertNotNull(agent.market);
        assertEquals(market, agent.market);
    }

    @Test
    public void newAgentWithMarketAndName() {
        Market market = new Market(0.05);
        Agent agent = new Agent("Agent", market);
        assertNotNull(agent.market);
        assertNotNull(agent.name);
        assertEquals(market, agent.market);
        assertEquals("Agent", agent.name);
    }

    @Test
    public void newAgentHasNoMoney() {
        Agent agent = new Agent();
        assertEquals(0, agent.getMoney(), 0.0);
    }

    @Test
    public void getMoneyTest() {
        Agent agent = new Agent();
        agent.receive(129.00);
        assertEquals(129.00, agent.getMoney(), 0.0);
    }

    @Test
    public void newAgentHasNoItems() {
        Agent agent = new Agent();
        assertTrue(agent.inventory.isEmpty());
    }

    @Test
    public void receiveZeroQuantity() {
        Agent agent = new Agent();
        agent.receive(items[0], 0);
        assertTrue(agent.inventory.isEmpty());
        assertEquals(0, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void receiveNegativeQuantity() {
        Agent agent = new Agent();
        agent.receive(items[0], -1);
        assertTrue(agent.inventory.isEmpty());
        assertEquals(0, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void receivePositiveQuantityOfExistingItem() {
        Agent agent = new Agent();
        agent.receive(items[0], 10);
        agent.receive(items[0], 1);
        assertFalse(agent.inventory.isEmpty());
        assertEquals(11, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void receiveNegativeQuantityOfExistingItem() {
        Agent agent = new Agent();
        agent.receive(items[0], 10);
        agent.receive(items[0], -1);
        assertFalse(agent.inventory.isEmpty());
        assertEquals(10, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void receiveZeroMoney() {
        Agent agent = new Agent();
        agent.receive(0.0);
        assertEquals(0.0, agent.getMoney(), 0.0);
    }

    @Test
    public void receiveNegativeMoney() {
        Agent agent = new Agent();
        agent.receive(-10.0);
        assertEquals(0.0, agent.getMoney(), 0.0);
    }

    @Test
    public void receiveMoneyTest() {
        Agent agent = new Agent();
        agent.receive(10.0);
        assertEquals(10.0, agent.getMoney(), 0.0);
    }

    @Test
    public void loseZeroQuantity() {
        Agent agent = new Agent();
        agent.lose(items[0], 0);
        assertTrue(agent.inventory.isEmpty());
        assertEquals(0, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void loseNegativeQuantity() {
        Agent agent = new Agent();
        agent.lose(items[0], -1);
        assertTrue(agent.inventory.isEmpty());
        assertEquals(0, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void losePositiveQuantityOfExistingItem() {
        Agent agent = new Agent();
        agent.receive(items[0], 10);
        agent.lose(items[0], 1);
        assertFalse(agent.inventory.isEmpty());
        assertEquals(9, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void loseNegativeQuantityOfExistingItem() {
        Agent agent = new Agent();
        agent.receive(items[0], 10);
        agent.lose(items[0], -1);
        assertFalse(agent.inventory.isEmpty());
        assertEquals(10, agent.inventory.getQuantity(items[0]));
    }

    @Test
    public void loseZeroMoney() {
        Agent agent = new Agent();
        agent.lose(0.0);
        assertEquals(0.0, agent.getMoney(), 0.0);
    }

    @Test
    public void loseNegativeMoney() {
        Agent agent = new Agent();
        agent.lose(-10.0);
        assertEquals(0.0, agent.getMoney(), 0.0);
    }

    @Test
    public void loseMoneyTest() {
        Agent agent = new Agent();
        agent.receive(29.96);
        agent.lose(29.96);
        assertEquals(0.0, agent.getMoney(), 0.0);
    }

    @Test
    public void newlyCreatedAgentToString() {
        Agent agent = new Agent("Agent");
        assertEquals("\"Agent\" with $0.00 and no items", agent.toString());
    }

    @Test
    public void toStringTest() {
        Agent agent = new Agent("Agent");
        agent.receive(items[0], 10);
        agent.receive(items[1], 20);
        agent.receive(129.95);
        assertEquals("\"Agent\" with $129.95 and 10x\"Item 0\", 20x\"Item 1\"", agent.toString());
    }

    @Test
    public void compareToSelf() {
        Agent agent = new Agent();
        assertEquals(0, agent.compareTo(agent));
    }

    @Test
    public void compareToEqualAgent() {
        Agent agent1 = new Agent();
        agent1.receive(19.11);
        agent1.receive(items[1], 12);
        Agent agent2 = new Agent();
        agent2.receive(19.11);
        agent2.receive(items[1], 12);
        assertEquals(0, agent1.compareTo(agent2));
        assertEquals(0, agent2.compareTo(agent1));
    }

    @Test
    public void compareToUnequalAgent() {
        Agent agent = new Agent();
        Agent agent1 = new Agent();
        agent1.receive(19.11);
        agent1.receive(items[1], 12);
        Agent agent2 = new Agent();
        agent2.receive(10000);
        assertTrue(agent1.compareTo(agent2) <  0);
        assertTrue(agent2.compareTo(agent1) > 0);
    }

    @Test
    public void agentEqualityIsReflexive() {
        Agent agent = new Agent();
        assertTrue(agent.equals(agent));
    }

    @Test
    public void agentEqualtityIsSymmetric() {
        Agent agent1 = new Agent("Agent");
        agent1.receive(19.11);
        agent1.receive(items[1], 12);
        Agent agent2 = new Agent("Agent");
        agent2.receive(19.11);
        agent2.receive(items[1], 12);
        assertTrue(agent1.equals(agent2));
        assertTrue(agent2.equals(agent1));

    }

    @Test
    public void agentIsNotEqualToNull() {
        Agent agent = new Agent();
        assertFalse(agent.equals(null));
    }

    @Test
    public void equalAgentsHaveEqualHashCodes() {
        Agent agent1 = new Agent("Agent");
        agent1.receive(19.11);
        agent1.receive(items[1], 12);
        Agent agent2 = new Agent("Agent");
        agent2.receive(19.11);
        agent2.receive(items[1], 12);
        assertEquals(agent1.hashCode(), agent2.hashCode());
    }

    @Test
    public void receiveItemInEmptyMarket() {
        Market market = new Market(0.05);
        Agent agent = new Agent();
        market.addAgent(agent);

        agent.receive(items[0], 7);

        assertEquals(7, market.getSupply(items[0]));
        assertTrue(market.getPrice(items[0]) >= 0);
    }

    @Test
    public void receiveItemInMarketAlreadyContainingItem() {
        Market market = new Market(0.05);
        Agent agent = new Agent();
        market.addAgent(agent);

        agent.receive(items[0], 1000);
        double oldMarketPrice = market.getPrice(items[0]);

        agent.receive(items[0], 1000);

        assertEquals(2000, market.getSupply(items[0]));
        double newMarketPrice = market.getPrice(items[0]);

        assertEquals(Math.max(0, oldMarketPrice * (1 - 0.05 * 1000)), newMarketPrice, 0x1.0p-10);
    }

    @Test
    public void loseItemInEmptyMarket() {
        Market market = new Market(0.05);
        Agent agent = new Agent();
        agent.receive(items[0], 10);

        assertEquals(0, market.getSupply(items[0]));

        market.addAgent(agent);
        assertEquals(10, market.getSupply(items[0]));
        double oldMarketPrice = market.getPrice(items[0]);

        agent.lose(items[0], 7);
        assertEquals(3, market.getSupply(items[0]));
        double newMarketPrice = market.getPrice(items[0]);

        assertEquals(oldMarketPrice * (1 + 0.05 * 7), newMarketPrice, 0x1.0p-10);
    }
}
