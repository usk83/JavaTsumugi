import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.JPanel;

public class EditorManager {
    private static final EditorManager instance = new EditorManager();

    private char[][] map;
    private int row;
    private int col;

    private Clip bgm;

    HashMap<Integer, Integer> keys;

    private EditorManager() {
        // キーは押していない状態
        keys = new HashMap<Integer, Integer>(3);
        keys.put(KeyEvent.VK_LEFT, KeyStatus.RELEASED);
        keys.put(KeyEvent.VK_RIGHT, KeyStatus.RELEASED);
        keys.put(KeyEvent.VK_SPACE, KeyStatus.RELEASED);
    }

    public static EditorManager getInstance() {
        return instance;
    }

    public void init() {
        // BGMの読み込み
        try {
            bgm = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream("res/sound/bgm/01-main-theme-overworld.wav"));
            bgm.open(inputStream);
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (LineUnavailableException |
               UnsupportedAudioFileException |
               IOException e) {
            System.out.println("play sound error: " + e.getMessage());
        }

        row = EditorPanel.HEIGHT/EditorPanel.TILE_SIZE;
        col = 30;
        initMap(row, col);
    }

    public void initMap(int r, int c) {
        map = new char[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                map[i][j] = ' ';
            }
        }
    }

    public void update() {
    }

    public void render(Graphics g) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (map[i][j] == ' ') {
                    g.setColor(Color.WHITE);
                }
                else if (map[i][j] == 'B') {
                    g.setColor(Color.ORANGE);
                }
                g.fillRect(j * EditorPanel.TILE_SIZE, i * EditorPanel.TILE_SIZE, EditorPanel.TILE_SIZE, EditorPanel.TILE_SIZE);
            }
        }
    }

    /**
     * キーがタイプされたとき
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * キーが押されたとき
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (keys.get(key) == null) {
            return;
        }
        if (keys.get(key) == KeyStatus.RELEASED) {
            keys.replace(key, KeyStatus.PRESSED);
        }
        else {
            keys.replace(key, KeyStatus.PRESSING);
        }

        if (key == KeyEvent.VK_LEFT) {
            keys.put(KeyEvent.VK_RIGHT, KeyStatus.RELEASED);
        }
        else if (key == KeyEvent.VK_LEFT) {
            keys.put(KeyEvent.VK_LEFT, KeyStatus.RELEASED);
        }
    }

    /**
     * キーが離されたとき
     */
    public void keyReleased(KeyEvent e) {
        keys.replace(e.getKeyCode(), KeyStatus.RELEASED);
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / EditorPanel.TILE_SIZE;
        int y = e.getY() / EditorPanel.TILE_SIZE;

        if (x >= 0 && x < col && y >= 0 && y < row) {
            if (map[y][x] == ' ') {
                System.out.println("ブロック");
                map[y][x] = 'B';
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / EditorPanel.TILE_SIZE;
        int y = e.getY() / EditorPanel.TILE_SIZE;

        if (x >= 0 && x < col && y >= 0 && y < row) {
            if (map[y][x] == ' ') {
                System.out.println("ブロック");
                map[y][x] = 'B';
            }
        }
    }
}
