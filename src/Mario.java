import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Mario extends GameObject {
    private int vx, vy;

    public Mario(int _px, int _py, int _vx, int _vy) {
        super(_px, _py);
        this.vx = _vx;
        this.vy = _vy;
        scale = 3;
        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource("res/mario/01.png"));
        icon = ii.getImage();
        size = icon.getWidth(null) * scale;
        icon = icon.getScaledInstance(size, -1, Image.SCALE_FAST);
    }

    public void move() {
        px += vx;
        py += vy;

        if (px < 0) {
            px = 0;
            vx = -vx;
        }

        if (px > MainPanel.WIDTH - size) {
            px = MainPanel.WIDTH - size;
            vx = -vx;
        }

        if (py < 0) {
            py = 0;
            vy = -vy;
        }

        if (py > MainPanel.HEIGHT - size) {
            py = MainPanel.HEIGHT - size;
            vy = -vy;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(icon, px, py, null);
    }
}
