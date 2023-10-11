package rpggame.Interfaces;
public interface GameUIInterface {
  public void displayMessage(String message);
  public void displayInventory(EntityInterface entity);
  public String getUserInput();
}
