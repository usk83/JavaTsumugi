import java.awt.*;
import javax.swing.*;

public class MovableGameObject extends GameObject {
    private static final float GRAVITY = 1.0f;
    protected float speed;
    protected float vx, vy;

    public MovableGameObject(float _px, float _py, String _path) {
        super(_px, _py, _path);
        speed = 0;
        vx = 0;
        vy = 0;
    }

    public void move() {
        super.move();
        addGravity();

        px += vx;
        py += vy;

        // 画面左
        if (px < 0) {
            px = 0;
        }

        // 画面右
        if (px > MainPanel.WIDTH - MainPanel.TILE_SIZE) {
            px = MainPanel.WIDTH - MainPanel.TILE_SIZE;
        }

        // 画面上
        if (py < 0) {
            py = 0;
        }

        // 画面下
        if (py > MainPanel.HEIGHT - MainPanel.TILE_SIZE) {
            py = MainPanel.HEIGHT - MainPanel.TILE_SIZE;
            vy = 0;
        }
    }

    private void addGravity() {
        vy += GRAVITY;
    }
}
