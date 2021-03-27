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
            f = new JFrame("Reminder 1.0"); //creates a frame for application with title as given in parameter
            f.setSize(WIDTH, HEIGHT); //decide size of application's window
            f.setDefaultCloseOperation(EXIT_ON_CLOSE); //what happens when window X is pressed
            f.setLocation(1250, 500); //location on the screen

            container = getContentPane();
            container.setLayout(new BorderLayout()); //set the layout for the frame container

            menuPanel = new JPanel();
            menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); //create a panel for the menu part of gui

            gifPanel = new JPanel();
            gifPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); //create panel for gif part of gui
            gifAreaText = new JTextArea();
            gifAreaText.setSize(200,100);
            gifAreaText.setEditable(false);

            String[] optionsArray = {"Reminder for water","Reminder for medicine"}; //array required to be passed for dropdown menu

            menuBox = new JComboBox(optionsArray); //comboxbox being used to make drop down menu list
            menuBox.addItemListener(this); //item listener listens for any change in state of the item. Here we are registering the menu drop down as an item to be listened to
        //the this is passed so that current class object is assigned as the listener
            menuBox.setSelectedIndex(0); // a default option to be chosen

        try {

            l1 = new JLabel("Reminder Intervals");
            l2 = new JLabel("minutes");

            String[] intervalArray = {"2","15","30", "45","60"};
            timeIntervalBox = new JComboBox(intervalArray);
            timeIntervalBox.addItemListener(this);
            timeIntervalBox.setSelectedIndex(0);

            reminderButton = new JButton("Remind Me!!!");
            reminderButton.addActionListener( e -> runTheTimer()); // action listener interface is used for buttons n such meaning an action event triggering component like button
            // we are registering the button here to action listener

            stopReminders = new JButton("Stop Reminder!!");
            stopReminders.addActionListener(pause -> resetALl());

//            mediaLabel.setBounds(1300, 400, 50, 50); // the code does not matter at all in case of layouts
            addAllItems();
        } catch (Exception e){
            e.printStackTrace();
        }
        setDefaultGif();
        f.setResizable(false); //do we need to be able to increase /decrease the size of the window
        f.setVisible(true); //if this is not true then whole frame would not appear and we will see nothing on screen
    }

    private void addAllItems(){
        gifPanel.add(mediaLabel); //used to add components to the frame/panel
        gifPanel.add(gifAreaText,FlowLayout.CENTER);
        menuPanel.add(menuBox);
        menuPanel.add(l1);
        menuPanel.add(timeIntervalBox);
        menuPanel.add(l2);
        menuPanel.add(reminderButton);
        f.revalidate(); //refreshes and recreates the components
        //revalidate is the method that cleanly resets everything so that any past component is not visible on the screen
        //official defintion --- is called on a container once new components are added or old ones removed. 
        //this call is an instruction to tell the layout manager to reset based on the new component list
        f.add(gifPanel,BorderLayout.NORTH); //second parameter sets the location of panel in the layout
        f.add(menuPanel, BorderLayout.CENTER);
    }

    private void defaultOperation() throws NullPointerException{ // this function is for setting default values
        currentReminderItem = menuBox.getSelectedItem().toString();
        reminderGif(currentReminderItem);
        currentIntervalString = timeIntervalBox.getSelectedItem().toString(); //getSelectedItem used to get the item that is selected in the menu
        currentInterval = Long.parseLong(currentIntervalString);
        System.out.println("interval is "+ currentInterval);

    }

    public void itemStateChanged(ItemEvent e) throws NullPointerException{ // this function will change the global variables to our need by listening to changes
        if(e.getSource() == menuBox || e.getSource() == timeIntervalBox){ //getSource method used to get the current component that has trigger an item listener event
            currentReminderItem = menuBox.getSelectedItem().toString();
            currentIntervalString = timeIntervalBox.getSelectedItem().toString();
        }
    }

/*    private LocalTime currentTime(){
        currentTimeTracker = new Timer();
        currentTimeTracker.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime = LocalTime.now();
            }
        },0,1000);
        return currentTime;
    }
    */

    private LocalTime upTime(){
        return (upTime = LocalTime.of(currentTime.getHour(),currentTime.getMinute()).plus(currentInterval,ChronoUnit.MINUTES));
    }

    private LocalTime downTime(){
        return (downTime = LocalTime.of(currentTime.getHour(),currentTime.getMinute(), currentTime.getSecond()).plus(currentInterval,ChronoUnit.MINUTES).plus(30, ChronoUnit.SECONDS));
    }

    private void runTheTimer() { //runs while the app is seeming to run in background
        defaultOperation(); //
        timerFlag = true;
        currentTime = LocalTime.now();
        f.setExtendedState(JFrame.ICONIFIED); //setExtendedState used to set state of the frame. In this case, iconified means that it will be seen as icon in the taskbar 
        //extended would have meant it would be fully open

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
            f.setExtendedState(JFrame.NORMAL); //NORMAL means application frame is again on the screen and user can see it easily
            reminderGif(currentReminderItem); //show the gif as per reminder set
            f.setAlwaysOnTop(true); //we want the app to be on top of any applications being run currently. Otherwise it could be missed by users
        }
    }

    class MinimizeScreenTask extends TimerTask {
        public void run() {
            f.setExtendedState(JFrame.ICONIFIED); //if timer is running and interval has not reached then app should go behind or minimize to an icon on taskbar
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
            icon = new ImageIcon(scaled); //scaled image is input here
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
            icon = new ImageIcon(scaled); //ImageIcon can also be used to show GIFs 
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
            monitorTimer.cancel(); //cancels the current running timer
        }
        timerFlag = false; //since timer is cancelled so flag should be false
        defaultOperation();
        setDefaultGif();
        menuPanel.remove(stopReminders);
        gifAreaText.setText("");
        addAllItems();
    }

    private void whenReminding() {
        menuPanel.removeAll(); //remove all components on the screen so that we can add reminder specific components
        menuPanel.add(stopReminders);
        f.add(gifPanel, BorderLayout.CENTER);
        f.add(menuPanel, BorderLayout.PAGE_END);
        f.revalidate(); //revalidate is the method that cleanly resets everything so that any past component is not visible on the screen
        //official defintion --- is called on a container once new components are added or old ones removed. 
        //this call is an instruction to tell the layout manager to reset based on the new component list
        f.repaint();
    }

}
