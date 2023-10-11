package rpggame.Items;

public class AmmoItem extends Item{
  public AmmoItem(String[] itemProps) {
    super(itemProps);
    this.type = itemProps[2];
  }
}
