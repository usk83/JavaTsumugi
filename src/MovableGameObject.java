import java.awt.*;
import javax.swing.*;

public abstract class MovableGameObject extends GameObject {
    protected float speed;
    protected float vx, vy;
    protected boolean onGround; // 地面に接しているか
    protected boolean isForward; // 正面を向いているか

    protected int objDire;

    protected GameManager gameManager;

    public MovableGameObject(float _px, float _py, String _path, int _numOfSprites) {
        super(_px, _py, _path, _numOfSprites);
        speed = 0;
        vx = 0;
        vy = 0;
        onGround = false;
        isForward = true;
        gameManager = GameManager.getInstance();
    }

    public void move() {
        addGravity();

        // x方向の当たり判定
        Float newPx = px + vx;
        // 移動先座標で衝突するタイルの位置を取得
        Point tile = gameManager.getTileCollision(this, newPx, py);
        // 衝突するタイルがなければ移動
        if (tile == null) {
            px = newPx;
        }
        // 衝突するタイルがある場合
        else {
            // 右へ移動中なので右のブロックと衝突
            if (vx > 0) {
                px = Map.tilesToPixels(tile.x) - width;
            }
            // 左へ移動中なので左のブロックと衝突
            else if (vx < 0) {
                px = Map.tilesToPixels(tile.x + 1);
            }
            vx = 0;
        }

        // y方向の当たり判定
        Float newPy = py + vy;
        // 移動先座標で衝突するタイルの位置を取得
        tile = gameManager.getTileCollision(this, px, newPy);
        // 衝突するタイルがなければ移動
        if (tile == null) {
            py = newPy;
            onGround = false; // 衝突してないということは空中
        }
        // 衝突するタイルがある場合
        else {
            // 下へ移動中なので下のブロックと衝突（着地）
            if (vy > 0) {
                py = Map.tilesToPixels(tile.y) - height;
                vy = 0;
                onGround = true;
            }
            // 上へ移動中なので上のブロックと衝突
            else if (vy < 0) {
                py = Map.tilesToPixels(tile.y + 1);
                vy = 0;
                //アタマに当たった時の操作(コインブロック用)
                headCollision(tile);
            }
        }
    }

    protected void headCollision(Point tile){
    }

    private void addGravity() {
        vy += GameManager.gravity;
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        if (isForward) {
            xs = 0;
            xe = imgWidth;
        }
        else {
            xs = imgWidth;
            xe = 0;
        }

        super.draw(g, offsetX, offsetY);
    }
}
