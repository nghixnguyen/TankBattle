package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;
import java.util.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject {

    private final int tank_ID;
    private float x;
    private float y;
    private float screen_x,screen_y;
    private float vx;
    private float vy;
    private float angle;
    private float R = 2;
    private float ROTATIONSPEED = 2.0f;
    private BufferedImage img;
    long speedDuration = 0;
    List<Bullet> ammo = new ArrayList<>();
    long timeSinceLastShot = 0L;
    long timeSincePowerUps = 0l;
    long cooldown = 2000;
    long timeInterval = 20;
    int lives = 3;
    int health = 5;

    Bullet currentChargeBullet = null;
    private Rectangle hitBox;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private boolean lifeAffected = false;
    private boolean shieldOn;
    private boolean bulletCharged = true;
    private boolean tankDie = false;

    private GameWorld gw;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img, GameWorld gw) {
        super(x,y,img);
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitBox = new Rectangle((int) x, (int) y,this.img.getWidth(),this.img.getHeight());
        this.gw = gw;
        tank_ID = new Random().nextInt();
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}
    public float getX() { return x;}

    public float getY() { return y;}
    public int getTank_ID() {
        return tank_ID;
    }
    public float getScreen_x() {
        return screen_x;
    }
    public float getScreen_y() {
        return screen_y;
    }
    void toggleUpPressed() {
        this.UpPressed = true;
    }
    void toggleDownPressed() {
        this.DownPressed = true;
    }
    void toggleRightPressed() {
        this.RightPressed = true;
    }
    void toggleLeftPressed() {
        this.LeftPressed = true;
    }
    void unToggleUpPressed() {
        this.UpPressed = false;
    }
    void unToggleDownPressed() {
        this.DownPressed = false;
    }
    void unToggleRightPressed() {
        this.RightPressed = false;
    }
    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }
    public Rectangle getHitBox(){
        return this.hitBox.getBounds();
    }
    void update() {
        if(System.currentTimeMillis() > this.speedDuration){
            cooldown = 2000;
            timeInterval = 20;
        }
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.ShootPressed &&
                (this.timeSinceLastShot + this.cooldown) < System.currentTimeMillis()) {
            this.timeSinceLastShot = System.currentTimeMillis();
            var b = new Bullet(safeShootX(),safeShootY(),ResourceManager.getSprite("bullet"),angle,tank_ID, gw);
            ResourceManager.getSound("bullet_shoot").playSound();
            this.ammo.add(b);
            gw.addGameObject(b);
            gw.anims.add(new Animation(safeShootX(), safeShootY()+12, ResourceManager.getAnimation("bulletshoot")));
        }

        this.ammo.forEach(bullet -> bullet.update());
        centerScreen();
        this.hitBox.setLocation((int) x, (int) y);
    }

    private int safeShootX() {
        double offsetX = 31 * Math.cos(Math.toRadians(angle));
        return (int) (x + img.getWidth() / 2f + offsetX - 4f);
    }

    private int safeShootY() {
        double offsetY = 31 * Math.sin(Math.toRadians(angle));
        return (int) (y + img.getWidth() / 2f + offsetY - 4f);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void centerScreen(){
        this.screen_x = this.x - GameConstants.GAME_SCREEN_WIDTH / 4.f;
        this.screen_y = this.y - GameConstants.GAME_SCREEN_HEIGHT / 2.f;

        if (this.screen_x < 0) this.screen_x =0;
        if (this.screen_y < 0) this.screen_y =0;

        if (this.screen_x > GameConstants.GAME_WORLD_WIDTH
                - GameConstants.GAME_SCREEN_WIDTH / 2f ){
            this.screen_x = GameConstants.GAME_WORLD_WIDTH
                    - GameConstants.GAME_SCREEN_WIDTH / 2f;
        }
        if (this.screen_y > GameConstants.GAME_WORLD_HEIGHT
                - GameConstants.GAME_SCREEN_HEIGHT){
            this.screen_y =  GameConstants.GAME_WORLD_HEIGHT
                    - GameConstants.GAME_SCREEN_HEIGHT;
        }
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        this.ammo.forEach(b -> b.drawImage(g2d));

        if (this.currentChargeBullet != null){
            this.currentChargeBullet.drawImage(g2d);
        }
        g2d.setColor(Color.GREEN);
        g2d.drawRect((int)x, (int) (y-30),100,10);
        long currentWidth = 100 - ((this.timeSinceLastShot + this.cooldown)
                                    - System.currentTimeMillis())/timeInterval;
        if (currentWidth > 100){
            currentWidth = 100;
        }
        g2d.fillRect((int)x, (int) (y-30), (int)currentWidth,10);

        if(health >= 5){
            health = 5;
        }
        int maxHealth = 5;
        int healthBarWidth = (int) Math.ceil((health * 1.0 / maxHealth) * 100);
        if (healthBarWidth < 0) {
            healthBarWidth = 0;
        }
        // Draw health bar
        g2d.setColor(Color.YELLOW);
        g2d.drawRect((int) x, (int) (y - 15), 100, 10);
        g2d.fillRect((int) x, (int) (y - 15), healthBarWidth, 10);

        if (health <= 0) {
            lives--;
            health = 5;
        }

        // Draw lives count
        if(lives >= 3) {
            lives = 3;
        }
        g2d.setColor(Color.PINK);
        for (int i = 0; i < lives; i++) {
            g2d.fillOval((int) x + i * 20, (int) (y + 55), 15, 15);
        }
    }

    public void toggleShootPressed() {
        this.ShootPressed = true;
    }

    public void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    public boolean tankDie(){
        return lives == 0;
    }
    @Override
    public void collides(GameObject obj2) {
        if (obj2 instanceof Bullet b && this.tank_ID != b.getBullet_ID()){
            ResourceManager.getSound("bullet_hit").playSound();
            if (shieldOn){
                shieldOn = false;
            } else {
                health = health - 1;
            }
        } if (obj2 instanceof Wall
                    ||obj2 instanceof Tank
                    ||obj2 instanceof BreakableWall) {
            handleCollision();
        }
        if(obj2 instanceof PowerUps){
            gw.anims.add(new Animation(safeShootX(),safeShootY(), ResourceManager.getAnimation("powerpick")));
            ResourceManager.getSound("pickup").playSound();
            ((PowerUps)obj2).applyPowerUp(this);
        }
    }

    private void handleCollision() {
        if(UpPressed){
            this.x -= this.vx;
            this.y -= this.vy;
        }
        if (DownPressed) {
            this.x += this.vx;
            this.y += this.vy;
        }
    }

    public void getShield() {
        shieldOn = true;
    }

    public void getHealth() {
        health = health + 1;
    }

    public void getSpeed() {
        this.speedDuration = System.currentTimeMillis() + 10000;
        cooldown = 800;
        timeInterval = 8;
    }
}
