package rpggame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rpggame.Interfaces.*;
import rpggame.Items.FoodItem;
import rpggame.Items.Item;

public class GameEngineTest {

    private GameEngine gameEngine;
    private GameUIInterface mockUI;
    private EntityInterface mockPlayer;
    private EntityInterface mockStore;
    private ItemInterface mockItem;

    @BeforeEach
    public void setUp() {
        mockUI = mock(GameUIInterface.class);
        mockPlayer = mock(Player.class);
        mockStore = mock(EntityInterface.class);
        mockItem = mock(ItemInterface.class);

        gameEngine = new GameEngine("ItemDatabase.csv");
        gameEngine.setUI(mockUI);
        gameEngine.initialize(); // Initialize the game engine
        gameEngine.setPlayer(mockPlayer);
        //assertEquals(mockPlayer, gameEngine.getPlayer(), "The player in the GameEngine should be the mockPlayer");
    }


    @Test
    public void testStoreMenu() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("5"); // Exit store option

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("storeMenu", EntityInterface.class, EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockStore, mockPlayer);

        // Verify interactions or state changes if any
        verify(mockUI).displayMessage("\nStore Menu:");
        verify(mockUI).displayMessage("Exiting the store...");
    }

    @Test
    public void testEatFoodMenu() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("InvalidFoodName");

        when(((Player) mockPlayer).getHealth()).thenReturn(100);
        List<FoodItem> mockFoodInventory = new ArrayList<>();
        FoodItem mockFood1 = mock(FoodItem.class);
        when(mockFood1.getName()).thenReturn("Apple");
        mockFood1.hpRegen = 10;
        mockFoodInventory.add(mockFood1);
        when(((Player) mockPlayer).exposeFoodInventory()).thenReturn(mockFoodInventory);

        // Mock the user input for the food name
        when(mockUI.getUserInput()).thenReturn("Apple");

        // Invoke the private method using reflection
        Method method = GameEngine.class.getDeclaredMethod("eatFoodMenu", EntityInterface.class);
        method.setAccessible(true);
        method.invoke(gameEngine, mockPlayer);

        // Verify the interactions with the mock UI
        verify(mockUI).displayMessage("\nCurrent Player Health: 100");
        verify(mockUI).displayMessage("Items in food bag: ");
        verify(mockUI).displayMessage("Apple: +10");
        verify(mockUI).displayMessage("\nEnter the name of the food you wish to eat:");
        when(mockUI.getUserInput()).thenReturn("Apple");
    }

    @Test
    public void testEquipItemMenu() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("1").thenReturn("1");

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("equipItemMenu", EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method with empty inventory
        method.invoke(gameEngine, mockPlayer);

        // Verify interactions for empty inventory
        verify(mockUI).displayMessage("\nPlayer's Inventory:");
        verify(mockUI).displayMessage("Inventory is empty.");

        // Add an item to the player's inventory
        when(mockPlayer.exposeInventory()).thenReturn(Collections.singletonList(mockItem));
        when(mockItem.getName()).thenReturn("TestItem");

        // Invoke the method again with an item in the inventory
        method.invoke(gameEngine, mockPlayer);

        // Verify interactions for non-empty inventory
        verify(mockUI, times(2)).displayMessage("\nPlayer's Inventory:");
        verify(mockUI).displayMessage("1. TestItem");
        verify(mockUI).displayMessage("Enter the number of the item you want to equip:");
        verify(mockPlayer).equip(mockItem);
        verify(mockUI).displayMessage("Player equipped TestItem");
    }

    @Test
    public void testRun() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("6"); // Exit option

        // Invoke the run method
        gameEngine.run();

        // Verify interactions
        verify(mockUI).displayMessage("\nEnter a command (1 to enter the store, 2 to equip an item, 3 to use equipped item, 4 to eat food, 5 to view worn clothing, 6 to exit):");
        verify(mockUI).displayMessage("Exiting the program...");
    }

    @Test
    public void testInitialize() {
        // Setup
        List<ItemInterface> mockGameItems = Collections.singletonList(mockItem);
        when(mockPlayer.exposeInventory()).thenReturn(mockGameItems);

        // Invoke the initialize method
        gameEngine.initialize();

        // Verify interactions
        //verify(mockPlayer).exposeInventory();
    }

    @Test
    public void testGetPlayerInventory() {
        // Setup
        List<ItemInterface> mockGameItems = Collections.singletonList(mockItem);
        when(mockPlayer.exposeInventory()).thenReturn(mockGameItems);

        // Assert that the mock setup is correct
        assertEquals(mockGameItems, mockPlayer.exposeInventory(), "Mock player's inventory should match mockGameItems");

        // Invoke the getPlayerInventory method
        List<ItemInterface> result = gameEngine.getPlayerInventory();

        // Verify interactions and result
        verify(mockPlayer, times(2)).exposeInventory();
        assertNotNull(result, "Returned inventory should not be null");
        assertFalse(result.isEmpty(), "Returned inventory should not be empty");
        assertEquals(mockGameItems.size(), result.size(), "Returned inventory size should match mock inventory size");
        assertEquals(mockGameItems.get(0), result.get(0), "Returned item should match mock item");
    }



    @Test
    public void testGetPlayerMoney() {
        // Setup
        when(((Player) mockPlayer).wealth()).thenReturn(100.0);

        // Invoke the getPlayerMoney method
        Double result = gameEngine.getPlayerMoney();

        // Verify interactions and result
        verify((Player) mockPlayer).wealth();
        assertNotNull(result, "Returned money should not be null");
        assertEquals(100.0, result, 0.001, "Returned money should match expected value");
    }

    @Test
    public void testStoreMenuBuyItem() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("1", "TestItem", "5"); // Buy item option, item name, exit store option
        when(mockStore.getItemByName("TestItem")).thenReturn(mockItem);
        when(mockStore.buy(mockItem, mockPlayer)).thenReturn(true);

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("storeMenu", EntityInterface.class, EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockStore, mockPlayer);

        // Verify interactions
        verify(mockUI).displayMessage("Enter the name of the item you want to buy:");
        verify(mockUI).displayMessage("Item purchased successfully!");
    }

    @Test
    public void testStoreMenuSellItem() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("2", "TestItem", "5"); // Sell item option, item name, exit store option
        when(mockPlayer.getItemByName("TestItem")).thenReturn(mockItem);

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("storeMenu", EntityInterface.class, EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockStore, mockPlayer);

        // Verify interactions
        verify(mockUI).displayMessage("\nEnter the name of the item you want to sell:");
        verify(mockUI).displayMessage("Item sold successfully!");
    }

    @Test
    public void testEatFoodMenuEmptyBag() throws Exception {
        // Setup
        when(((Player) mockPlayer).getHealth()).thenReturn(100);
        when(((Player) mockPlayer).exposeFoodInventory()).thenReturn(Collections.emptyList());

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("eatFoodMenu", EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockPlayer);

        // Verify interactions
        verify(mockUI).displayMessage("Food bag is empty!");
    }

    @Test
    public void testClothingMenuNoWornClothing() throws Exception {
        // Setup
        when(((Player) mockPlayer).exposeWearInventory()).thenReturn(Collections.emptyList());

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("clothingMenu", EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockPlayer);

        // Verify interactions
        verify(mockUI).displayMessage("Player is wearing no clothing!");
    }

    @Test
    public void testStoreMenuInvalidOption() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("99", "5"); // Invalid option followed by exit store option

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("storeMenu", EntityInterface.class, EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockStore, mockPlayer);

        // Verify interactions
        verify(mockUI).displayMessage("Invalid command!");
    }


    @Test
    public void testEatFoodMenuInvalidFoodName() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("InvalidFoodName");
        FoodItem mockFoodItem = mock(FoodItem.class);
        when(mockFoodItem.getName()).thenReturn("ValidFoodName");
        mockFoodItem.hpRegen = 10;
        List<FoodItem> mockFoodInventory = Collections.singletonList(mockFoodItem);
        when(((Player) mockPlayer).exposeFoodInventory()).thenReturn(mockFoodInventory);
        when(((Player) mockPlayer).getHealth()).thenReturn(100);
        when(((Player) mockPlayer).getItemByName("InvalidFoodName")).thenReturn(null);

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("eatFoodMenu", EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockPlayer);

        // Verify interactions
        verify(mockUI).displayMessage("\nCurrent Player Health: 100");
        verify(mockUI).displayMessage("Items in food bag: ");
        verify(mockUI).displayMessage("ValidFoodName: +10");
        verify(mockUI).displayMessage("\nEnter the name of the food you wish to eat:");
        verify(mockUI).displayMessage("\nEnter valid food name!");
    }


    @Test
    public void testEquipItemMenuInvalidItemNumber() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("99"); // Invalid item number
        when(mockItem.getName()).thenReturn("TestItem");
        List<ItemInterface> mockInventory = Collections.singletonList(mockItem);
        when(mockPlayer.exposeInventory()).thenReturn(mockInventory);

        // Access the private method
        Method method = GameEngine.class.getDeclaredMethod("equipItemMenu", EntityInterface.class);
        method.setAccessible(true);

        // Invoke the method
        method.invoke(gameEngine, mockPlayer);

        // Verify interactions
        verify(mockUI).displayMessage("\nPlayer's Inventory:");
        verify(mockUI).displayMessage("1. TestItem");
        verify(mockUI).displayMessage("Enter the number of the item you want to equip:");
        verify(mockUI).displayMessage("Invalid choice!");
    }


    @Test
    public void testRunDifferentCommands() throws Exception {
        // Setup
        when(mockUI.getUserInput()).thenReturn("1", "5", "2", "3", "4", "5", "6"); // Different command options

        // Invoke the run method multiple times
        gameEngine.run();

        // Verify interactions
        verify(mockUI, times(6)).displayMessage("\nEnter a command (1 to enter the store, 2 to equip an item, 3 to use equipped item, 4 to eat food, 5 to view worn clothing, 6 to exit):");
    }






    // Add more tests as needed.
}
