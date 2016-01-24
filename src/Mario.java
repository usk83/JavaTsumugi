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

    private int iconCount; // アニメーション用カウンタ
    private int animWait; // アニメーション用スリープ時間

    private boolean isForward; // 正面を向いているか
    private boolean isStop; // 止まっているか

    public Mario(float _px, float _py) {
        super(_px, _py, "res/mario/mario.png");
        imgWidth = imgWidth / 14;

        jumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/jump.wav"));
        iconCount = 1;
        animWait = 50;
        isForward = true;
        isStop = true;

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
        int xs, xe;
        if (isForward) {
            xs = 0;
            xe = imgWidth;
        }
        else {
            xs = imgWidth;
            xe = 0;
        }

        g.drawImage(icon,
                    (int)px + offsetX, (int)py + offsetY,
                    (int)px + offsetX + width, (int)py + offsetY + height,
                    iconCount * imgWidth + xs, 0,
                    iconCount * imgWidth + xe, imgHeight,
                    null);
    }

    private void jump() {
        if (onGround) {
            onGround = false;
            vy = JUMP_SPEED;
            iconCount = 5;
            jumpSound.play();
        }
    }

    public void keyAction(HashMap<Integer, Integer> keys) {
        if (keys.get(KeyEvent.VK_LEFT) <= KeyStatus.PRESSING) {
            vx = -1 * SPEED;
            isForward = false;
            isStop = false;
        }
        else if (keys.get(KeyEvent.VK_RIGHT) <= KeyStatus.PRESSING) {
            vx = SPEED;
            isForward = true;
            isStop = false;
        }
        else {
            isStop = true;
            if (onGround) {
                iconCount = 0;
            }
        }

        if (keys.get(KeyEvent.VK_SPACE) == KeyStatus.PRESSED) {
            jump();
        }
    }

    // アニメーション用スレッド
    private class AnimationThread extends Thread {
        public void run() {
            while (true) {
                if (onGround) {
                    if (!isStop) {
                        iconCount += 1;
                        if (iconCount > 3) {
                            iconCount = 0;
                        }
                    }
                }
                else {
                    // ジャンプ中でなければ
                    if (iconCount != 5) {
                        iconCount = 3;
                    }
                }

                try {
                    Thread.sleep(animWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
