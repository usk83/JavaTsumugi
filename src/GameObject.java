import java.awt.*;
import javax.swing.*;

public abstract class GameObject {
    protected int width;
    protected int height;
    protected int imgWidth;
    protected int imgHeight;
    protected float px, py;
    protected Image icon;
    protected int xs, xe; // 画像表示時の画像の始点x座標と終点x座標
    protected int numOfSprites; // 画像あたりのスプライト数

    protected int iconCount; // アニメーション用カウンタ
    protected int animWait; // アニメーション用スリープ時間

    public GameObject(float _px, float _py, String path, int _numOfSprites) {
        width = MainPanel.TILE_SIZE;
        height = MainPanel.TILE_SIZE;
        px = _px;
        py = _py;
        numOfSprites = _numOfSprites;
        loadImage(path);
        xs = 0;
        xe = imgWidth;;

        iconCount = 0;
        animWait = 200;

        // アニメーション用スレッドを開始
        AnimationThread thread = new AnimationThread();
        thread.start();
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        g.drawImage(icon,
                    (int)px + offsetX, (int)py + offsetY,
                    (int)px + offsetX + width, (int)py + offsetY + height,
                    iconCount * imgWidth + xs, 0,
                    iconCount * imgWidth + xe, imgHeight,
                    null);
    }

    protected void loadImage(String path) {
        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(path));
        icon = ii.getImage();
        imgWidth = icon.getWidth(null) / numOfSprites;
        imgHeight = icon.getHeight(null);
    }

    protected void runAnimation() {
        iconCount += 1;
        if (iconCount >= numOfSprites) {
            iconCount = 0;
        }
    }

    public float getPx(){
      return px;
    }

    public float getPy(){
      return py;
    }

    public int getWidth(){
      return width;
    }

    public int getHeight(){
      return height;
    }

    // アニメーション用スレッド
    protected class AnimationThread extends Thread {
        public void run() {
            while (true) {
                runAnimation();
                try {
                    Thread.sleep(animWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
