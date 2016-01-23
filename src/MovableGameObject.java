import java.awt.*;
import javax.swing.*;

public class MovableGameObject extends GameObject {
    private static final int GRAVITY = 1;
    protected int speed;
    protected int vx, vy;

    public MovableGameObject(int _px, int _py, int _speed) {
        super(_px, _py);
        speed = _speed;
        vx = 0;
        vy = 0;
    }

    public void move() {
        super.move();
        addGravity();

        px += vx;
        py += vy;

        // 画面上
        if (px < 0) {
            px = 0;
        }

        // 画面下
        if (px > MainPanel.WIDTH - size) {
            px = MainPanel.WIDTH - size;
            vy = 0;
        }

        // 画面左
        if (py < 0) {
            py = 0;
        }

        // 画面右
        if (py > MainPanel.HEIGHT - size) {
            py = MainPanel.HEIGHT - size;
            vy = 0;
        }
    }

    private void addGravity() {
        vy += GRAVITY;
    }
}
