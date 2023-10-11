package rpggame.Interfaces;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public interface DatabaseInterface {
  public Scanner parseFile(String filename) throws FileNotFoundException;
  public ArrayList<ItemInterface> getDatabase();
}
