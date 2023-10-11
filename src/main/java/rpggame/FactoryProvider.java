package rpggame;

import rpggame.Factories.*;
import rpggame.Interfaces.ItemFactoryInterface;

public class FactoryProvider {
  public static ItemFactoryInterface getFactoryByType(String type) {
    switch(type.toLowerCase()) {
      case "clothing":
        return new ClothingFactory();
      case "weapon":
        return new WeaponFactory();
      case "food":
        return new FoodFactory();
      case "potion":
        return new PotionFactory();
      case "buff":
        return new BuffFactory();
      case "ammo":
        return new AmmoFactory();
      default:
        return new GenericItemFactory();
    }
  }
}
