import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EditorPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {
    private JavaMario frame;

    public static final int ROW = 15;
    public static final int DEFAULT_COL = 25;
    public static final int TILE_SIZE = 32;
    private int col;

    private int width;
    private int height;

    private EditorManager editorManager;

    // ダブルバッファリング（db）用
    private Graphics dbg;
    private Image dbImage = null;

    private Thread gameLoop; // ゲームループ

    public EditorPanel(int _col) {
        col = _col;
        init();

        editorManager.init(ROW, col);
    }

    public EditorPanel(String loadMapName) {
        BufferedReader loadMap;

        try {
            File mapFile = new File(new URI(getClass().getClassLoader().getResource("res/map").toString() + "/" + loadMapName + ".dat"));
            loadMap = new BufferedReader(new FileReader(mapFile));
        }
        catch (IOException | URISyntaxException ex) {
            return;
        }

        try {
            loadMap.readLine();
            loadMap.readLine();
            col = Integer.parseInt(loadMap.readLine());
            loadMap.close();
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame,
                        "エラー");
            return;
        }

        init();

        editorManager.init(ROW, col);
        frame.pack();

        editorManager.loadMap(loadMapName);
    }

    private void init() {
        frame = JavaMario.getInstance();
        setPreferredSize(new Dimension(DEFAULT_COL * TILE_SIZE, ROW * TILE_SIZE));
        width = col * TILE_SIZE;
        height = ROW * TILE_SIZE;

        editorManager = EditorManager.getInstance();

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();

        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void adjustSize() {
        setPreferredSize(new Dimension(DEFAULT_COL * TILE_SIZE, ROW *TILE_SIZE + 15));
    }

    public void setColumns(int c) {
        width = c * TILE_SIZE;
        setPreferredSize(new Dimension(c * TILE_SIZE, ROW * TILE_SIZE));
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
            dbImage = createImage(width, height);

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
        dbg.fillRect(0, 0, width, height);

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

    public int getCol() {
        return col;
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
    }

    public void mouseReleased(MouseEvent e){
    }

    public void mouseDragged(MouseEvent e) {
        editorManager.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
    }
}
