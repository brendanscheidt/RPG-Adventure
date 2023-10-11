package rpggame.Items;

public class BuffItem extends Item{
  public BuffItem(String[] itemProps) {
    super(itemProps);
    this.type = itemProps[2];
  }
}
