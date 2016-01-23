import java.awt.*;
import javax.swing.*;

public class GameObject {
    protected int px, py;
    protected int size;
    protected Image icon;
    protected int scale;

    public GameObject(int _px, int _py) {
        px = _px;
        py = _py;
    }

    public void move() {
    }

    public void draw(Graphics g) {
    }

    public int getPx(){
        return px;
    }

    public void setPx(int px){
        this.px = px;
    }

    public int getPy(){
        return py;
    }

    public void setPy(int py){
        this.py = py;
    }

    protected void loadImage(String name) {
        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(name));
        icon = ii.getImage();
    }
}
