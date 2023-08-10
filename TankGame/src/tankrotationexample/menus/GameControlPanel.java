package tankrotationexample.menus;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class GameControlPanel extends JPanel{
    private final Launcher lf;
    private BufferedImage instruction;

    public GameControlPanel(Launcher lf) {
        this.lf = lf;
        instruction = ResourceManager.getSprite("control");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton powerUp = new JButton("Power Ups");
        powerUp.setFont(new Font("Courier New", Font.BOLD, 24));
        powerUp.setBounds(100, 330, 250, 50);
        powerUp.addActionListener((actionEvent -> this.lf.setFrame("powerup")));

        JButton start = new JButton("Main Menu");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(100, 390, 250, 50);
        start.addActionListener((actionEvent -> this.lf.setFrame("start")));

        this.add(powerUp);
        this.add(start);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.instruction,0,0,null);
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("RED TANK", 45,70);
        g2.drawString("BLUE TANK", 305,70);
        g2.drawString("Move", 220,170);
        g2.drawString("Shoot", 220,250);
    }
}
