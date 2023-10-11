package rpggame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {

    private GameEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new GameEngine("ItemDatabase.csv");
        engine.initialize();
    }

    @Test
    public void testInitialization() {
        assertNotNull(engine);
        // TODO: Add more assertions to check initial state of the engine
    }

    // TODO: Add more test methods as needed

}
