import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class TitlePanel extends JPanel {
    public static final int WIDTH = 352;
    public static final int HEIGHT = 176;

    private Image titleImage;

    public TitlePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        ImageIcon titleImageIcon = new ImageIcon(getClass().getClassLoader().getResource("res/title/title.png"));
        titleImage = titleImageIcon.getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(titleImage, 0, 0, WIDTH, HEIGHT, this);
    }
}
