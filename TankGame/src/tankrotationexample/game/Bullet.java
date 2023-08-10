package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float charge = 1f;
    private float R = 2;

    public int getBullet_ID() {
        return bullet_ID;
    }

    private int bullet_ID;
    private BufferedImage img;
    private Rectangle hitBox;
    private GameWorld gw;

    Bullet (float x, float y, BufferedImage img, float angle, int bullet_ID, GameWorld gw) {
        super(x,y,img);
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.img = img;
        this.angle = angle;
        this.bullet_ID = bullet_ID;
        this.hitBox = new Rectangle((int) x, (int) y,this.img.getWidth(),this.img.getHeight());
        this.gw = gw;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation((int) x, (int) y);
    }


    private void checkBorder() {
            if (x < 30) {
                hasCollided = true;
            }

            if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
                hasCollided = true;
            }
            if (y < 40) {
                hasCollided = true;
            }
            if (y >= GameConstants.GAME_WORLD_HEIGHT - 88) {
                hasCollided = true;
            }

    }

    public void increaseCharge() {
        this.charge = this.charge + 0.005f;
    }

    public void setHeading(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    @Override
   public void drawImage(Graphics g) {
        if(!hasCollided) {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0,
                    this.img.getHeight() / 2.0);
            rotation.scale(this.charge, this.charge);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, rotation, null);
        }
    }

    public Rectangle getHitBox(){
        return this.hitBox.getBounds();
    }
    @Override
    public void collides(GameObject obj2) {
        if((obj2 instanceof Tank t && this.bullet_ID != t.getTank_ID())
            || obj2 instanceof Wall){
            gw.anims.add(new Animation(x,y, ResourceManager.getAnimation("rockethit")));
            ResourceManager.getSound("bullet_hit").playSound();
            hasCollided = true;
        }
        if (obj2 instanceof BreakableWall bw) {
            gw.anims.add(new Animation(x,y, ResourceManager.getAnimation("rockethit")));
            ResourceManager.getSound("bullet_hit").playSound();
            bw.collides(this);
            hasCollided = true;
        }
    }
}
