import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Map {
    private GameManager gameManager;

    private int row; // 行数
    private int col; // 列数

    private char[][] map; // マップ

    public Map(String mapPath) {
        gameManager = GameManager.getInstance();
        loadMap(mapPath); // マップの読み込み
    }

    /**
     * マップをロードする
     */
    private void loadMap(String mapPath) {
        try {
            // ファイルを開く
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(mapPath)));
            // 重力の値を読み込む
            String line = br.readLine();
            gameManager.gravity = Float.parseFloat(line);
            // 行数を読み込む
            line = br.readLine();
            row = Integer.parseInt(line);
            // 列数を読み込む
            line = br.readLine();
            col = Integer.parseInt(line);

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * (newPx, newPy)で衝突するブロックの座標を返す
     */
    public Point getTileCollision(MovableGameObject mgo, Float newPx, Float newPy) {

        Float fromX = Math.min(mgo.getPx(), newPx);
        Float fromY = Math.min(mgo.getPy(), newPy);
        Float toX = Math.max(mgo.getPx(), newPx);
        Float toY = Math.max(mgo.getPy(), newPy);

        int fromTileX = pixelsToTiles(fromX);
        int fromTileY = pixelsToTiles(fromY);
        int toTileX = pixelsToTiles(toX + mgo.getWidth() - 1);
        int toTileY = pixelsToTiles(toY + mgo.getHeight() - 1);

        // 衝突しているか調べる
        for (int x = fromTileX; x <= toTileX; x++) {
            for (int y = fromTileY; y <= toTileY; y++) {
                // 画面外は衝突
                if (x < 0 || x >= col) {
                    return new Point(x, y);
                }
                if (y < 0 || y >= row) {
                    return new Point(x, y);
                }
                // ブロックがあったら衝突
                if (map[y][x] != ' ') {
                    return new Point(x, y);
                }
            }
        }

        return null;
    }

    /**
     * タイル単位をピクセル単位に変更する
     */
    public static int tilesToPixels(int tiles) {
        return tiles * MainPanel.TILE_SIZE;
    }

    /**
     * ピクセル単位をタイル単位に変更する
     */
    public static int pixelsToTiles(float pixels) {
        return (int)Math.floor(pixels / MainPanel.TILE_SIZE);
    }
}