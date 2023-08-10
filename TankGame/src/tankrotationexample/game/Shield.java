package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shield extends GameObject implements PowerUps {
    float x, y;
    BufferedImage img;
    private Rectangle hitBox;
    public Shield(float x, float y, BufferedImage img) {
        super(x,y,img);
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitBox = new Rectangle((int) x, (int) y,this.img.getWidth(),this.img.getHeight());
    }

    public void drawImage(Graphics buffer) {
        if(!hasCollided) {
            Graphics2D g2d = (Graphics2D) buffer;
            buffer.drawImage(this.img, (int) x, (int) y, null);
        }
    }

    public Rectangle getHitBox(){
        return this.hitBox.getBounds();
    }
    @Override
    public void collides(GameObject obj2) {
    }

    @Override
    public void applyPowerUp(Tank tank) {
        tank.getShield();
        hasCollided = true;
    }
}
