package tankrotationexample.menus;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class PowerUpPanel extends JPanel{
    private final Launcher lf;
    private BufferedImage powerups;

    public PowerUpPanel(Launcher lf) {
        this.lf = lf;
        powerups = ResourceManager.getSprite("powerup");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton control = new JButton("Control");
        control.setFont(new Font("Courier New", Font.BOLD, 24));
        control.setBounds(100, 330, 250, 50);
        control.addActionListener((actionEvent -> this.lf.setFrame("control")));

        JButton start = new JButton("Main Menu");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(100, 390, 250, 50);
        start.addActionListener((actionEvent -> this.lf.setFrame("start")));

        this.add(control);
        this.add(start);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.powerups,0,0,null);
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Health + 1", 165,70);
        g2.drawString("Protection Shield", 165,165);
        g2.drawString("Charges Bullet Faster", 165,265);
    }
}
