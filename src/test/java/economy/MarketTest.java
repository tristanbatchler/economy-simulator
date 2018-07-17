package economy;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit tests for the {@link Market} class.
 */
public class MarketTest {
    Item[] items = {new Item("Item 0"), new Item("Item 1"), new Item("Item 2")};

    @Test (expected = IllegalStateException.class)
    public void constructor() {
        Market market = new Market(0.05);
        assertEquals("Market", market.name);
        assertEquals(0, market.getSupply(items[0]));
        assertEquals(0, market.getSupply(items[1]));
        assertEquals(0, market.getSupply(items[2]));
        double price = market.getPrice(items[0]);
        price = market.getPrice(items[1]);
        price = market.getPrice(items[2]);
    }

    @Test (expected = IllegalStateException.class)
    public void constructorWithName() {
        Market market = new Market("Market with Name", 0.05);
        assertEquals("Market with Name", market.name);
        assertEquals(0, market.getSupply(items[0]));
        assertEquals(0, market.getSupply(items[1]));
        assertEquals(0, market.getSupply(items[2]));
        double price = market.getPrice(items[0]);
        price = market.getPrice(items[1]);
        price = market.getPrice(items[2]);
    }

    @Test
    public void getSupplyOfNonExistentItem() {
        Market market = new Market(0.05);
        assertEquals(0, market.getSupply(items[0]));
    }

    @Test
    public void getSupplyTest() {
        Agent agent = new Agent();
        agent.receive(items[0], 10);

        Market market = new Market(0.05);
        market.addAgent(agent);

        assertEquals(10, market.getSupply(items[0]));
    }

    @Test (expected = IllegalStateException.class)
    public void getPriceOfNonExistentItem() {
        Market market = new Market(0.05);
        double price = market.getPrice(items[0]);
    }

    @Test
    public void getPriceTest() {
        for (int i = 0; i < 10000; i++) {
            Agent agent = new Agent();
            agent.receive(items[0], 10);

            Market market = new Market(0.05);
            market.addAgent(agent);

            assertTrue(market.getPrice(items[0]) >= 0);
        }
    }

    @Test
    public void getItemsOfEmptyMarket() {
        Market market = new Market(0.05);
        assertTrue(market.getItems().isEmpty());
    }

    @Test
    public void getItemsTest() {
        Agent agent1 = new Agent();
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 1);

        Agent agent2 = new Agent();
        agent2.receive(items[1], 3);
        agent2.receive(items[2], 5);


        Market market = new Market(0.05);
        market.addAgents(agent1, agent2);

