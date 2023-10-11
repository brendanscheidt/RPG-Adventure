package rpggame.Interfaces;
import java.util.List;

public interface EntityInterface {
  public void enter(EntityInterface store);
  public void exit(EntityInterface store);
  public boolean buy(ItemInterface item, EntityInterface entity);
  public boolean sell(ItemInterface item, EntityInterface entity);
  public ItemInterface getItemByName(String itemName);
  public void aquire(ItemInterface itemAquired);
  public void relinquish(ItemInterface itemToRelinquish);
  public List<ItemInterface> exposeInventory();
  public void equip(ItemInterface item);
  public void useEquippedItem();
  public boolean removeMoney(Double amount);
  public boolean addMoney(Double amount);
}
