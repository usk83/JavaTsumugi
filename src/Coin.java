import java.applet.*;

public class Coin extends GameObject {

    private AudioClip coinSound;

    public Coin(float _px, float _py) {
        super(_px, _py, "res/img/coin.png", 4);
        coinSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/coin.wav"));
    }

    public void playCoinSound() {
        coinSound.play();
    }
}
