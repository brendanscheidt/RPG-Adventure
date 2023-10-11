package rpggame;

public class Main {
    public static GameEngine engine = new GameEngine("ItemDatabase.csv");
    public static void main(String[] args) {
        engine.initialize();
        engine.run();
    }
}
