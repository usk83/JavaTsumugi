import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.System;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GameMenu extends JMenu {
    public GameMenu(String label) {
        super(label);
        JMenuItem exit = new JMenuItem("終了");
        exit.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            }
        );
        this.add(exit);
    }
}
