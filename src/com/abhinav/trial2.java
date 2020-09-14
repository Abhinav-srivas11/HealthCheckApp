package com.abhinav;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

public class trial2 extends JFrame {

    public trial2() {

        initUI();
    }

    private void initUI() {

        ImageIcon originalIcon = new ImageIcon("C:\\Users\\91878\\Downloads\\HealthCheckApp\\src\\com\\abhinav\\images\\name.jpg");
        JLabel originalLabel = new JLabel(originalIcon);

        int width = originalIcon.getIconWidth() / 2;
        int height = originalIcon.getIconHeight() / 2;
        System.out.println(width);

        Image scaled = scaleImage(originalIcon.getImage(), width, height);

        ImageIcon scaledIcon = new ImageIcon(scaled);

        JLabel newLabel = new JLabel(scaledIcon);

        createLayout(originalLabel, newLabel);

        setTitle("Scaled icon");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Image scaleImage(Image image, int w, int h) {

        Image scaled = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);

        return scaled;
    }

    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
        );

        gl.setVerticalGroup(gl.createParallelGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
        );

        pack();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            trial2 ex = new trial2();
            ex.setVisible(true);
        });
    }
}
