import java.awt.*;
import javax.swing.*;

public abstract class GameObject {
    protected float px, py;
    protected Image icon;

    public GameObject(float _px, float _py, String path) {
        px = _px;
        py = _py;

        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(path));
        icon = ii.getImage();
        icon = icon.getScaledInstance(MainPanel.TILE_SIZE, -1, Image.SCALE_FAST);
    }

    public void draw(Graphics g) {
        g.drawImage(icon, (int)px, (int)py, null);
    }

    protected void loadImage(String name) {
        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(name));
        icon = ii.getImage();
    }
}
