package rpggame.Factories;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class BuffFactory implements ItemFactoryInterface{
  public ItemInterface createItem(String[] itemProps) {
    return new BuffItem(itemProps);
  }
}
