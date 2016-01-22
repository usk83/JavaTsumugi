import javax.swing.JFrame;

public class JavaMario extends JFrame {
    public JavaMario() {
        setTitle("JavaMario");

        // メインパネルを追加
        MainPanel panel = new MainPanel();
        getContentPane().add(panel);

        pack();
        setResizable(false);
    }

    public static void main(String[] args) {
        JavaMario frame = new JavaMario();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
