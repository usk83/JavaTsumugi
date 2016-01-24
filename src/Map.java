import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Map {
    private GameManager gameManager;

    private char[][] map; // マップ

    public Map(String mapPath) {
        gameManager = GameManager.getInstance();
        loadMap(mapPath); // マップの読み込み
    }

    /**
     * マップをロードする
     */
    private void loadMap(String mapPath) {
        int row; // 行数
        int col; // 列数

        try {
            // ファイルを開く
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(mapPath)));
            // 行数を読み込む
            String line = br.readLine();
            row = Integer.parseInt(line);
            // 列数を読み込む
            line = br.readLine();
            col = Integer.parseInt(line);

            System.out.println("row: " + row + " col: " + col);
            // マップを作成
            map = new char[row][col];
            for (int i = 0; i < row; i++) {
                line = br.readLine();
                for (int j = 0; j < col; j++) {
                    map[i][j] = line.charAt(j);
                    // マップデータにしたがってオブジェクトの配置
                    switch (map[i][j]) {
                        case 'B': // ブロック
                            gameManager.addGameObject(new Block(tilesToPixels(j), tilesToPixels(i)));
                            break;
                    }
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * タイル単位をピクセル単位に変更する
     */
    public static int tilesToPixels(int tiles) {
        return tiles * MainPanel.TILE_SIZE;
    }
}
