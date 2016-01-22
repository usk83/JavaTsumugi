import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

/*
 * 参考 <http://aidiary.hatenablog.com/entry/20060507/1251462851>
 */

public class MainPanel extends JPanel implements Runnable {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 320;

    private GameObject mario;

    // ダブルバッファリング（db）用
    private Graphics dbg;
    private Image dbImage = null;

    private Thread gameLoop; // ゲームループ

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        mario = new Mario(320, 240, 1, 2);

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
            gameUpdate(); // ゲーム状態を更新
            gameRender(); // バッファにレンダリング
            paintScreen(); // バッファを画面に描画

            // 休止
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ゲーム状態を更新
     */
    private void gameUpdate() {
        mario.move();
    }

    /**
     * バッファにレンダリング（ダブルバッファリング）
     */
    private void gameRender() {
        // 初回の呼び出し時にダブルバッファリング用オブジェクトを作成
        if (dbImage == null) {
            // バッファイメージ
            dbImage = createImage(WIDTH, HEIGHT);

            if (dbImage == null) {
                // dbImageが作られるまでは何もしない
                return;
            }
            else {
                // バッファイメージの描画オブジェクト
                dbg = dbImage.getGraphics();
            }
        }

        // バッファをクリアする
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, WIDTH, HEIGHT);

        // ボールをバッファへ描画する
        mario.draw(dbg);
    }

    /**
     * バッファを画面に描画
     */
    private void paintScreen() {
        try {
            Graphics g = getGraphics(); // グラフィックオブジェクトを取得
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null); // バッファイメージを画面に描画
            }
            Toolkit.getDefaultToolkit().sync();
            if (g != null) {
                g.dispose(); // グラフィックオブジェクトを破棄
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
