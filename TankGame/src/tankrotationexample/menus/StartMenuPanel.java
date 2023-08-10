package tankrotationexample.menus;


import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;

    public StartMenuPanel(Launcher lf) {
        this.lf = lf;
        menuBackground = ResourceManager.getSprite("menu");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Start");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(115, 295, 250, 50);
        start.addActionListener(actionEvent -> this.lf.setFrame("game"));

        JButton controlButton = new JButton("Key Control");
        controlButton.setFont(new Font("Courier New", Font.BOLD, 24));
        controlButton.setBounds(115, 360, 250, 50);
        controlButton.addActionListener(actionEvent -> this.lf.setFrame("control"));  // Switch to the control panel

        JButton exit = new JButton("Exit");
        exit.setSize(new Dimension(200, 100));
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(115, 425, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));


        this.add(start);
        this.add(controlButton);
        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }
}
