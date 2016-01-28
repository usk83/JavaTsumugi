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
    private static final EditorManager instance = new EditorManager();

    private char[][] map;
    private float mapGravity;
    private int row;
    private int col;

    private EditorManager() {
        frame = JavaMario.getInstance();
        mapGravity = 0;
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
                loadMap.read(); // 改行文字
            }
            loadMap.close();
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame,
                        "エラー");
            return;
        }
    }

    public void saveMap() {
        String fileName;
        PrintWriter newMap;

        SaveDialog dialog = new SaveDialog(frame, mapGravity);
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
            newMap.println("");
        }
        newMap.close();

        System.out.println("保存しました");
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

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / EditorPanel.TILE_SIZE;
        int y = e.getY() / EditorPanel.TILE_SIZE;

        if (x >= 0 && x < col && y >= 0 && y < row) {
            if (map[y][x] == ' ') {
                map[y][x] = 'B';
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / EditorPanel.TILE_SIZE;
        int y = e.getY() / EditorPanel.TILE_SIZE;

        if (x >= 0 && x < col && y >= 0 && y < row) {
            if (map[y][x] == ' ') {
                map[y][x] = 'B';
            }
        }
    }
}
