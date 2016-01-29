import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.System;

public class Map {
    private GameManager gameManager;

    private int row; // 行数
    private int col; // 列数
    private int width; // 幅
    private int height; // 高さ

    private char[][] map; // マップ

    public Map(String mapPath) {
        gameManager = GameManager.getInstance();
        loadMap(mapPath); // マップの読み込み
    }

    /**
     * マップをロードする
     */
    private void loadMap(String mapPath) {
        int marioCount = 0;

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

            // 幅と高さの設定
            width = MainPanel.TILE_SIZE * col;
            height = MainPanel.TILE_SIZE * row;

            // マップを作成
            map = new char[row][col];
            for (int i = 0; i < row; i++) {
                line = br.readLine();
                for (int j = 0; j < col; j++) {
                    map[i][j] = line.charAt(j);
                    // マップデータにしたがってオブジェクトの配置
                    switch (map[i][j]) {
                        case 'm': // マリオ
                            if (marioCount == 0) {
                                marioCount++;
                                Mario mario = new Mario(tilesToPixels(j), tilesToPixels(i));
                                gameManager.setMario(mario);
                                gameManager.addMovableGameObject(mario);
                            }
                            break;
                        case 'B': // ブロック
                            gameManager.addGameObject(new Block(tilesToPixels(j), tilesToPixels(i)));
                            break;
                        case 'C': // コインブロック
                            gameManager.addGameObject(new CoinBlock(tilesToPixels(j), tilesToPixels(i)));
                            break;
                        case 'c': // コイン
                            gameManager.addGameObject(new Coin(tilesToPixels(j), tilesToPixels(i)));
                            break;
                        case 'G': // ゴール
                            gameManager.addGameObject(new Goal(tilesToPixels(j), tilesToPixels(i)));
                            break;
                        case 'k': //クリボー
                            gameManager.addMovableGameObject(new Kuribo(tilesToPixels(j),tilesToPixels(i)));                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (marioCount == 0) {
            System.out.println("マリオがマップに存在しません");
            System.exit(-1);
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
                if (y < 0) {
                    // 上方向突き抜けokにする
                    // TODO: 画面外でブロックに乗った時の処理修正
                    return null;
                }
                else if (y >= row) {
                    //落ちた時のゲームオーバー処理
                    gameManager.gameOver();
                    // TODO: 穴に落ちたら姿が見えなくなって終了
                    return new Point(x, y);
                }
                // ブロックまたはコインブロックがあったら衝突
                if (map[y][x] == 'B' || map[y][x] == 'C') {
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

    public int getWidth(){
      return width;
    }

    public int getHeight(){
      return height;
    }
}
