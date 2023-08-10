package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;

import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected float x, y;
    BufferedImage img;
    boolean hasCollided = false;

    GameObject(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
    public static GameObject newInstance(String type, float x, float y) throws UnsupportedOperationException {
        return switch (type){
            case "9", "3" -> new Wall(x,y, ResourceManager.getSprite("unbreak"));
            case "2" -> new BreakableWall(x,y, ResourceManager.getSprite("break1"));
            case "4" -> new Health(x,y, ResourceManager.getSprite("health"));
            case "5" -> new Speed(x,y, ResourceManager.getSprite("speed"));
            case "6" -> new Shield(x,y, ResourceManager.getSprite("shield"));
            default -> throw new UnsupportedOperationException("Unsupported GameObject type: " + type);
        };
    }
    public abstract void drawImage(Graphics g);
    public abstract Rectangle getHitBox();
    public abstract void collides(GameObject obj2);
}
