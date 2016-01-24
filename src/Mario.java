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
        super(_px, _py, "res/mario/01.png");
        speed = _speed;

        jumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/jump.wav"));
        isJumping = false;
    }

    public void move() {
        super.move();

        vx = 0;
    }

    private void jump() {
        if (!isJumping) {
            isJumping = true;
            vy = -18;
            jumpSound.play();
        }
    }

    public void keyAction(HashMap<Integer, Boolean> keys) {
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
            if (py >= MainPanel.HEIGHT - MainPanel.TILE_SIZE) {
                isJumping = false;
                jumpSound.stop();
            }
        }
    }
}
