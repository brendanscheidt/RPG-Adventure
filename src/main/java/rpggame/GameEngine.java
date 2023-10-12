package rpggame;

import java.util.ArrayList;
import java.util.List;

import rpggame.Interfaces.*;
import rpggame.Items.*;

public class GameEngine {

  GameUIInterface ui;
  DatabaseInterface csvItemDatabase;
  ArrayList<ItemInterface> gameItems;
  EntityInterface player;
  EntityInterface store;

  public GameEngine(String filename) {
    this.ui = new ConsoleUI();
    SingletonUI.setUI(ui);
    this.csvItemDatabase = new CSVDatabase(filename);  
  }

  public void initialize() {
    this.gameItems = csvItemDatabase.getDatabase();
    this.player = new Player(100.0);
    this.store = new Store();
    int randomInt = (int) (Math.random() * gameItems.size());
    int itemCount = 0;
    while(itemCount < 5) {
      store.aquire(gameItems.get(randomInt));
      randomInt = (int) (Math.random() * gameItems.size());
      itemCount++;
    }
  }

  public void run() {
    

    while(true) {
      ui.displayInventory(player);
      ui.displayMessage("\nEnter a command (1 to enter the store, 2 to equip an item, 3 to use equipped item, 4 to eat food, 5 to view worn clothing, 6 to exit):");
      String input = ui.getUserInput();

      if (input.equals("1")) {
        store.enter(player);
        storeMenu(store, player);
        store.exit(player);
      } else if (input.equals("2")) {
        equipItemMenu(player);
      } else if (input.equals("3")) {
        player.useEquippedItem();
      } else if (input.equals("4")) {
        eatFoodMenu(player);
      } else if (input.equals("5")) {
        clothingMenu(player);
      }else if (input.equals("6")) {
        ui.displayMessage("Exiting the program...");
        break;
      } else {
        ui.displayMessage("Invalid command!");
      }
    }
  }

  private void storeMenu(EntityInterface store, EntityInterface player) {
    while (true) {
      ui.displayMessage("\nStore Menu:");
      ui.displayMessage("1. Buy an item");
      ui.displayMessage("2. Sell an item");
      ui.displayMessage("3. Display inventory");
      ui.displayMessage("4. Restock Store Inventory");
      ui.displayMessage("5. Exit store");

      String input = ui.getUserInput();

      if (input.equals("1")) {
        ui.displayInventory(store);
        ui.displayMessage("Enter the name of the item you want to buy:");
        String itemName = ui.getUserInput();
        ItemInterface item = store.getItemByName(itemName);
        if (item != null) {
          if (store.buy(item, player)){
            ui.displayMessage("Item purchased successfully!");
          } else {
            ui.displayMessage("Could not purchase the desired item.");
          }
        } else {
          ui.displayMessage("Item not available in the store.");
        }
      } else if (input.equals("2")) {
        ui.displayInventory(player);
        ui.displayMessage("\nEnter the name of the item you want to sell:");
        String itemName = ui.getUserInput();
        ItemInterface item = player.getItemByName(itemName);
        if (item != null) {
          store.sell(item, player);
          ui.displayMessage("Item sold successfully!");
        } else {
          ui.displayMessage("Item not found in your inventory.");
        }
        } else if (input.equals("3")) {
          ui.displayInventory(store);
        } else if(input.equals("4")) {
          restockInventory(store);
          ui.displayInventory(store);
        } else if (input.equals("5")) {
          ui.displayMessage("Exiting the store...");
          break;
        } else {
          ui.displayMessage("Invalid command!");
        }
    }
  }

  private void eatFoodMenu(EntityInterface player) {
    Player currentPlayer = (Player) player;
    List<FoodItem> foodInventory = currentPlayer.exposeFoodInventory();
    
    ui.displayMessage("\nCurrent Player Health: " + currentPlayer.getHealth());
    ui.displayMessage("Items in food bag: ");

    if(foodInventory.size() == 0) {
      ui.displayMessage("Food bag is empty!");
      return;
    }
    for(FoodItem food : foodInventory) {
      ui.displayMessage(food.getName() + ": +" + food.hpRegen);
    }

    ui.displayMessage("\nEnter the name of the food you wish to eat:");
    String foodNameInput = ui.getUserInput();
    FoodItem food = (FoodItem) currentPlayer.getItemByName(foodNameInput);

    if(food != null) {
      currentPlayer.consume(food);
    }
    else {
      ui.displayMessage("\nEnter valid food name!");
    }
  }

  private void clothingMenu(EntityInterface player) {
    Player currentPlayer = (Player) player;
    List<ItemInterface> playerClothing = currentPlayer.exposeWearInventory();

    if(playerClothing.size() == 0) {
      ui.displayMessage("Player is wearing no clothing!");
      return;
    }

    ui.displayMessage("\nCurrently worn clothing: ");
    for(ItemInterface clothing : playerClothing) {
      ui.displayMessage(clothing.getName());
    }
  }

  private void equipItemMenu(EntityInterface entity) {
    Player player = (Player) entity;

    ui.displayMessage("\nPlayer's Inventory:");
    List<ItemInterface> inventory = entity.exposeInventory();
    if (inventory.isEmpty()) {
      ui.displayMessage("Inventory is empty.");
      return;
    }
    int i = 1;
    for (ItemInterface item : inventory) {
      ui.displayMessage(i + ". " + item.getName());
      i++;
    }
    ui.displayMessage("Enter the number of the item you want to equip:");
    int choice = 0;

    try{
      choice = Integer.parseInt(ui.getUserInput());
    } catch (NumberFormatException e) {
      ui.displayMessage("Please input a number!");
    }

    if (choice < 1 || choice > inventory.size()) {
      ui.displayMessage("Invalid choice!");
    } else {
      ItemInterface chosenItem = inventory.get(choice - 1);
      if(chosenItem instanceof ClothingItem) {
        player.wear(chosenItem);
        return;
      }
      player.equip(chosenItem);
      ui.displayMessage("Player equipped " + chosenItem.getName());
    }
  }

  private void restockInventory(EntityInterface entity) {
    List<ItemInterface> inventory = entity.exposeInventory();
    int randomInt = (int) (Math.random() * gameItems.size());
    int itemCount = 0;
    int i;

    for(i = inventory.size() - 1; i >= 0; i--) {
      entity.relinquish(inventory.get(i));
    }
    while(itemCount < 5) {
      store.aquire(gameItems.get(randomInt));
      randomInt = (int) (Math.random() * gameItems.size());
      itemCount++;
    }
  }

  public List<ItemInterface> getPlayerInventory() {
    return player.exposeInventory();
  }

  public Double getPlayerMoney() {
    Player playerP = (Player) player;
    return playerP.wealth();
  }

  public void setUI(GameUIInterface ui) {
    this.ui = ui;
  }

  public void setPlayer(EntityInterface player) {
    this.player = player;
  }

}
