package rpggame.Items;
import rpggame.Interfaces.ItemInterface;

public class Item implements ItemInterface {
  public String name;
  public double price;
  public String type = "Generic";

  public Item(String[] itemProps) {
    this.name = itemProps[0];
    this.price = Double.parseDouble(itemProps[1]);
  }

  public String getName() {
    return name;                            
  }

  public double getPrice() {
    return price;
  }

  public String getType() {
    return this.type;
  }
}
