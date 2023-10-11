package rpggame.Factories;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class WeaponFactory implements ItemFactoryInterface{
  public ItemInterface createItem(String[] itemProps) {
    return new WeaponItem(itemProps);
  }
}
