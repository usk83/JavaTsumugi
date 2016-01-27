import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class EditorPanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final int TILE_SIZE = 32;

    private EditorManager editorManager;

    // ダブルバッファリング（db）用
    private Graphics dbg;
    private Image dbImage = null;

    private Thread gameLoop; // ゲームループ

    public EditorPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        editorManager = EditorManager.getInstance();
        editorManager.init();

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
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
        editorManager.update();
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

        editorManager.render(dbg);
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

    /**
     * キーがタイプされたとき
     */
    public void keyTyped(KeyEvent e) {
        editorManager.keyTyped(e);
    }

    /**
     * キーが押されたとき
     */
    public void keyPressed(KeyEvent e) {
        editorManager.keyPressed(e);
    }

    /**
     * キーが離されたとき
     */
    public void keyReleased(KeyEvent e) {
        editorManager.keyReleased(e);
    }

    /**
     * マウス操作関連
     */
    public void mouseEntered(MouseEvent e){
    }

    public void mouseExited(MouseEvent e){
    }

    public void mouseClicked(MouseEvent e) {
        editorManager.mouseClicked(e);
    }

    public void mousePressed(MouseEvent e){
        editorManager.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e){
        editorManager.mouseReleased(e);
    }

    public void mouseDragged(MouseEvent e) {
        editorManager.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
    }
}
