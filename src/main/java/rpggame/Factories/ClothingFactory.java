package rpggame.Factories;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class ClothingFactory implements ItemFactoryInterface {
  public ItemInterface createItem(String[] itemProps) {
    return new ClothingItem(itemProps);
  }
}
