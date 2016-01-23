import java.util.*;
import java.awt.Graphics;

public class GameManager {

    private static final GameManager instance = new GameManager();

    private List<GameObject> gameObjects;
    GameObject mario;

    private GameManager() {
        gameObjects = new ArrayList<>();
        gameObjects.add(new Mario(320, 240, 1, 2));
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void update() {
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).move();
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).draw(g);
        }
    }
}
