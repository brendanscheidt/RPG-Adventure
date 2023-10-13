package rpggame;

import rpggame.Factories.AmmoFactory;
import rpggame.Factories.BuffFactory;
import rpggame.Factories.ClothingFactory;
import rpggame.Factories.FoodFactory;
import rpggame.Factories.GenericItemFactory;
import rpggame.Factories.PotionFactory;
import rpggame.Factories.WeaponFactory;
import rpggame.Interfaces.*;
import rpggame.Items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FactoryProviderTest {

    @Test
    public void testClothingFactory() {
        ItemFactoryInterface factory = FactoryProvider.getFactoryByType("clothing");
        assertEquals(true, factory instanceof ClothingFactory);
    }

    @Test
    public void testAmmoFactory() {
        ItemFactoryInterface factory = FactoryProvider.getFactoryByType("ammo");
        assertEquals(true, factory instanceof AmmoFactory);
    }

    @Test
    public void testBuffFactory() {
        ItemFactoryInterface factory = FactoryProvider.getFactoryByType("Buff");
        assertEquals(true, factory instanceof BuffFactory);
    }

    @Test
    public void testFoodFactory() {
        ItemFactoryInterface factory = FactoryProvider.getFactoryByType("Food");
        assertEquals(true, factory instanceof FoodFactory);
    }

    @Test
    public void testGenericItemFactory() {
        ItemFactoryInterface factory = FactoryProvider.getFactoryByType("GenericItem");
        assertEquals(true, factory instanceof GenericItemFactory);
    }

    @Test
    public void testPotionFactory() {
        ItemFactoryInterface factory = FactoryProvider.getFactoryByType("Potion");
        assertEquals(true, factory instanceof PotionFactory);
    }

    @Test
    public void testWeaponFactory() {
        ItemFactoryInterface factory = FactoryProvider.getFactoryByType("Weapon");
        assertEquals(true, factory instanceof WeaponFactory);
    }
}
