import java.applet.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Point;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Mario extends MovableGameObject {
    private static final int SPEED = 8;
    private static final int JUMP_SPEED = -18;

    private AudioClip jumpSound;
    private AudioClip bumpSound;

    private boolean isStop; // 止まっているか

    public Mario(float _px, float _py) {
        super(_px, _py, "res/mario/mario.png", 14);

        jumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/Jump.wav"));
        bumpSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/Bump.wav"));
        iconCount = 1;
        animWait = 50;
        isStop = true;
    }

    public void move() {

        super.move();

        if (onGround) {
            jumpSound.stop();
        }
        vx = 0;
    }

    protected void headCollision(Point tile){
        gameManager.checkCoinBlock(tile);
    }

    public void jump() {
        if (onGround) {
            onGround = false;
            vy = JUMP_SPEED;
            iconCount = 5;
            jumpSound.play();
        }
    }

    public void reflectJump() {
        vy = JUMP_SPEED/2;
        iconCount = 5;
    }

    public void playBumpSound() {
        jumpSound.stop();
        bumpSound.play();
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

    public boolean isCollision(GameObject go) {
        Rectangle marioRect = new Rectangle((int)px, (int)py,
                                             width, height);
        Rectangle goRect = new Rectangle((int)go.getPx(),
                                         (int)go.getPy(),
                                              go.getWidth(),
                                              go.getHeight());
        // マリオの矩形と対象オブジェクトの矩形が重なっているか調べる
        if (marioRect.intersects(goRect)) {
            return true;
        }
        return false;
    }

    protected void runAnimation() {
        if (onGround) {
            if (!isStop) {
                iconCount += 1;
                if (iconCount > 3) {
                    iconCount = 1;
                }
            }
        }
        else {
            // ジャンプ中でなければ
            if (iconCount != 5) {
                iconCount = 3;
            }
        }
    }
}
