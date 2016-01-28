import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GameStartMenu extends JMenu {

    private JavaMario frame;

    public GameStartMenu(String label) {
        super(label);
        frame = JavaMario.getInstance();
        // マップデータ一覧メニューを作成
        try {
            URI uri = new URI(getClass().getClassLoader().getResource("res/map").toString());
            File[] files = new File(uri).listFiles();

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    String fileFullName = file.getName();
                    String fileName = "";
                    int point = fileFullName.lastIndexOf(".");
                    if (point != -1) {
                        fileName = fileFullName.substring(0, point);
                    }
                    if (file.getPath().endsWith(".dat")) {
                        JMenuItem map = new JMenuItem(fileName);
                        this.add(map);
                        map.addActionListener(
                            new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                        frame.gameStart(fileFullName);
                                    }
                                }
                        );
                    }
                }
            }
        } catch (URISyntaxException e) {}
    }
}
