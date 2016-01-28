import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

public class JavaMario extends JFrame implements KeyListener {

    private static final JavaMario instance = new JavaMario();

    TitlePanel titlePanel;
    JScrollPane scrollPane;

    private JavaMario() {
        setTitle("JavaMario");
    }

    public void init() {
        titlePanel = new TitlePanel();
        getContentPane().add(titlePanel);

        initManuBar();

        pack();
        setResizable(false);
    }

    public static JavaMario getInstance() {
        return instance;
    }

    private void initManuBar() {
        // ファイルメニュー
        JMenu gameStartMenu = new GameStartMenu("ゲームプレイ");
        JMenu mapEditorMenu = new MapEditorMenu("マップ作成");

        // メニューバー
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(gameStartMenu);
        menuBar.add(mapEditorMenu);
        setJMenuBar(menuBar);
    }

    public void gameStart(String mapFileName) {
        setJMenuBar(null);
        getContentPane().remove(titlePanel);
        MainPanel mainPanel = new MainPanel(mapFileName);
        getContentPane().add(mainPanel);
        addKeyListener(mainPanel);
        pack();
    }

    public void editorStart(int columns) {
        getContentPane().remove(titlePanel);
        EditorPanel editorPanel = new EditorPanel(columns);
        scrollPane = new JScrollPane(editorPanel);

        getContentPane().add(scrollPane);

        pack();

        if (columns > editorPanel.DEFAULT_COL) {
            editorPanel.adjustSize();
            pack();
        }

        editorPanel.setColumns(columns);
    }

    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JavaMario frame = JavaMario.getInstance();
        frame.init();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
