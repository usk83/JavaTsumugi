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
    private static final int SPEED = 8;
    private static final int JUMP_SPEED = -18;

    private AudioClip jumpSound;

    private int anim_count; // アニメーション用カウンタ
    private int anim_wait; // アニメーション用スリープ時間

    public Mario(float _px, float _py) {
        super(_px, _py, "res/mario/mario.png");
        imgWidth = imgWidth / 14;

        jumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/jump.wav"));
        anim_count = 1;
        anim_wait = 50;

        // アニメーション用スレッドを開始
        AnimationThread thread = new AnimationThread();
        thread.start();
    }

    public void move() {

        super.move();

        if (onGround) {
            jumpSound.stop();
        }
        vx = 0;
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        g.drawImage(icon,
                    (int)px + offsetX, (int)py + offsetY,
                    (int)px + offsetX + width, (int)py + offsetY + height,
                    anim_count * imgWidth, 0,
                    anim_count * imgWidth + imgWidth, imgHeight,
                    null);
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

    // アニメーション用スレッド
    private class AnimationThread extends Thread {
        public void run() {
            while (true) {
                anim_count++;
                if (anim_count > 3) {
                    anim_count = 1;
                }

                try {
                    Thread.sleep(anim_wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
