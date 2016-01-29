import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Point;
import java.io.IOException;
import java.util.*;
import java.applet.*;
import javax.sound.sampled.*;
import javax.swing.*;
import static java.lang.System.exit;


public class GameManager {

    private JavaMario frame;

    private static final GameManager instance = new GameManager();

    public static double gravity = 1.0;

    private Mario mario;
    private List<GameObject> gameObjects;
    private List<MovableGameObject> movableGameObjects;

    private Map map;

    private Clip bgm;
    private AudioClip goalSound;
    private AudioClip deathSound;

    private boolean isGamecleared;
    private boolean isGameovered;

    //Dialog系Threadを回すための変数。
    private int waitTime;
    private final int GAMEOVER_TIME = 3000;
    private final int GAMECLEAR_TIME = 6000;

    //ゲームオーバー,クリアのDialog用Thread
    private DialogThread dialogThread;

    HashMap<Integer, Integer> keys;

    private GameManager() {
        gameObjects = new ArrayList<>();
        movableGameObjects= new ArrayList<>();

        //フレームを持たせる。(ダイアログ用)
        frame = JavaMario.getInstance();

        // キーは押していない状態
        keys = new HashMap<Integer, Integer>(3);
        keys.put(KeyEvent.VK_LEFT, KeyStatus.RELEASED);
        keys.put(KeyEvent.VK_RIGHT, KeyStatus.RELEASED);
        keys.put(KeyEvent.VK_SPACE, KeyStatus.RELEASED);
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void init(String mapFileName) {
        //isGamecleared,isGameoveredを初期化
        isGamecleared = false;
        isGameovered = false;

        //DialogThreadを回すためのwaitTimeを初期化
        waitTime = 200;

        //DialogThreadをスタート。
        dialogThread = new DialogThread();
        dialogThread.start();

        // BGMの読み込み
        try {
            bgm = AudioSystem.getClip();
            //ゴールサウンドを取り込み。
            goalSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/bgm/06-level-complete.wav"));
            //ゲームオーバー用のサウンドを取り込み。
            deathSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/bgm/08-you're-dead.wav"));

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
        initMap(mapFileName);
    }

    public void initMap(String mapFileName) {
        // マップを作成
        map = new Map("res/map/" + mapFileName);
    }

    public void setMario(Mario m) {
        mario = m;
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
                //ゴールがisGameclearedがfalseの場合、ゴールになる。
                else if (go instanceof Goal && !isGamecleared) {
                    gameClear();
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
                    } else if (!isGameovered){//上以外から接触して且つisGameorveredがfalseなら(二重死亡防止)
                        gameOver();
                        break;
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

    //クリア時に呼び出される関数
    private void gameClear(){
        if(isGamecleared){
            return;
        }
        System.out.println("Game clear");
        //ゲームclear時のwaitTImeを設定
        waitTime = GAMECLEAR_TIME;
        goalSound.play();
        bgm.stop();
        //マリオのアニメーション
        mario.gameClear();
    }

    //死亡時に呼び出される関数
    public void gameOver(){
        if(isGameovered){
            return;
        }
        System.out.println("Game Over");
        //ゲームオーバー時のwaitTImeを設定
        waitTime = GAMEOVER_TIME;
        deathSound.play();
        bgm.stop();
        mario.gameOver();
    }
    //ゲームオーバー時、クリア時にwaitTImeが変化してそれぞれのDialogに突入するスレッド
    public class  DialogThread extends Thread {

        public void run() {
            while(true) {
                if(isGameovered) {
                    JOptionPane.showMessageDialog(frame, "ゲームオーバー！終了します。");
                    exit(0);
                }
                if(isGamecleared) {
                    JOptionPane.showMessageDialog(frame, "ゲームクリア！終了します。");
                    exit(0);
                }
                try {
                    if(waitTime == GAMEOVER_TIME){
                        isGameovered = true;
                    }
                    if(waitTime == GAMECLEAR_TIME){
                        isGamecleared = true;
                    }
                    this.sleep(waitTime);
                }
                catch (InterruptedException e){
                    System.out.println(" dialogThread err!");
                }
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
        if(isGamecleared||isGameovered){//ゴールまたはゲームオーバーしたらキー操作を無効にする。
            keys.replace(key, KeyStatus.RELEASED);
            return;
        }
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
