package rpggame.Items;

public class FoodItem extends Item{
  public int hpRegen;

  public FoodItem(String[] itemProps) {
    super(itemProps);
    this.type = itemProps[2];
    this.hpRegen = Integer.parseInt(itemProps[3]);
  }
}
