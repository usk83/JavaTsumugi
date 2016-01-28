import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.System;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class EditorMenu extends JMenu {

    private EditorManager editorManager;

    public EditorMenu(String label) {
        super(label);

        editorManager = EditorManager.getInstance();

        JMenuItem save = new JMenuItem("保存");
        save.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editorManager.saveMap();
                }
            }
        );
        JMenuItem exit = new JMenuItem("終了");
        exit.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            }
        );
        this.add(save);
        this.add(exit);
    }
}
