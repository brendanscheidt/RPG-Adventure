package rpggame;

import rpggame.Interfaces.*;
import rpggame.Items.*;

import java.util.ArrayList;
import java.util.List;

public class Player implements EntityInterface{
  //Double wrapper has concept of infinity
  private Double money;
  private ItemInterface equipedItem;
  private List<ItemInterface> inventory;
  private List<ItemInterface> clothing;
  private List<ItemInterface> currencyPouch;
  private List<FoodItem> consumableBag;
  private int health;
  GameUIInterface ui;
  
  public Player(double money) {
    //All items in clothing, and consumableBag are also in inventory. currencyPouch is seperate from inventory.
    ui = SingletonUI.getUI();
    this.money = money;
    this.health = 30;
    equipedItem = null;
    clothing = new ArrayList<>();
    inventory = new ArrayList<>();
    currencyPouch = new ArrayList<>();
    consumableBag = new ArrayList<>();
    currencyPouch.add(new Item(new String[] {"money", ""+money}));
  } 

  public void enter(EntityInterface store) {
    store.enter(this);
  }

  public void exit(EntityInterface store) {
    store.exit(this);
  }

  public Double wealth() {
    return money;
  }

  public boolean addMoney(Double amount) {
    //Money is an item with a price equal to the amount of money. it is stored in my currencyPouch currently.
    //Also check if amount of money in currencyPouch is infinity.
    if(Double.isInfinite(money + amount)) return false;

    money += amount;
    currencyPouch.set(0, new Item(new String[] {"money", ""+money}));
    return true;
  }

  public boolean removeMoney(Double amount) {
    //replaces the money item in currency pouch with amount removed.
    if(money - amount < 0) return false;

    money -= amount;
    currencyPouch.set(0, new Item(new String[] {"money", ""+money}));
    return true;
  }

  public void spend(double amount) {
    removeMoney(amount);
  } 

  public void get(double amount) {
    addMoney(amount);
  }

  public boolean buy(ItemInterface item, EntityInterface entity) {
    //calls on aquire in case aquire gets side effects added to it when buying
    if(removeMoney(item.getPrice())) {
        aquire(item);
        entity.addMoney(item.getPrice());
        return true;
    }
    return false;
  }

  public boolean sell(ItemInterface item, EntityInterface entity) {
    //calls on relinquish in case relinquish has side effects in future when getting rid of an item
    relinquish(item);
    entity.aquire(item);
    return addMoney(item.getPrice());  
  }

  public void equip(ItemInterface itemToEquip) {
    //ui.displayMessage("Player equiped " + itemToEquip.getName());
    equipedItem = itemToEquip;
  }

  public void consume(FoodItem itemToConsume) {
    //equips food or drink and removes item from inventory and consumableBag. add health or hunger later?
    if(!inventory.contains(itemToConsume)) {
      throw new IllegalArgumentException();
    }

    if(equipedItem != null && equipedItem.getName().equals(itemToConsume.getName())) {
      equipedItem = null;
    }

    ui.displayMessage("Player consumed " + itemToConsume.name);
    regenHealth(itemToConsume.hpRegen);
    ui.displayMessage("Player health restored to " + health);
    consumableBag.remove(itemToConsume);
    inventory.remove(itemToConsume);
  }

  private void regenHealth(int healthToRegen) {
    if(health + healthToRegen >= 100) {
      health = 100;
      return;
    }
    health += healthToRegen;
  }

  public void equipItem(ItemInterface itemToEquip) {
    equip(itemToEquip);
  }

  public void useEquippedItem() {
    //Using item right now just prints player used item.
    if(equipedItem == null) ui.displayMessage("Player has no item equipped to use!");
    else {
      ui.displayMessage("Player used " + equipedItem.getName());
    }
  }

  public void wear(ItemInterface itemToWear) {
    //Should I equip() clothing? only one item can be equiped and i want to have multiple clothes.
    if(!(itemToWear instanceof ClothingItem) || !inventory.contains(itemToWear)) {
      throw new IllegalArgumentException();
    }

    if(clothing.contains(itemToWear)) {
      ui.displayMessage("Player is already wearing that!");
    }
    else {
        clothing.add(itemToWear);
        ui.displayMessage("Player is now wearing " + itemToWear.getName());
    }    
  }

  public void hold(ItemInterface itemToHold) {
    equip(itemToHold);
  }

  public void eat(ItemInterface itemToEat) {
    consume((FoodItem) itemToEat);
  }

  public void drink(ItemInterface itemToDrink) {
    consume((FoodItem) itemToDrink);
  }

  public List<ItemInterface> getInventory() {
    return exposeInventory();
  }

  public void removeItem(ItemInterface item) {
    relinquish(item);
  }

  public void aquire(ItemInterface itemAquired) {
    //TODO: add functionality to store aquired item in correct storage place along with inventory
    if(itemAquired == null) {
      throw new NullPointerException();
    }

    if(itemAquired instanceof FoodItem) {
      consumableBag.add((FoodItem) itemAquired);
    }
    inventory.add(itemAquired);    
  }

  public void relinquish(ItemInterface itemToRelinquish) {
    //avoid keeping item equipped after selling  
    if(itemToRelinquish == null) {
      throw new NullPointerException();
    }
    
    if(equipedItem != null && itemToRelinquish.getName().equalsIgnoreCase(equipedItem.getName())) {
      equipedItem = null;
    }   
    //removes item from inventory and any other bags it may be stored in.
    if(inventory.contains(itemToRelinquish)) {
      inventory.remove(itemToRelinquish);
      consumableBag.remove(itemToRelinquish);
      currencyPouch.remove(itemToRelinquish);
      clothing.remove(itemToRelinquish);
    }
  }

  public void addItem(ItemInterface item) {
    aquire(item);
  }

  public List<ItemInterface> exposeInventory() {
    return inventory;
  }

  public List<ItemInterface> exposeWearInventory() {
    return clothing;
  }

  public List<FoodItem> exposeFoodInventory() {
    return consumableBag;
  }

  public List<ItemInterface> exposeCurrencyInventory() {
    return currencyPouch;
  }

  public ItemInterface getItemByName(String itemName) {
    int i = 0;
    for(ItemInterface item : inventory) {
      if(item.getName().equalsIgnoreCase(itemName)) {
        break;
      }
      i++;
    }
    return (i == inventory.size()) ? null : inventory.get(i);
  }

  public int getHealth() {
    return health;
  }

  public void setUI(GameUIInterface ui) {
    this.ui = ui;
  }

  public ItemInterface getEquipedItem() {
    return this.equipedItem;
  }

}
