package com.abhinav;

import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class HealthCheckUp extends JFrame implements ItemListener {
    final int WIDTH = 270;
    final int HEIGHT = 325;
    Container container;
    boolean timerFlag = false;
    JComboBox menuBox;
    JComboBox timeIntervalBox;
    long currentInterval;
    String currentIntervalString;
    String currentReminderItem;
    ImageIcon icon;
    JButton reminderButton;
    JPanel menuPanel;
    JPanel gifPanel;
    JLabel mediaLabel = new JLabel();
    JLabel l1;
    JLabel l2;
    JTextArea gifAreaText;
    JFrame f;
    JButton stopReminders;
    LocalTime currentTime;
    LocalTime upTime;
    LocalTime downTime;
    java.util.Timer monitorTimer;
    java.util.Timer currentTimeTracker;
    public HealthCheckUp() {
            f = new JFrame("Reminder 1.0");
            f.setSize(WIDTH, HEIGHT);
            f.setDefaultCloseOperation(EXIT_ON_CLOSE);
            f.setLocation(1250, 500);

            container = getContentPane();
            container.setLayout(new BorderLayout());

            menuPanel = new JPanel();
            menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

            gifPanel = new JPanel();
            gifPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            gifAreaText = new JTextArea();
            gifAreaText.setSize(200,100);
            gifAreaText.setEditable(false);

            String[] optionsArray = {"Reminder for water","Reminder for medicine"};

            menuBox = new JComboBox(optionsArray);
            menuBox.addItemListener(this);
            menuBox.setSelectedIndex(0);

        try {

            l1 = new JLabel("Reminder Intervals");
            l2 = new JLabel("minutes");

            String[] intervalArray = {"2","15","30", "45","60"};
            timeIntervalBox = new JComboBox(intervalArray);
            timeIntervalBox.addItemListener(this);
            timeIntervalBox.setSelectedIndex(0);

            reminderButton = new JButton("Remind Me!!!");
            reminderButton.addActionListener( e -> runTheTimer());

            stopReminders = new JButton("Stop Reminder!!");
            stopReminders.addActionListener(pause -> resetALl());

//            mediaLabel.setBounds(1300, 400, 50, 50); // the code does not matter at all in case of layouts
            addAllItems();
        } catch (Exception e){
            e.printStackTrace();
        }
        setDefaultGif();
        f.setResizable(false);
        f.setVisible(true);
    }

    private void addAllItems(){
        gifPanel.add(mediaLabel);
        gifPanel.add(gifAreaText,FlowLayout.CENTER);
        menuPanel.add(menuBox);
        menuPanel.add(l1);
        menuPanel.add(timeIntervalBox);
        menuPanel.add(l2);
        menuPanel.add(reminderButton);
        f.revalidate();
        f.add(gifPanel,BorderLayout.NORTH);
        f.add(menuPanel, BorderLayout.CENTER);
    }

    private void defaultOperation() throws NullPointerException{ // this function is for setting default values
        currentReminderItem = menuBox.getSelectedItem().toString();
        reminderGif(currentReminderItem);
        currentIntervalString = timeIntervalBox.getSelectedItem().toString();
        currentInterval = Long.parseLong(currentIntervalString);
        System.out.println("interval is "+ currentInterval);

    }

    public void itemStateChanged(ItemEvent e) throws NullPointerException{ // this function will change the global variables to our need by listening to changes
        if(e.getSource() == menuBox || e.getSource() == timeIntervalBox){
            currentReminderItem = menuBox.getSelectedItem().toString();
            currentIntervalString = timeIntervalBox.getSelectedItem().toString();
        }
    }

    private LocalTime currentTime(){
        currentTimeTracker = new Timer();
        currentTimeTracker.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime = LocalTime.now();
            }
        },0,1000);
        return currentTime;
    }

    private LocalTime upTime(){
        return (upTime = LocalTime.of(currentTime.getHour(),currentTime.getMinute()).plus(currentInterval,ChronoUnit.MINUTES));
    }

    private LocalTime downTime(){
        return (downTime = LocalTime.of(currentTime.getHour(),currentTime.getMinute(), currentTime.getSecond()).plus(currentInterval,ChronoUnit.MINUTES).plus(30, ChronoUnit.SECONDS));
    }

    private void runTheTimer() {
        defaultOperation();
        timerFlag = true;
        currentTime = LocalTime.now();
        f.setExtendedState(JFrame.ICONIFIED);

        monitorTimer = new Timer();
        //bring up the reminder with below line
        monitorTimer.scheduleAtFixedRate(new RemindTask(), 0, currentInterval * 60 * 1000);
        //minimize the application window automatically after delay of 30 seconds
        monitorTimer.scheduleAtFixedRate(new MinimizeScreenTask(), 0, currentInterval * 60 * 1000 + 30 * 1000);
    }
    //below is the earlier logic that we used
