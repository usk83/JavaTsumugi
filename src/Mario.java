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
    private static final int SPEED = 9;
    private static final int JUMP_SPEED = -18;

    private AudioClip jumpSound;

    public Mario(float _px, float _py) {
        super(_px, _py, "res/mario/01.png");

        jumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/jump.wav"));
    }

    public void move() {
        super.move();

        if (onGround) {
            jumpSound.stop();
        }
        vx = 0;
    }

    private void jump() {
        if (onGround) {
            onGround = false;
            vy = JUMP_SPEED;
            jumpSound.play();
        }
    }

    public void keyAction(HashMap<Integer, Boolean> keys) {
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
