package rpggame.Factories;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class AmmoFactory implements ItemFactoryInterface{
  public ItemInterface createItem(String[] itemProps) {
    return new AmmoItem(itemProps);
  }
}
