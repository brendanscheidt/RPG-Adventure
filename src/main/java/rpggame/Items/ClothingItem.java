package rpggame.Items;

public class ClothingItem extends Item {
  public ClothingItem(String[] itemProps) {
    super(itemProps);
    this.type = itemProps[2];
  }
}