        assertEquals(3, market.getItems().size());
        assertTrue(market.getItems().contains(items[0]));
        assertTrue(market.getItems().contains(items[1]));
        assertTrue(market.getItems().contains(items[2]));
    }

    @Test
    public void setPriceOfNonExistentItem() {
        Market market = new Market(0.05);
        market.setPrice(items[0], 10.00);
        assertEquals(10.00, market.getPrice(items[0]), 0.0);
        assertEquals(0, market.getSupply(items[0]));
    }

    @Test
    public void setPriceOfExistingItem() {
        Market market = new Market(0.05);

        Agent agent1 = new Agent(market);
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 1);

        Agent agent2 = new Agent(market);
        agent2.receive(items[1], 3);
        agent2.receive(items[2], 5);

        market.setPrice(items[1], 0.00);
        assertEquals(0.00, market.getPrice(items[1]), 0.0);
    }

    @Test (expected = IllegalStateException.class)
    public void changePriceOfNonExistentItem() {
        Market market = new Market(0.05);
        market.changePrice(items[0], 10.00);
    }

    @Test
    public void increasePriceOfExistingItem() {
        Market market = new Market(0.05);

        Agent agent1 = new Agent(market);
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 1);

        Agent agent2 = new Agent(market);
        agent2.receive(items[1], 3);
        agent2.receive(items[2], 5);

        double oldPrice = market.getPrice(items[0]);

        market.changePrice(items[0], 10.00);

        assertEquals(oldPrice + 10.00, market.getPrice(items[0]), 0.0);
    }

    @Test
    public void decreasePriceOfExistingItem() {
        Market market = new Market(0.05);

        Agent agent1 = new Agent(market);
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 1);

        Agent agent2 = new Agent(market);
        agent2.receive(items[1], 3);
        agent2.receive(items[2], 5);

        double oldPrice = market.getPrice(items[0]);

        market.changePrice(items[0], -10.00);

        assertEquals(oldPrice - 10.00, market.getPrice(items[0]), 0.0);
    }

    @Test
    public void generateAgentsTest() {
        Market market = new Market(0.05);
        market.generateAgents(100);
        assertEquals(100, market.getAgents().size());
    }

    @Test
    public void generateNegativeAgents() {
        Market market = new Market(0.05);
        market.generateAgents(-100);
        assertEquals(0, market.getAgents().size());
    }

    @Test
    public void generateItemsTest() {
        Market market = new Market(0.05);
        market.generateAgents(100);
        market.generateItems(50, 100);
        assertTrue(market.getItems().size() >= 0 && market.getItems().size() < 50);
    }

    @Test
    public void generateNegativeItems() {
        Market market = new Market(0.05);
        market.generateAgents(100);
        market.generateItems(-50, 100);
        assertEquals(0, market.getItems().size());
    }

    @Test
    public void generateMoneyTest() {
        Market market = new Market(0.05);
        market.generateAgents(100);
        market.generateMoney(100);
        for (Agent agent : market.getAgents()) {
            assertEquals(1.00, agent.getMoney(), 0.0);
        }
    }

    @Test
    public void generateNegativeMoney() {
        Market market = new Market(0.05);
        market.generateAgents(100);
        market.generateMoney(-100);
        for (Agent agent : market.getAgents()) {
            assertEquals(0.0, agent.getMoney(), 0.0);
        }
    }

    @Test (expected = IllegalStateException.class)
    public void addAgentWithNoItemsToEmptyMarket() {
        Market market = new Market(0.05);
        Agent agent = new Agent();
        market.addAgent(agent);

        assertEquals("Market", market.name);
        assertEquals(0, market.getSupply(items[0]));
        assertEquals(0, market.getSupply(items[1]));
        assertEquals(0, market.getSupply(items[2]));
        double price = market.getPrice(items[0]);
        price = market.getPrice(items[1]);
        price = market.getPrice(items[2]);
    }

    @Test
    public void addAgentWithItemsToNonEmptyMarket() {
        Market market = new Market(0.05);

        Agent agent1 = new Agent(market);
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 20);
        agent1.receive(items[2], 30);

        double oldPrice0 = market.getPrice(items[0]);
        double oldPrice1 = market.getPrice(items[1]);
        double oldPrice2 = market.getPrice(items[2]);

        Agent agent2 = new Agent();
        agent2.receive(items[0], 5);
        agent2.receive(items[2], 1);

        market.addAgent(agent2);

        double expectedPrice0 = Math.max(0, oldPrice0 * (1 - market.elasticity * 5));
        double expectedPrice1 = oldPrice1;
        double expectedPrice2 = Math.max(0, oldPrice2 * (1 - market.elasticity * 1));

        assertEquals(15, market.getSupply(items[0]));
        assertEquals(20, market.getSupply(items[1]));
        assertEquals(31, market.getSupply(items[2]));
        assertEquals(expectedPrice0, market.getPrice(items[0]), 0x1.0p-10);
        assertEquals(expectedPrice1, market.getPrice(items[1]), 0x1.0p-10);
        assertEquals(expectedPrice2, market.getPrice(items[2]), 0x1.0p-10);
    }

    @Test
    public void addAgentWithNoItemsToNonEmptyMarket() {
        Market market = new Market(0.05);

        Agent agent1 = new Agent(market);
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 20);
        agent1.receive(items[2], 30);

        double oldPrice0 = market.getPrice(items[0]);
        double oldPrice1 = market.getPrice(items[1]);
        double oldPrice2 = market.getPrice(items[2]);

        Agent agent2 = new Agent();

        market.addAgent(agent2);

        assertEquals(10, market.getSupply(items[0]));
        assertEquals(20, market.getSupply(items[1]));
        assertEquals(30, market.getSupply(items[2]));
        assertEquals(oldPrice0, market.getPrice(items[0]), 0x1.0p-10);
        assertEquals(oldPrice1, market.getPrice(items[1]), 0x1.0p-10);
        assertEquals(oldPrice2, market.getPrice(items[2]), 0x1.0p-10);
    }

    @Test
    public void addAgentWithItemsToEmptyMarket() {
        Market market = new Market(0.05);

        Agent agent = new Agent();
        agent.receive(items[0], 5);
        agent.receive(items[2], 1);

        market.addAgent(agent);

        assertEquals(5, market.getSupply(items[0]));
        assertEquals(0, market.getSupply(items[1]));
        assertEquals(1, market.getSupply(items[2]));

        assertTrue(market.getPrice(items[0]) >= 0);
        assertTrue(market.getPrice(items[2]) >= 0);
    }

    @Test
    public void addAgentsTest1() {
        Market market = new Market(0.05);

        Agent[] agents = new Agent[100];
        for (int i = 0; i < 100; i++) {
            agents[i] = new Agent();
        }

        market.addAgents(agents);

        assertEquals(100, market.getAgents().size());
    }

    @Test
    public void addAgentsTest2() {
        Market market = new Market(0.05);
        market.addAgents(new Agent(), new Agent(), new Agent());
        assertEquals(3, market.getAgents().size());
    }

    @Test (expected = IllegalStateException.class)
    public void removeAgentWithNoItemsFromEmptyMarket() {
        Market market = new Market(0.05);
        Agent agent = new Agent();
        market.addAgent(agent);

        market.removeAgent(agent);

        assertEquals("Market", market.name);
        assertEquals(0, market.getSupply(items[0]));
        assertEquals(0, market.getSupply(items[1]));
        assertEquals(0, market.getSupply(items[2]));
        double price = market.getPrice(items[0]);
        price = market.getPrice(items[1]);
        price = market.getPrice(items[2]);
    }

    @Test
    public void removeAgentWithItemsFromNonEmptyMarket1() {
        Market market = new Market(0.05);

        Agent agent1 = new Agent(market);
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 20);
        agent1.receive(items[2], 30);

        double oldPrice0 = market.getPrice(items[0]);
        double oldPrice1 = market.getPrice(items[1]);
        double oldPrice2 = market.getPrice(items[2]);

        Agent agent2 = new Agent();
        agent2.receive(items[0], 5);
        agent2.receive(items[2], 1);

        market.addAgent(agent2);

        market.removeAgent(agent2);

        assertEquals(10, market.getSupply(items[0]));
        assertEquals(20, market.getSupply(items[1]));
        assertEquals(30, market.getSupply(items[2]));

        assertEquals(oldPrice0, market.getPrice(items[0]), 0x1.0p-10);
        assertEquals(oldPrice1, market.getPrice(items[1]), 0x1.0p-10);
        assertEquals(oldPrice2, market.getPrice(items[2]), 0x1.0p-10);
    }

    @Test
    public void removeAgentWithItemsFromNonEmptyMarket2() {
        Market market = new Market(0.10);
        Item apple = new Item("apple");

        Agent agent1 = new Agent(market);
        agent1.receive(apple, 1);
        market.setPrice(apple, 10.00);

        double oldPrice = market.getPrice(apple);

        Agent agent2 = new Agent();
        agent2.receive(apple, 1);

        System.out.println("BEFORE NEW APPLE ENTERS:");
        System.out.println(market);

        market.addAgent(agent2);

        System.out.println("WHILE APPLE IS IN MARKET:");
        System.out.println(market);

        market.removeAgent(agent2);
        System.out.println("AFTER APPLE LEAVES:");
        System.out.println(market);

        assertEquals(oldPrice, market.getPrice(apple), 0x1.0p-10);
    }

    @Test
    public void removeAgentWithNoItemsFromNonEmptyMarket() {
        Market market = new Market(0.05);

        Agent agent1 = new Agent(market);
        agent1.receive(items[0], 10);
        agent1.receive(items[1], 20);
        agent1.receive(items[2], 30);

        double oldPrice0 = market.getPrice(items[0]);
        double oldPrice1 = market.getPrice(items[1]);
        double oldPrice2 = market.getPrice(items[2]);

        Agent agent2 = new Agent();

        market.addAgent(agent2);

        market.removeAgent(agent2);

        assertEquals(10, market.getSupply(items[0]));
        assertEquals(20, market.getSupply(items[1]));
        assertEquals(30, market.getSupply(items[2]));
        assertEquals(oldPrice0, market.getPrice(items[0]), 0x1.0p-10);
        assertEquals(oldPrice1, market.getPrice(items[1]), 0x1.0p-10);
        assertEquals(oldPrice2, market.getPrice(items[2]), 0x1.0p-10);
    }

    @Test
    public void removeAgentWithItemsFromEmptyMarket() {
        Market market = new Market(0.05);

        Agent agent = new Agent();
        agent.receive(items[0], 5);
        agent.receive(items[2], 1);

        market.addAgent(agent);

        market.removeAgent(agent);

        assertEquals(0, market.getSupply(items[0]));
        assertEquals(0, market.getSupply(items[1]));
        assertEquals(0, market.getSupply(items[2]));

        assertTrue(market.getPrice(items[0]) >= 0);
        assertTrue(market.getPrice(items[2]) >= 0);
    }

    @Test
    public void removeAgentsTest1() {
        Market market = new Market(0.05);

        Agent[] agents = new Agent[100];
        Agent[] agentsToRemove = new Agent[45];
        for (int i = 0; i < 100; i++) {
            agents[i] = new Agent();
            if (i < 45) {
                agentsToRemove[i] = agents[i];
            }
        }

        market.addAgents(agents);

        market.removeAgents(agentsToRemove);

        assertEquals(55, market.getAgents().size());
    }

    @Test
    public void removeAgentsTest2() {
        Market market = new Market(0.05);
        market.addAgents(new Agent(), new Agent(), new Agent());
        market.removeAgents(market.getAgents().get(0), market.getAgents().get(2));
        assertEquals(1, market.getAgents().size());
    }

    @Test
    public void getRandomAgentInEmptyMarket() {
        Market market = new Market(0.05);
        assertNull(market.getRandomAgent());
    }

    @Test
    public void getRandomAgentInMarketWithOneAgent() {
        Market market = new Market(0.05);
        Agent agent = new Agent(market);
        assertEquals(agent, market.getRandomAgent());
    }

    @Test
    public void getRandomAgentTest() {
        Market market = new Market(0.05);
        market.generateAgents(100);
        for (int i = 0; i < 10000; i++) {
            assertNotNull(market.getRandomAgent());
        }
    }

    @Test
    public void getAgentsInEmptyMarket() {
        Market market = new Market(0.05);
        assertTrue(market.getAgents().isEmpty());
    }

    @Test
    public void getAgentsInMarketWithOneAgent() {
        Market market = new Market(0.05);
        Agent agent = new Agent(market);
        assertEquals(1, market.getAgents().size());
        assertTrue(market.getAgents().contains(agent));
    }

    @Test
    public void getAgentsTest() {
        Market market = new Market(0.05);
        market.generateAgents(100);
        assertEquals(100, market.getAgents().size());
    }

    @Test
    public void getEmptyAgentWealth() {
        Market market = new Market(0.05);
        Agent agent = new Agent(market);
        assertEquals(0.0, market.getAgentWealth(agent), 0.0);
    }

    @Test
    public void getNonEmptyAgentWealth() {
        Market market = new Market(0.05);
        Agent agent = new Agent(market);

        agent.receive(50.00);

        Item item = new Item();
        agent.receive(item, 1);

        market.setPrice(item, 100.0);

        assertEquals(150.00, market.getAgentWealth(agent), 0.0);
    }

    @Test
    public void buyZeroQuantity() throws InsufficientAmountException {
        Market market = new Market(0.05);
        Agent buyer = new Agent(market);
        Agent seller = new Agent(market);

        buyer.receive(10000.00);
        seller.receive(items[0], 10);

        double oldMarketPrice = market.getPrice(items[0]);

        market.buy(buyer, seller, items[0], 0, 1.00);

        assertEquals(oldMarketPrice, market.getPrice(items[0]), 0.0);
        assertFalse(buyer.inventory.contains(items[0]));
        assertEquals(10000.00, buyer.getMoney(), 0.0);
        assertEquals(10, seller.inventory.getQuantity(items[0]));
        assertEquals(0.00, seller.getMoney(), 0.0);
    }

    @Test
    public void buyNegativeQuantity() throws InsufficientAmountException {
        Market market = new Market(0.05);
        Agent buyer = new Agent(market);
        Agent seller = new Agent(market);

        buyer.receive(10000.00);
        seller.receive(items[0], 10);

        double oldMarketPrice = market.getPrice(items[0]);

        market.buy(buyer, seller, items[0], -1, 1.00);

        assertEquals(oldMarketPrice, market.getPrice(items[0]), 0.0);
        assertFalse(buyer.inventory.contains(items[0]));
        assertEquals(10000.00, buyer.getMoney(), 0.0);
        assertEquals(10, seller.inventory.getQuantity(items[0]));
        assertEquals(0.00, seller.getMoney(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void buyItemFromAnotherMarket() throws InsufficientAmountException {
        Market market1 = new Market(0.05);
        Market market2 = new Market(0.05);
        Agent buyer = new Agent(market1);
        Agent seller = new Agent(market2);
        seller.receive(items[0], 10);
        buyer.receive(10000.00);
        market1.buy(buyer, seller, items[0], 1, 1.00);
        market2.buy(buyer, seller, items[0], 1, 1.00);
    }

    @Test (expected = InsufficientAmountException.class)
    public void buyItemWithNoMoney() throws InsufficientAmountException {
        Market market = new Market(0.05);
        Agent buyer = new Agent(market);
        Agent seller = new Agent(market);
        seller.receive(items[0], 1);
        market.buy(buyer, seller, items[0], 1, 1.00);
    }

    @Test (expected = InsufficientAmountException.class)
    public void buyItemFromAgentWithNoItems() throws InsufficientAmountException {
        Market market = new Market(0.05);
        Agent buyer = new Agent(market);
        Agent seller = new Agent(market);
        buyer.receive(10000.00);
        market.buy(buyer, seller, items[0], 1, 1.00);

    }

    @Test
    public void buyItemTest() throws InsufficientAmountException {
        for (int i = 0; i < 10000; i++) {
            Market market = new Market(0.05);
            Agent buyer = new Agent(market);
            Agent seller = new Agent(market);

            buyer.receive(40000.00);
            seller.receive(items[0], 10);

            double oldMarketPrice = market.getPrice(items[0]);

            market.buy(buyer, seller, items[0], 4, 1.00);

            assertNotEquals(oldMarketPrice, market.getPrice(items[0]), 0.0);

            assertEquals(4, buyer.inventory.getQuantity(items[0]));
            assertEquals(6, seller.inventory.getQuantity(items[0]));

            assertEquals(39996.00, buyer.getMoney(), 0.0);
            assertEquals(4.00, seller.getMoney(), 0.0);
        }
    }

    @Test
    public void toStringTest() {
        Market market = new Market(0.05);
        Agent[] agents = {
                new Agent("Agent 0", market),
                new Agent("Agent 1", market),
                new Agent("Agent 2", market)
        };

        agents[0].receive(items[2], 10);
        agents[0].receive(items[1], 5);

        agents[1].receive(items[1], 20);

        agents[2].receive(items[1], 5);
        agents[2].receive(items[0], 10);

        agents[0].receive(101.00);
        agents[1].receive(10.00);
        agents[2].receive(101.00);

        market.setPrice(items[0], 12.00);
        market.setPrice(items[1], 13.00);
        market.setPrice(items[2], 11.00);

        String expected = "ITEMS: {\n" +
                "\t10x\"Item 0\"@$12.00ea,\n" +
                "\t10x\"Item 2\"@$11.00ea,\n" +
                "\t30x\"Item 1\"@$13.00ea,\n" +
                "}\n" +
                "AGENTS: {\n" +
                "\tAgent 0 worth $276.00 with $101.00 and 10x\"Item 2\", 5x\"Item 1\",\n" +
                "\tAgent 1 worth $270.00 with $10.00 and 20x\"Item 1\",\n" +
                "\tAgent 2 worth $286.00 with $101.00 and 10x\"Item 0\", 5x\"Item 1\",\n" +
                "}";

        assertEquals(expected, market.toString());
    }

}
