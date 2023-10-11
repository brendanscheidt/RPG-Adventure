package rpggame.Factories;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class PotionFactory implements ItemFactoryInterface{
  public ItemInterface createItem(String[] itemProps) {
    return new PotionItem(itemProps);
  }
}
