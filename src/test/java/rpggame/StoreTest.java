package rpggame;

import rpggame.Interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreTest {

    private Store store;
    private EntityInterface mockPlayer;
    private ItemInterface mockItem;
    private GameUIInterface mockUI;

    @BeforeEach
    public void setUp() {
        mockPlayer = mock(EntityInterface.class);
        mockItem = mock(ItemInterface.class);
        mockUI = mock(GameUIInterface.class);

        store = new Store();
        store.setUI(mockUI);
    }

    @Test
    public void testEnterStore() {
        store.enter(mockPlayer);
        assertTrue(store.exposeInventory().isEmpty()); // Assuming the store starts empty
    }

    @Test
    public void testEnterStoreTwice() {
        store.enter(mockPlayer);
        store.enter(mockPlayer);
        verify(mockUI).displayMessage("Player is already in the store.");
    }

    @Test
    public void testExitStore() {
        store.enter(mockPlayer);
        store.exit(mockPlayer);
        // No message should be displayed
    }

    @Test
    public void testExitStoreWithoutEntering() {
        store.exit(mockPlayer);
        verify(mockUI).displayMessage("Player never entered the store.");
    }

    @Test
    public void testBuyItem() {
        when(mockItem.getPrice()).thenReturn(100.0);
        store.aquire(mockItem);
        store.enter(mockPlayer);
        when(mockPlayer.removeMoney(100.0)).thenReturn(true);
        assertTrue(store.buyItem(mockItem, mockPlayer));
    }

    @Test
    public void testBuyItemNotInStore() {
        when(mockItem.getPrice()).thenReturn(100.0);
        store.enter(mockPlayer);
        when(mockPlayer.removeMoney(100.0)).thenReturn(true);
        assertFalse(store.buyItem(mockItem, mockPlayer));
        verify(mockUI).displayMessage("Item not available in the store.");
    }

    @Test
    public void testBuyItemWithoutEnteringStore() {
        when(mockItem.getPrice()).thenReturn(100.0);
        when(mockPlayer.removeMoney(100.0)).thenReturn(true);
        assertFalse(store.buyItem(mockItem, mockPlayer));
        verify(mockUI).displayMessage("Player needs to enter the store before being able to buy anything");
    }

    @Test
    public void testSellItem() {
        when(mockItem.getPrice()).thenReturn(100.0);
        store.enter(mockPlayer);
        assertTrue(store.sellItem(mockItem, mockPlayer));
    }

    @Test
    public void testSellItemWithoutEnteringStore() {
        when(mockItem.getPrice()).thenReturn(100.0);
        assertFalse(store.sellItem(mockItem, mockPlayer));
        verify(mockUI).displayMessage("Player needs to enter the store before being able to sell anything");
    }

    @Test
    public void testGetItemByName() {
        store.aquire(mockItem);
        when(mockItem.getName()).thenReturn("Sword");
        assertEquals(mockItem, store.getItemByName("Sword"));
    }

    @Test
    public void testGetItemByNameNonExistent() {
        assertNull(store.getItemByName("NonExistentItem"));
    }

    @Test
    public void testAddMoneyToStore() {
        store.addMoney(500.0);
        assertEquals(1500.0, store.wealth());
        // You might want to expose a method in Store to check its current money for verification.
    }

    @Test
    public void testRemoveMoneyFromStore() {
        store.removeMoney(500.0);
        assertEquals(500, store.wealth());
        // Similarly, you might want to expose a method in Store to check its current money for verification.
    }

    @Test
    public void testAquireItem() {
        store.aquire(mockItem);
        assertTrue(store.exposeInventory().contains(mockItem));
    }

    @Test
    public void testRelinquishItem() {
        store.aquire(mockItem);
        store.relinquish(mockItem);
        assertFalse(store.exposeInventory().contains(mockItem));
    }

    @Test
    public void testBuyItemWithoutEnoughMoney() {
        when(mockItem.getPrice()).thenReturn(1500.0); // Price more than the player can afford
        store.aquire(mockItem);
        store.enter(mockPlayer);
        when(mockPlayer.removeMoney(1500.0)).thenReturn(false); // Player can't afford
        assertFalse(store.buyItem(mockItem, mockPlayer));
        // Optionally, verify a message that indicates insufficient funds.
    }

    @Test
    public void testSellItemPlayerDoesntHave() {
        when(mockItem.getPrice()).thenReturn(100.0);
        store.enter(mockPlayer);
        // Assuming the player doesn't have the item, the store should still buy it (based on current logic)
        assertTrue(store.sellItem(mockItem, mockPlayer));
        // Optionally, verify a message that indicates the player sold an item they didn't have.
    }


    // Add more tests as needed.
}

