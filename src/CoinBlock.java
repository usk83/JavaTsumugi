import java.applet.*;

public class CoinBlock extends GameObject {
	
	private boolean Knocked;
    private AudioClip coinBlockSound;

    public CoinBlock(float _px, float _py) {
        super(_px, _py, "res/block/CoinBlock.png", 5);
        Knocked = false;
        coinBlockSound = Applet.newAudioClip(getClass().getClassLoader().getResource("res/sound/effects/Coin.wav"));
    }

    public void playCoinBlockSound() {
       coinBlockSound.play();
    }
    
    public void knockCoinBlock() {
    	if (Knocked) {
    		return;
    	}
    	Knocked = true;
    	iconCount = 4;
    	coinBlockSound.play();
    }
    
    protected void runAnimation() {
    	if (Knocked) {
    		return;
    	}
        iconCount += 1;
        if (iconCount >= 3) {
            iconCount = 0;
        }
    }
    public boolean isKnocked() {
    	return Knocked;
    }
    
}
