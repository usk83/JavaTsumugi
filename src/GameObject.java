import java.awt.*;
import javax.swing.*;

public abstract class GameObject {
    protected int width;
    protected int height;
    protected int imgWidth;
    protected int imgHeight;
    protected float px, py;
    protected Image icon;

    public GameObject(float _px, float _py, String path) {
        width = MainPanel.TILE_SIZE;
        height = MainPanel.TILE_SIZE;
        px = _px;
        py = _py;
        loadImage(path);
        // icon = icon.getScaledInstance(width, -1, Image.SCALE_FAST);
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        g.drawImage(icon, (int)px + offsetX, (int)py + offsetY, width, height, null);
    }

    protected void loadImage(String path) {
        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(path));
        icon = ii.getImage();
        imgWidth = icon.getWidth(null);
        imgHeight = icon.getHeight(null);
    }

    public float getPx(){
      return px;
    }

    public float getPy(){
      return py;
    }

    public int getWidth(){
      return width;
    }

    public int getHeight(){
      return height;
    }
}
