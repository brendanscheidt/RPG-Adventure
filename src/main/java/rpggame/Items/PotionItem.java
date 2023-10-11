package rpggame.Items;

public class PotionItem extends Item{
  public PotionItem(String[] itemProps) {
    super(itemProps);
    this.type = itemProps[2];
  }
}
