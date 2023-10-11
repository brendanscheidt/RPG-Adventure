package rpggame;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import rpggame.Interfaces.*;

public class CSVDatabase implements DatabaseInterface {
  ItemFactoryInterface factoryProvider;
  ArrayList<ItemInterface> databaseItems;
  Scanner filescnr;
  GameUIInterface ui = SingletonUI.getUI();

  public CSVDatabase(String filename) {

    this.databaseItems = new ArrayList<>();
    this.filescnr = parseFile(filename);
    buildItemList(filescnr);
  }

  public Scanner parseFile(String filename) {
    InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
    if (is == null) {
      ui.displayMessage("Error reading the file. Program terminating!...");
      System.exit(0);
    }
    this.filescnr = new Scanner(is);
    return filescnr;
  }

  public ArrayList<ItemInterface> getDatabase() {
    return this.databaseItems;
  }

  private void buildItemList(Scanner fileScnr) {
    //Throw away headers
    if(fileScnr.hasNextLine()) {
      fileScnr.nextLine();
    }

    while(fileScnr.hasNextLine()) {
      try {
        String line = fileScnr.nextLine();
        String[] newItemProps = line.split(",");
        String itemType = newItemProps[2];
        factoryProvider = FactoryProvider.getFactoryByType(itemType);
        ItemInterface newItem = factoryProvider.createItem(newItemProps);
        databaseItems.add(newItem); 
      } catch (Exception e) {
        ui.displayMessage("Error building item database. Program terminating!...");
        System.exit(0);
      }   
    }
  }
}
