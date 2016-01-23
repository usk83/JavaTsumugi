import java.awt.*;
import javax.swing.*;

public class GameObject {
    protected float px, py;
    protected int size;
    protected Image icon;
    protected int scale;

    public GameObject(float _px, float _py) {
        px = _px;
        py = _py;
    }

    public void move() {
    }

    public void draw(Graphics g) {
    }

    public float getPx(){
        return px;
    }

    public void setPx(int px){
        this.px = px;
    }

    public float getPy(){
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
