package rpggame;

import java.util.List;
import java.util.Scanner;

import rpggame.Interfaces.*;

public class ConsoleUI implements GameUIInterface {
  Scanner userInputScnr = new Scanner(System.in);

  public void displayMessage(String message) {
    System.out.println(message);
  }

  public void displayInventory(EntityInterface entity) {
    List<ItemInterface> inventory = entity.exposeInventory();

    if(entity instanceof Store) {
      System.out.println("\nStore Inventory:");
      if(inventory.isEmpty()) {
        System.out.println("Store inventory is empty.\n");
      }
      for (ItemInterface item : inventory) {
        String itemInfo = item.getName() + " - $" + item.getPrice();
        int numSpaces = 30 - itemInfo.length();
        itemInfo = addSpaces(itemInfo, numSpaces);
        itemInfo += "~~(" + item.getType() + ")";       
        System.out.println(itemInfo);
      }
    }
    if(entity instanceof Player) {
      System.out.println("\nPlayer Inventory:");
      if(inventory.size() == 0) {
        System.out.println("Player inventory is empty.");
      } else {
        for (ItemInterface item : inventory) {
          String itemInfo = item.getName();
          int numSpaces = 15 - itemInfo.length();
          itemInfo = addSpaces(itemInfo, numSpaces);
          itemInfo += "~~(" + item.getType() + ")";       
          System.out.println(itemInfo);
      }
      }
    }
  }

  private String addSpaces(String str, int spaces) {
    for(int i = 0; i < spaces; i++) {
      str += " ";
    }

    return str;
  }

  public String getUserInput() {
    return userInputScnr.nextLine();
  }
}
