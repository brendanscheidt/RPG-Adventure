package rpggame.Factories;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class FoodFactory implements ItemFactoryInterface{
  public ItemInterface createItem(String[] itemProps) {
    return new FoodItem(itemProps);
  }
}
