import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EditorManager {
    private JavaMario frame;
    private PalettePanel palettePanel;
    private static final EditorManager instance = new EditorManager();

    private char[][] map;
    private float mapGravity;
    private String loadedMapName;
    private int row;
    private int col;

    private Mario mario;
    private Block block;
    private CoinBlock coinBlock;
    private Coin coin;
    private Kuribo kuribo;

    private EditorManager() {
        frame = JavaMario.getInstance();
        palettePanel = PalettePanel.getInstance();
        mapGravity = 0;
        loadedMapName = "";
        mario = new Mario(0, 0);
        block = new Block(0, 0);
        coinBlock = new CoinBlock(0, 0);
        coin = new Coin(0, 0);
        kuribo = new Kuribo(0, 0);
    }

    public static EditorManager getInstance() {
        return instance;
    }

    public void init(int r, int c) {
        row = r;
        col = c;

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

    public void loadMap(String loadMapName) {
        BufferedReader loadMap;

        try {
            File mapFile = new File(new URI(getClass().getClassLoader().getResource("res/map").toString() + "/" + loadMapName + ".dat"));
            loadMap = new BufferedReader(new FileReader(mapFile));
        }
        catch (IOException | URISyntaxException ex) {
            return;
        }

        try {
            mapGravity = Float.parseFloat(loadMap.readLine());
            loadMap.readLine();
            loadMap.readLine();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    map[i][j] = (char)loadMap.read();
                }
                loadMap.read(); // 改行文字
            }
            loadMap.close();
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame,
                        "エラー");
            return;
        }
        loadedMapName = loadMapName;
    }

    public void saveMap() {
        String fileName;
        PrintWriter newMap;

        if (!validateMario()) {
            JOptionPane.showMessageDialog(frame, "マリオの数が不正です");
            return;
        }

        SaveDialog dialog = new SaveDialog(frame, loadedMapName, mapGravity);
        dialog.setVisible(true);

        if (!dialog.isSavePressed()) {
            return;
        }

        fileName = dialog.getFileName();
        mapGravity = dialog.getMapGravity();

        // 保存処理
        try {
            File mapFile = new File(new URI(getClass().getClassLoader().getResource("res/map").toString() + "/" + fileName + ".dat"));
            mapFile.createNewFile();
            newMap = new PrintWriter(new BufferedWriter(new FileWriter(mapFile)));
        }
        catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(frame,
                        "エラー");
            return;
        }

        newMap.println(mapGravity);
        newMap.println(row);
        newMap.println(col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                newMap.print(map[i][j]);
            }
            newMap.print("\n");
        }
        newMap.close();

        JOptionPane.showMessageDialog(frame,
                                        "保存しました。");
        System.exit(0);
    }

    public void update() {
    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                switch (map[i][j]) {
                    case 'm':
                        mario.draw(g, j * EditorPanel.TILE_SIZE, i * EditorPanel.TILE_SIZE, 0);
                        break;
                    case 'B':
                        block.draw(g, j * EditorPanel.TILE_SIZE, i * EditorPanel.TILE_SIZE, 0);
                        break;
                    case 'C':
                        coinBlock.draw(g, j * EditorPanel.TILE_SIZE, i * EditorPanel.TILE_SIZE, 0);
                        break;
                    case 'c':
                        coin.draw(g, j * EditorPanel.TILE_SIZE, i * EditorPanel.TILE_SIZE, 0);
                        break;
                    case 'k':
                        kuribo.draw(g, j * EditorPanel.TILE_SIZE, i * EditorPanel.TILE_SIZE, 0);
                        break;
                    default:
                        g.fillRect(j * EditorPanel.TILE_SIZE, i * EditorPanel.TILE_SIZE, EditorPanel.TILE_SIZE, EditorPanel.TILE_SIZE);
                }
            }
        }
    }

    public boolean validateMario() {
        int marioCount = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (map[i][j] == 'm') {
                    marioCount++;
                }
            }
        }

        if (marioCount == 1) {
            return true;
        }

        return false;
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / EditorPanel.TILE_SIZE;
        int y = e.getY() / EditorPanel.TILE_SIZE;

        if (x >= 0 && x < col && y >= 0 && y < row) {
            map[y][x] = palettePanel.getSelected();
        }
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / EditorPanel.TILE_SIZE;
        int y = e.getY() / EditorPanel.TILE_SIZE;

        if (x >= 0 && x < col && y >= 0 && y < row) {
            map[y][x] = palettePanel.getSelected();
        }
    }
}
