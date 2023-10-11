package rpggame;

import rpggame.Interfaces.GameUIInterface;

public class SingletonUI {
    private static GameUIInterface instance;

    private SingletonUI() {}

    public static void setUI(GameUIInterface ui) {
        if (instance == null) {
            instance = ui;
        }
    }

    public static GameUIInterface getUI() {
        return instance;
    }
}
