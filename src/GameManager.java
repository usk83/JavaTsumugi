import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Point;
import java.io.IOException;
import java.util.*;

import javax.sound.sampled.*;

public class GameManager {

    private static final GameManager instance = new GameManager();

    public static double gravity = 1.0;

    private Mario mario;
    private List<GameObject> gameObjects;
    private List<MovableGameObject> movableGameObjects;

    private Map map;

    private Clip bgm;

    HashMap<Integer, Integer> keys;

    private GameManager() {
        gameObjects = new ArrayList<>();
        movableGameObjects= new ArrayList<>();

        // キーは押していない状態
        keys = new HashMap<Integer, Integer>(3);
        keys.put(KeyEvent.VK_LEFT, KeyStatus.RELEASED);
        keys.put(KeyEvent.VK_RIGHT, KeyStatus.RELEASED);
        keys.put(KeyEvent.VK_SPACE, KeyStatus.RELEASED);
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void init() {
        // マリオを追加
        mario = new Mario(320, 160);
        movableGameObjects.add(mario);

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

        // マップの読み込み
        initMap();
    }

    public void initMap() {
        // マップを作成
        map = new Map("res/map/01.dat");
    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);
    }
    public void addMovableGameObject(MovableGameObject mgo) {
        movableGameObjects.add(mgo);
    }

    public void update() {
        mario.keyAction(keys);
        
        //CheckColisionを前に持ってきた
        checkCollision();
        for (int i = 0; i < movableGameObjects.size(); i++) {
            movableGameObjects.get(i).move();
        }
    }

    public void render(Graphics g) {

        // X方向のオフセットを計算
        int offsetX = MainPanel.WIDTH / 2 - (int)mario.getPx();
        // マップの端ではスクロールしないようにする
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, MainPanel.WIDTH - map.getWidth());

        // Y方向のオフセットを計算
        int offsetY = MainPanel.HEIGHT / 2 - (int)mario.getPy();
        // マップの端ではスクロールしないようにする
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, MainPanel.HEIGHT - map.getHeight());

        for (int i = 0; i < movableGameObjects.size(); i++) {
            movableGameObjects.get(i).draw(g, offsetX, offsetY);
        }

        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).draw(g, offsetX, offsetY);
        }
    }

    public Point getTileCollision(MovableGameObject mgo, Float newPx, Float newPy) {
        return map.getTileCollision(mgo, newPx, newPy);
    }

    private void checkCollision() {
        Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            GameObject go = (GameObject)iterator.next();
            // マリオと接触してたら
            if (mario.isCollision(go)) {
                // コインだったら削除する
                if (go instanceof Coin) {
                    Coin coin = (Coin)go;
                    coin.playCoinSound(); // 消滅時のサウンド再生
                    gameObjects.remove(coin); // 削除
                    break;
                 }
             }
         }
         Iterator<MovableGameObject> iteratorM = movableGameObjects.iterator();
         while (iteratorM.hasNext()) {
             MovableGameObject go = (MovableGameObject)iteratorM.next();
             // マリオと接触してたら
             if (mario.isCollision(go)) {
                 //クリボだったら死ぬ
                 if (go instanceof Kuribo) {
                     Kuribo kuribo = (Kuribo)go;
                     // 上から踏まれたら
                     if ((int)mario.getPy() < (int)kuribo.getPy()) {
                     movableGameObjects.remove(kuribo); // kuribo削除
                     kuribo.playKuriboSound(); //消滅時のサウンド
                     mario.reflectJump(); //踏むとmarioジャンプ
                     break;
                   } else {
                      System.out.println("Game Over");
                     }
                  }
              }
          }
     }
    
    public void checkCoinBlock(Point tile) {
    	Iterator<GameObject> iterator = gameObjects.iterator();
         while (iterator.hasNext()) {
             GameObject go = (GameObject)iterator.next();
             if(go instanceof CoinBlock && !((CoinBlock) go).isKnocked()) {//コインブロックだったら、コインブロックを叩く処理をする。
            	 if(tile.x == map.pixelsToTiles(go.getPx()) && tile.y == map.pixelsToTiles(go.getPy())) {
            		 ((CoinBlock) go).knockCoinBlock();
            	 }
             }
             else {
            	 //頭突きの衝突音
            	 mario.playBumpSound();
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
}
