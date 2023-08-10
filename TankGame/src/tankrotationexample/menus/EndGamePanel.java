package tankrotationexample.menus;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {

    private BufferedImage tankRed;
    private BufferedImage tankBlue;

    private final Launcher lf;
    private int winner;

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        tankRed = ResourceManager.getSprite("tank1");
        tankBlue = ResourceManager.getSprite("tank2");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(115, 235, 250, 50);
        start.addActionListener((actionEvent -> this.lf.setFrame("game")));

        JButton menu = new JButton("Main Menu");
        menu.setFont(new Font("Courier New", Font.BOLD, 24));
        menu.setBounds(115, 300, 250, 50);
        menu.addActionListener(actionEvent -> this.lf.setFrame("start"));

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(115, 365, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(start);
        this.add(menu);
        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,GameConstants.END_MENU_SCREEN_WIDTH,GameConstants.END_MENU_SCREEN_HEIGHT);
        g2.setColor(Color.WHITE);
        if(this.winner == 1) {
            g2.drawString("RED TANK WINS!", 130, 60);
            g2.drawImage(this.tankRed,210, 120, null);
        } else {
            g2.drawString("BLUE TANK WINS!", 130, 60);
            g2.drawImage(this.tankBlue,210, 120, null);
        }
    }
}
