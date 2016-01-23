import java.applet.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Mario extends MovableGameObject {
    private AudioClip jumpSound;

    private Boolean isJumping;

    public Mario(float _px, float _py, float _speed) {
        super(_px, _py);
        speed = _speed;
        scale = 3;
        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource("res/mario/01.png"));
        icon = ii.getImage();
        size = icon.getWidth(null) * scale;
        icon = icon.getScaledInstance(size, -1, Image.SCALE_FAST);

        jumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/jump.wav"));
        isJumping = false;
    }

    public void move() {
        super.move();

        vx = 0;
    }

    public void draw(Graphics g) {
        super.draw(g);
        g.drawImage(icon, (int)px, (int)py, null);
    }

    private void jump() {
        if (!isJumping) {
            isJumping = true;
            vy = -18;
            jumpSound.play();
        }
    }

    public void updateKeys(HashMap<Integer, Boolean> keys) {
        if (keys.get(KeyEvent.VK_LEFT)) {
            vx = -1 * speed;
        }
        else if (keys.get(KeyEvent.VK_RIGHT)) {
            vx = speed;
        }

        if (keys.get(KeyEvent.VK_SPACE)) {
            jump();
        }
        else {
            // 画面下
            if (py >= MainPanel.HEIGHT - size) {
                isJumping = false;
                jumpSound.stop();
            }
        }
    }
}
