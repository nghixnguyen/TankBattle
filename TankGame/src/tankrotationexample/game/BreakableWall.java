package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject {
    float x, y;
    BufferedImage img;
    private  int health = 1;

    private Rectangle hitBox;
    public BreakableWall(float x, float y, BufferedImage img) {
        super(x,y,img);
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitBox = new Rectangle((int) x, (int) y,this.img.getWidth(),this.img.getHeight());
    }

    @Override
    public void collides(GameObject obj2) {
        if(health <= 0) {
            hasCollided = true;
        } else {
            health = health - 1;
            this.img = ResourceManager.getSprite("break2");
        }
    }

    public void drawImage(Graphics buffer) {
        if(!hasCollided){
            Graphics2D g2d = (Graphics2D) buffer;
            buffer.drawImage(this.img, (int) x, (int) y, null);
        }
    }

    public Rectangle getHitBox(){
        return this.hitBox.getBounds();
    }
}
