import java.applet.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Kuribo extends MovableGameObject {
  //スピード
  private static final int SPEED = 8;
  public Kuribo(float _px, float _py ){
    super(_px, _py, "res/enemy/kuribo.png", 2);

    iconCount = 1;  //アニメ用カウンタ
    animWait = 50;  //アニメ用スリープ時間
  }
  public void move() {

      super.move();
      vx = -SPEED;
      vy = 0;
  }


}
