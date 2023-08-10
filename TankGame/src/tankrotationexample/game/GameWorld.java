package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;

    private Tank t2;

    private final Launcher lf;
    private long tick = 0;
    List<GameObject> gobjs = new ArrayList<>(1000);
    List<Animation> anims = new ArrayList<>();
    Sound bg = ResourceManager.getSound("bg");

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            bg.setLooping();
            bg.playSound();
            this.resetGame();
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update(); // update tank
                this.checkCollision();
                this.anims.forEach(animation -> animation.update());
                this.gobjs.removeIf(GameObject -> GameObject.hasCollided);
                this.repaint();   // redraw game

                if(t1.tankDie() || t2.tankDie()){
                    if(t1.tankDie()){
                        lf.getEndGame().setWinner(2);
                    } else if(t2.tankDie()){
                        lf.getEndGame().setWinner(1);
                    }
                    this.bg.stop();
                    this.lf.setFrame("end");
                    return;
                }
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */
                Thread.sleep(1000 / 144);
                this.anims.removeIf(a -> !a.isRunning());
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.anims.clear();
        this.gobjs.clear();
        InitializeGame();
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream(
                        "maps/map2.csv"))
        );

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            int row = 0;
            String[] gameItems;
            while (mapReader.ready()) {
                gameItems = mapReader.readLine().strip().split(",");
                for (int col = 0; col < gameItems.length; col++) {
                    String gameObject = gameItems[col];
                    if ("0".equals(gameObject)) continue;
                    this.gobjs.add(GameObject.newInstance(gameObject, col * 30, row * 30));
                }
                row++;
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        t1 = new Tank(60, 420, 0, 0, (short) 0, ResourceManager.getSprite("tank1"),this);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(1640, 800, 0, 0, (short) 180, ResourceManager.getSprite("tank2"),this);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        this.gobjs.add(t1);
        this.gobjs.add(t2);
    }

    private void drawFloor(Graphics2D buffer){
        BufferedImage floor = ResourceManager.getSprite("floor");
        for(int i = 0; i< GameConstants.GAME_WORLD_WIDTH; i+=320){
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+=240){
                buffer.drawImage(floor,i,j,null);
            }
        }
    }

    private void renderMiniMap(Graphics2D g2){
        BufferedImage miniMap = this.world.getSubimage(
                0,0,GameConstants.GAME_WORLD_WIDTH,GameConstants.GAME_WORLD_HEIGHT);
        g2.scale(.2,.2);
        g2.drawImage(miniMap,
                (GameConstants.GAME_SCREEN_WIDTH*5)/2 - (GameConstants.GAME_WORLD_WIDTH/2),
                (GameConstants.GAME_SCREEN_HEIGHT*5) - (GameConstants.GAME_WORLD_HEIGHT)-180, null);
    }

    private void renderSplitScreen(Graphics2D g2) {
        BufferedImage leftHalf = this.world.getSubimage((int) this.t1.getScreen_x(),
                (int) this.t1.getScreen_y(),
                GameConstants.GAME_SCREEN_WIDTH / 2,
                GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rightHalf = this.world.getSubimage((int) this.t2.getScreen_x(),
                (int) this.t2.getScreen_y(),
                GameConstants.GAME_SCREEN_WIDTH / 2,
                GameConstants.GAME_SCREEN_HEIGHT);
        g2.drawImage(leftHalf, 0, 0, null);
        g2.drawImage(rightHalf, (GameConstants.GAME_SCREEN_WIDTH / 2) + 4, 0, null);
    }

    private void checkCollision() {
        for (int i = 0; i < this.gobjs.size(); i++) {
            GameObject obj1 = this.gobjs.get(i);
            if (obj1 instanceof Tank || obj1 instanceof Bullet) {
                for (int j = 0; j < this.gobjs.size(); j++) {
                    if(i == j) continue;
                    GameObject obj2 = this.gobjs.get(j);
                    if (obj1.getHitBox().intersects(obj2.getHitBox())) {
                        obj1.collides(obj2);
                    }
                }
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.drawFloor(buffer);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer) );
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.anims.forEach(animation -> animation.drawImage(buffer));
        this.renderSplitScreen(g2);
        this.renderMiniMap(g2);
    }

    public void addGameObject(GameObject obj) {
        this.gobjs.add(obj);
    }
}
