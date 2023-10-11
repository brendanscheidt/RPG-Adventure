package rpggame;

import rpggame.Interfaces.*;
import rpggame.Items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private Player player;
    private GameUIInterface mockUI;
    private EntityInterface mockEntity;
    private ItemInterface mockItem;
    private FoodItem mockFoodItem;
    private ClothingItem mockClothingItem;

    @BeforeEach
    public void setUp() {
        mockUI = mock(GameUIInterface.class);
        mockEntity = mock(EntityInterface.class);
        mockItem = mock(ItemInterface.class);
        mockClothingItem = mock(ClothingItem.class);
        mockFoodItem = mock(FoodItem.class);

        player = new Player(1000); // Player starts with 1000 units of money.
        player.setUI(mockUI); // Set the mocked UI to the player instance
    }

    @Test
    public void testInitialInventoryState() {
        assertTrue(player.getInventory().isEmpty());
        assertTrue(player.exposeWearInventory().isEmpty());
        assertTrue(player.exposeFoodInventory().isEmpty());
        assertTrue(player.exposeCurrencyInventory().size() == 1); // Contains only the money item
        assertNull(player.getEquipedItem());
    }



    @Test
    public void testBuyItem() {
        when(mockItem.getPrice()).thenReturn(100.0);
        assertTrue(player.buy(mockItem, mockEntity));
        assertEquals(900, player.wealth());
    }

    @Test
    public void testSellItem() {
        when(mockItem.getPrice()).thenReturn(100.0);
        player.aquire(mockItem);
        assertTrue(player.sell(mockItem, mockEntity));
        assertEquals(1100, player.wealth());
    }

    @Test
    public void testEquipItem() {
        player.equip(mockItem);
        assertEquals(mockItem, player.getEquipedItem());
    }

    @Test
    public void testConsumeFoodItem() {
        mockFoodItem.hpRegen = 10;
        player.aquire(mockFoodItem);
        player.consume(mockFoodItem);
        assertEquals(40, player.getHealth());
    }

    @Test
    public void testInventoryManagement() {
        player.aquire(mockItem);
        assertTrue(player.getInventory().contains(mockItem));

        player.relinquish(mockItem);
        assertFalse(player.getInventory().contains(mockItem));

        player.aquire(mockFoodItem);
        assertTrue(player.exposeFoodInventory().contains(mockFoodItem));

        player.relinquish(mockFoodItem);
        assertFalse(player.exposeFoodInventory().contains(mockFoodItem));

        player.aquire(mockClothingItem);
        player.wear(mockClothingItem);
        assertTrue(player.exposeWearInventory().contains(mockClothingItem));

        player.relinquish(mockClothingItem);
        assertFalse(player.exposeWearInventory().contains(mockClothingItem));
    }

    @Test
    public void testPlayerInitialHealthAndMoney() {
        assertEquals(1000, player.wealth());
        assertEquals(30, player.getHealth());
    }

    @Test
    public void testHealthRegenerationBeyondMax() {
        mockFoodItem.hpRegen = 80;
        player.aquire(mockFoodItem);
        player.consume(mockFoodItem);
        assertEquals(100, player.getHealth()); // Health should not exceed 100
    }

    @Test
    public void testMoneyOverflow() {
        assertFalse(player.addMoney(Double.POSITIVE_INFINITY));
        assertEquals(1000, player.wealth()); // Money should remain unchanged
    }

    @Test
    public void testEnterAndExitStore() {
        player.enter(mockEntity);
        verify(mockEntity).enter(player);

        player.exit(mockEntity);
        verify(mockEntity).exit(player);
    }

    @Test
    public void testUseEquippedItem() {
        player.equip(mockItem);
        player.useEquippedItem();
        verify(mockUI).displayMessage("Player used " + mockItem.getName());
    }

    @Test
    public void testWearingAndRemovingClothing() {
        player.aquire(mockClothingItem);
        player.wear(mockClothingItem);
        assertTrue(player.exposeWearInventory().contains(mockClothingItem));

        player.relinquish(mockClothingItem);
        assertFalse(player.exposeWearInventory().contains(mockClothingItem));
    }

    @Test
    public void testCurrencyManagement() {
        assertTrue(player.addMoney(500.0));
        assertEquals(1500, player.wealth());

        assertTrue(player.removeMoney(300.0));
        assertEquals(1200, player.wealth());
    }

    @Test
    public void testRetrieveItemByName() {
        player.aquire(mockItem);
        when(mockItem.getName()).thenReturn("Sword");
        assertEquals(mockItem, player.getItemByName("Sword"));
    }

    @Test
    public void testMoneyUnderflow() {
        assertFalse(player.removeMoney(1500.0));
        assertEquals(1000, player.wealth()); // Money should remain unchanged
    }

    @Test
    public void testEquipNullItem() {
        player.equip(null);
        assertNull(player.getEquipedItem());
    }

    @Test
    public void testConsumeNonFoodItem() {
        assertThrows(ClassCastException.class, () -> {
            player.eat(mockItem); // mockItem isn't a FoodItem
        });
    }


    @Test
    public void testWearNonClothingItem() {
        assertThrows(IllegalArgumentException.class, () -> player.wear(mockItem));
    }

    @Test
    public void testWearItemNotInInventory() {
        ClothingItem mockClothingNotInInventory = mock(ClothingItem.class);
        assertFalse(player.getInventory().contains(mockClothingNotInInventory));
        assertThrows(IllegalArgumentException.class, () -> {
            player.wear(mockClothingNotInInventory);
        });
    }

    @Test
    public void testConsumeItemNotInInventory() {
        FoodItem mockFoodNotInInventory = mock(FoodItem.class);
        assertFalse(player.getInventory().contains(mockFoodNotInInventory));
        assertThrows(IllegalArgumentException.class, () -> {
            player.eat(mockFoodNotInInventory);
        });
    }

    @Test
    public void testWearClothingNotInInventory() {
        ClothingItem mockClothingNotInInventory = mock(ClothingItem.class);
        assertFalse(player.getInventory().contains(mockClothingNotInInventory));
        assertThrows(IllegalArgumentException.class, () -> {
            player.wear(mockClothingNotInInventory);
        });
    }


    @Test
    public void testUseWithoutEquippedItem() {
        assertNull(player.getEquipedItem());
        player.useEquippedItem();
        verify(mockUI).displayMessage("Player has no item equipped to use!");
    }



    @Test
    public void testEquipWhileAnotherItemEquipped() {
        ItemInterface anotherMockItem = mock(ItemInterface.class);
        player.equip(mockItem);
        player.equip(anotherMockItem);
        assertEquals(anotherMockItem, player.getEquipedItem());
    }

    @Test
    public void testRelinquishNonExistingItem() {
        player.relinquish(mockItem);
        assertFalse(player.getInventory().contains(mockItem));
    }

    @Test
    public void testHealthRegenerationNegativeValue() {
        mockFoodItem.hpRegen = -10;
        player.aquire(mockFoodItem);
        player.consume(mockFoodItem);
        assertEquals(20, player.getHealth());
    }

    @Test
    public void testSetUI() {
        GameUIInterface anotherMockUI = mock(GameUIInterface.class);
        player.setUI(anotherMockUI);
        player.useEquippedItem(); // This should use the new UI
        verify(anotherMockUI).displayMessage("Player has no item equipped to use!");
    }

    @Test
    public void testWealth() {
        assertEquals(1000, player.wealth());
    }

    @Test
    public void testSpend() {
        player.spend(100.0);
        assertEquals(900, player.wealth());
    }

    @Test
    public void testGet() {
        player.get(100.0);
        assertEquals(1100, player.wealth());
    }

    @Test
    public void testHold() {
        player.hold(mockItem);
        assertEquals(mockItem, player.getEquipedItem());
    }

    @Test
    public void testDrink() {
        mockFoodItem.hpRegen = 10;
        player.aquire(mockFoodItem);
        player.drink(mockFoodItem);
        assertEquals(40, player.getHealth());
    }

    @Test
    public void testGetItemByNameNonExistent() {
        assertNull(player.getItemByName("NonExistentItem"));
    }

    @Test
    public void testEnterWithNull() {
        assertThrows(NullPointerException.class, () -> player.enter(null));
    }

    @Test
    public void testExitWithNull() {
        assertThrows(NullPointerException.class, () -> player.exit(null));
    }

    @Test
    public void testAquireNullItem() {
        assertThrows(NullPointerException.class, () -> player.aquire(null));
    }

    @Test
    public void testRelinquishNullItem() {
        assertThrows(NullPointerException.class, () -> player.relinquish(null));
    }

    @Test
    public void testConsumeNonFoodItemDirectly() {
        assertThrows(ClassCastException.class, () -> player.consume((FoodItem) mockItem));
    }

    @Test
    public void testWearNonClothingItemDirectly() {
        assertThrows(IllegalArgumentException.class, () -> player.wear(mockItem));
    }

    @Test
    public void testWearAlreadyWearing() {
        player.aquire(mockClothingItem);
        player.wear(mockClothingItem);
        player.wear(mockClothingItem);
        verify(mockUI).displayMessage("Player is already wearing that!");
    }

    @Test
    public void testWearClothingNotInInventoryDirectly() {
        ClothingItem anotherMockClothingItem = mock(ClothingItem.class);
        assertThrows(IllegalArgumentException.class, () -> player.wear(anotherMockClothingItem));
    }




    // Add more tests as needed.
}
