package com.abhinav;
//import org.w3c.dom.ls.LSOutput;
//
//import java.awt.*;
//import java.util.*;
//import java.awt.event.*;
//public class trials {
//    public static void main(String[] args) {
//        Frame f=new Frame("ActionListener Example");
//        final TextField tf=new TextField();
//        tf.setBounds(50,50, 150,20);
//        Button b=new Button("Click Here");
//        b.setBounds(50,100,60,30);
//        Button b2 =new Button("Click Here");
//        b2.setBounds(150,100,60,30);
//
//        b.addActionListener( e-> f.setExtendedState(Frame.MAXIMIZED_BOTH));
//        b2.addActionListener( e -> f.setExtendedState(Frame.NORMAL));
//
//        f.add(b);f.add(b2);f.add(tf);
//        f.setSize(400,400);
//        f.setLayout(null);
//
//        f.setVisible(true);
//    }
//}
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class trials extends JFrame {

    public trials() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JPanel panel1 = new JPanel();
        panel1.setBackground(Color.RED);
        final JPanel panel2 = new JPanel();
        panel2.setBackground(Color.BLUE);

        JButton button = new JButton("ADD AND REMOVE PANEL");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (panel1.isShowing()) {
                    remove(panel1);
                    add(panel2, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } else if (panel2.isShowing()) {
                    remove(panel2);
                    add(panel1, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                }
            }
        });

        add(panel1, BorderLayout.CENTER);
        add(button, BorderLayout.PAGE_END);

        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(new  Runnable() {
            @Override
            public void run() {
                new trials();
            }
        });
    }
}