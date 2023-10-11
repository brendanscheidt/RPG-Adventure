package rpggame;

import rpggame.Interfaces.*;
import rpggame.Items.*;

import java.util.ArrayList;
import java.util.List;

public class Store implements EntityInterface{
  private List<ItemInterface> inventory;
  private List<EntityInterface> players_in_store;
  private Double money;
  GameUIInterface ui = SingletonUI.getUI();              

  public Store() {
    inventory = new ArrayList<>();
    players_in_store = new ArrayList<>(); 
    this.money = 1000.0;           
  }

  public void enter(EntityInterface player){
    if (check_player_in_store(player) == false)
      players_in_store.add(player);             
    else
      ui.displayMessage("Player is already in the store.");
  }

  public void exit(EntityInterface player){
    if (check_player_in_store(player) == true) 
      players_in_store.remove(player);
    else
      ui.displayMessage("Player never entered the store.");
  }

  public boolean addMoney(Double amount) {
    this.money += amount;
    return true;
  }

  public boolean removeMoney(Double amount) {
    this.money -= amount;
    return true;
  }

  public void buy(ItemInterface item) {
    aquire(item);
    removeMoney(item.getPrice());
  }

  public void sell(ItemInterface item) {
    relinquish(item);
    addMoney(item.getPrice());
  }

  public void aquire(ItemInterface item) {
    inventory.add(item);
  }

  public void relinquish(ItemInterface item) {
    inventory.remove(item);
  }

  public void addItem(Item item) {                    
    aquire(item);
  }

  public void removeItem(Item item) {               
    relinquish(item);
  }

  private boolean check_player_in_store(EntityInterface player){
    int index =  players_in_store.indexOf(player);
    if (index == -1) {
      return false;
    } else {                            
      return true;
    }
  }

  public ItemInterface getItemByName(String name) {
    // Iterate through the player's items and return the item with the matching name
    for (ItemInterface item : inventory) {
      if (item.getName().equalsIgnoreCase(name)) {           
        return item;
      }
    }
    return null; // Item not found in the player's inventory
  }

  public boolean buy(ItemInterface item, EntityInterface player) {
    if (check_player_in_store(player) == false){
      ui.displayMessage("Player needs to enter the store before being able to buy anything");
      return false;
    }
        
    if (inventory.contains(item)) {
      if (player.removeMoney(item.getPrice())) {
        inventory.remove(item);                            
        player.aquire(item);
        return true;
      }
    } else {
      ui.displayMessage("Item not available in the store.");
    }
    return false;
  }

  public boolean sell(ItemInterface item, EntityInterface player) {
    if (check_player_in_store(player) == false){
      ui.displayMessage("Player needs to enter the store before being able to sell anything");
      return false;
    }
    player.relinquish(item);                       
    player.addMoney(item.getPrice());
    inventory.add(item);
    return true;
  }

  public void equip(ItemInterface item) {
    aquire(item);
  }

  public void useEquippedItem() {

  }

  public boolean buyItem(ItemInterface item, EntityInterface player) {
    return buy(item, player);
  }

  public boolean sellItem(ItemInterface item, EntityInterface player) {
    return sell(item, player);
  }

  public List<ItemInterface> exposeInventory() {
    return this.inventory;
  }
}