//        monitorTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                LocalTime check = currentTime();
//                if(check.getHour() == showReminder[0].getHour() && check.getMinute() == showReminder[0].getMinute()){
//                    f.setExtendedState(JFrame.NORMAL);
//                    reminderGif(currentReminderItem);
//                    showReminder[0] = upTime();
//                    f.setAlwaysOnTop(true);
//                }
//                if(check.getHour() == hideScreen[0].getHour() && check.getMinute() == hideScreen[0].getMinute() && hideScreen[0].getSecond() == check.getSecond()){
//                    f.setExtendedState(JFrame.ICONIFIED);
//                    hideScreen[0] = downTime;
//                }
//            }
//        },0,1000);//period is so that the code runs at delay of 1 sec


    class RemindTask extends TimerTask {
        public void run() {
            f.setExtendedState(JFrame.NORMAL);
            reminderGif(currentReminderItem);
            f.setAlwaysOnTop(true);
        }
    }

    class MinimizeScreenTask extends TimerTask {
        public void run() {
            f.setExtendedState(JFrame.ICONIFIED);
        }
    }

    private void reminderGif(String item){
        switch(item){
            case "Reminder for water":
                waterReminderMethod();
                break;
            case "Reminder for medicine":
                medicineReminderMethod();
                break;
            default:
                setDefaultGif();
                break;
        }
    }

    private void waterReminderMethod() {
        try {
            icon = new ImageIcon("C:\\Users\\91878\\Downloads\\HealthCheckApp\\src\\com\\abhinav\\images\\drinking.gif");
            Image scaled = icon.getImage().getScaledInstance( 150, 150, Image.SCALE_DEFAULT);
            icon = new ImageIcon(scaled);
            mediaLabel.setIcon(icon);
            gifAreaText.setText("Time to drink some water.\n See you again soon!");
            whenReminding();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void medicineReminderMethod() {
        try {
            icon = new ImageIcon("C:\\Users\\91878\\Downloads\\HealthCheckApp\\src\\com\\abhinav\\images\\medicine.gif");
            Image scaled = icon.getImage().getScaledInstance( 150, 150, Image.SCALE_DEFAULT);
            icon = new ImageIcon(scaled);
            mediaLabel.setIcon(icon);
            gifAreaText.setText("Time to have your medicine");
            whenReminding();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    I can make a list with all the path for gifs saved already and have only one method that will need the path parameter which can then
    be passed to this default gif function that will save lines of code and base on the principle DRY
    */

    private void setDefaultGif() {
        icon = new ImageIcon("C:\\Users\\91878\\Downloads\\HealthCheckApp\\src\\com\\abhinav\\images\\reminder.gif");
        Image scaled = icon.getImage().getScaledInstance( 190, 150, Image.SCALE_DEFAULT);
        icon = new ImageIcon(scaled);
        mediaLabel.setIcon(icon);
    }

    private void resetALl() {
        if(timerFlag) {
//            currentTimeTracker.cancel();
            monitorTimer.cancel();
        }
        timerFlag = false;
        defaultOperation();
        setDefaultGif();
        menuPanel.remove(stopReminders);
        gifAreaText.setText("");
        addAllItems();
    }

    private void whenReminding() {
        menuPanel.removeAll();
        menuPanel.add(stopReminders);
        f.add(gifPanel, BorderLayout.CENTER);
        f.add(menuPanel, BorderLayout.PAGE_END);
        f.revalidate();
        f.repaint();
    }

}
