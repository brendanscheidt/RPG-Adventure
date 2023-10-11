package rpggame.Factories;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class GenericItemFactory implements ItemFactoryInterface{
  public ItemInterface createItem(String[] itemProps) {
    return new Item(itemProps);
  }
}
