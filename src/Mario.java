import java.applet.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Mario extends GameObject {
    private static final int SPEED = 3;
    private int vx, vy;

    private AudioClip jumpSound;

    public Mario(int _px, int _py) {
        super(_px, _py);
        vx = 0;
        vy = 0;
        scale = 3;
        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource("res/mario/01.png"));
        icon = ii.getImage();
        size = icon.getWidth(null) * scale;
        icon = icon.getScaledInstance(size, -1, Image.SCALE_FAST);

        jumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/jump.wav"));
    }

    public void move() {
        px += vx;
        py += vy;

        vx = 0;
        vy = 0;
    }

    public void draw(Graphics g) {
        g.drawImage(icon, px, py, null);
    }

    private void jump() {
        jumpSound.play();
    }

    public void updateKeys(HashMap<Integer, Boolean> keys) {
        if (keys.get(KeyEvent.VK_LEFT)) {
            vx = -1 * SPEED;
        }
        else if (keys.get(KeyEvent.VK_RIGHT)) {
            vx = SPEED;
        }

        if (keys.get(KeyEvent.VK_SPACE)) {
            jump();
        }
    }
}
