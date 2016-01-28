import static java.lang.System.exit;
import javax.swing.JOptionPane;

public class Goal extends GameObject {
    public JavaMario frame;
    private DialogThread dialogThread;
    private  int waitTime;

    private final int CLEAR_WAIT = 6000;

    public Goal(float _px, float _py) {
        super(_px, _py, "res/object/flag.png", 4);

        waitTime = 200;
        dialogThread = new DialogThread();
        dialogThread.start();
    }

    public void clear() {
        waitTime = CLEAR_WAIT;
    }
    public class  DialogThread extends Thread {
        private boolean isGoaled;

        public void run() {
            isGoaled = false;
            while(true) {
                if(isGoaled) {
                    JOptionPane.showMessageDialog(frame, "ゲームクリア！終了します。");
                    exit(0);
                }
                try {
                    if(waitTime == CLEAR_WAIT){
                        isGoaled = true;
                    }
                    this.sleep(waitTime);
                }
                catch (InterruptedException e){
                    System.out.println("Goal dialogThread err!");
                }
            }
        }
    }
}
