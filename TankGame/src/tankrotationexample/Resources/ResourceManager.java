package tankrotationexample.Resources;

import tankrotationexample.game.Bullet;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;


public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String,List<BufferedImage>> animations = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private final static Map<String ,Integer> animationInfo =new HashMap<>(){{
        put("bullethit",24);
        put("bulletshoot",24);
        put("powerpick",32);
        put("puffsmoke",32);
        put("rocketflame",16);
        put("rockethit",32);
    }};

    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path),
                        "%s image is missing".formatted(path)));
    }
    private static void initSprites(){
        try {
            ResourceManager.sprites.put("menu", loadSprite("menu/title.png"));
            ResourceManager.sprites.put("control", loadSprite("menu/instruction.PNG"));
            ResourceManager.sprites.put("powerup", loadSprite("menu/powerup.PNG"));
            ResourceManager.sprites.put("tank1", loadSprite("tank/tank1.png"));
            ResourceManager.sprites.put("tank2", loadSprite("tank/tank2.png"));
            ResourceManager.sprites.put("bullet", loadSprite("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1", loadSprite("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2", loadSprite("bullet/rocket2.png"));
            ResourceManager.sprites.put("floor", loadSprite("floor/bg.bmp"));
            ResourceManager.sprites.put("health", loadSprite("powerups/health.png"));
            ResourceManager.sprites.put("shield", loadSprite("powerups/shield.png"));
            ResourceManager.sprites.put("speed", loadSprite("powerups/speed.png"));
            ResourceManager.sprites.put("break1", loadSprite("walls/break1.jpg"));
            ResourceManager.sprites.put("break2", loadSprite("walls/break2.jpg"));
            ResourceManager.sprites.put("unbreak", loadSprite("walls/unbreak.jpg"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initAnimations(){
        String baseName = "animations/%s/%s_%04d.png";
            animationInfo.forEach((animationName, frameCount) -> {
                List<BufferedImage> frames = new ArrayList<>();
                try {
                    for (int i = 0; i < frameCount; i++) {
                        String spritePath = baseName.formatted(animationName,animationName,i);
                        frames.add(loadSprite(spritePath));
                    }
                    ResourceManager.animations.put(animationName, frames);
                } catch (IOException e) {
                    System.out.println(e);
                    throw new RuntimeException(e);
                }
            });
    }

    private static void initSounds(){
        try {
            ResourceManager.sounds.put("bullet_shoot", loadSound("sounds/bullet.wav"));
            ResourceManager.sounds.put("bullet_hit", loadSound("sounds/bullet_shoot.wav"));
            ResourceManager.sounds.put("explosion", loadSound("sounds/explosion.wav"));
            ResourceManager.sounds.put("bg", loadSound("sounds/Music.mid"));
            ResourceManager.sounds.put("pickup", loadSound("sounds/pickup.wav"));
            ResourceManager.sounds.put("shotfire", loadSound("sounds/bullet_shoot.wav"));

        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public static Sound loadSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        AudioInputStream ais = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path)
                )
        );
        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);
        s.setVolume(.5f);
        return s;
    }

    public static void loadResources(){
        ResourceManager.initSprites();
        ResourceManager.initAnimations();
        ResourceManager.initSounds();
    }

    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s resource is missing.".formatted(type));
        }
        return ResourceManager.sprites.get(type);
    }

    public static List<BufferedImage> getAnimation (String type){
        if(!ResourceManager.animations.containsKey(type)){
            throw new RuntimeException("%s resource is missing.".formatted(type));
        }
        return ResourceManager.animations.get(type);
    }

    public static Sound getSound (String type){
        if(!ResourceManager.sounds.containsKey(type)){
            throw new RuntimeException("%s resource is missing.".formatted(type));
        }
        return ResourceManager.sounds.get(type);
    }

}
