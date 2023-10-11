package rpggame.Items;

public class WeaponItem extends Item {
  public WeaponItem(String[] itemProps) {
    super(itemProps);
    this.type = itemProps[2];
  }
}
