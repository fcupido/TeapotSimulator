import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

public class View implements Runnable {
    private TeaPot [] teaPots;
    private int teaPotCount;
    private JFrame frame;
    private JPanel panel;
    private JLabel fireIcon;
    private boolean [] isDestroyed;

    //TODO Add starting values (needed to decide starting size, instantiate teapots)
    View(String [] info) throws ArrayIndexOutOfBoundsException{
        teaPots = new TeaPot[4];
        isDestroyed = new boolean[4];
        teaPotCount = getTeaPotCount(info);
        for(int i =0; i<teaPotCount; i++) {
            teaPots[i] = extractTeaPot(info[i]); //Starting all the teapot threads
            Thread t = new Thread(teaPots[i]);
            t.start();
        }
        frame = new JFrame("Teapots");
        panel = new JPanel();
        frame.setContentPane(panel);
    }
    private int getTeaPotCount(String[] info) throws ArrayIndexOutOfBoundsException{
        int count = Integer.parseInt(info[0].substring(7,8));
        if(info.length < count){
            count = info.length;
        }
        if(count > 4) count = 4;
        if(count < 0) count = 0;
        return count;
    }
    void update(String [] strings){
        int newTeapotCount = getTeaPotCount(strings);
        if(newTeapotCount == teaPotCount){ //No need to change the panels
            for(int i = 0; i < teaPotCount; i++) {
                stringToTeaPot(strings[i], teaPots[i]); //Loading new values into my teapots
            }
        } else if (newTeapotCount >= 1 && newTeapotCount <= 4){
            teaPotCount = newTeapotCount;
            for(int i =0; i<teaPotCount; i++) {
                if(teaPots[i] == null) {
                    teaPots[i] = extractTeaPot(strings[i]); //Starting all the teapot threads
                    Thread t = new Thread(teaPots[i]);
                    t.start();
                } else {
                    stringToTeaPot(strings[i], teaPots[i]); //Loading new values into my teapots
                }
            }
            makeContentPane();
        }
    }

    private void stringToTeaPot(String info, TeaPot teaPot) {
        int register = getNumberAfter(info,"0x",16);
        boolean power_on = (register & 0x0001) != 0; //if not equal 0, then it's true
        boolean leds_enabled = (register & 0x0002) != 0;//bit 1
        boolean water_enabled = (register & 0x0004) !=0;//bit 2
        boolean heater_enabled = (register &0x0008) != 0;//bit 3
        boolean device_ready = (register & 0x10) != 0; //bit 4
        boolean coffeePotInserted = (register & 0x0800) != 0;// bit 11
        boolean led1 = (register & 0x1000) != 0;//bit 12
        boolean led2 = (register & 0x2000) != 0;//bit 12
        boolean led3 = (register & 0x4000) != 0;//bit 12
        boolean led4 = (register & 0x8000) != 0;//bit 12

        int capacity = getNumberAfter(info,"mWL ",10);
        int currentTemperature = getNumberAfter(info, "CT ",10);
        int waterVolume = getNumberAfter(info,"TW ",10);
        int HR = getNumberAfter(info,"HR ",10);
        int HRB = getNumberAfter(info,"HRB ", 10);
        int frameRate = getNumberAfter(info,"CN ",10);

        teaPot.setHR(HR);
        teaPot.setHRB(HRB);
        teaPot.setCapacity(capacity);
        teaPot.setCoffee_pot_inserted(coffeePotInserted);
        teaPot.setHeater_enabled(heater_enabled);
        teaPot.setDevice_ready(device_ready);
        teaPot.setLed1_on(led1);
        teaPot.setLed2_on(led2);
        teaPot.setLed3_on(led3);
        teaPot.setLed4_on(led4);
        teaPot.setPower_on(power_on);
        teaPot.setSystem_enabled(leds_enabled);
        teaPot.setWater_enabled(water_enabled);
        teaPot.setTemperature(currentTemperature);
        teaPot.setWaterVolume(waterVolume);
        teaPot.setFrameRate(frameRate);
        teaPot.setNotQuit(true);
    }

    private TeaPot extractTeaPot(String info){
        TeaPot temp = new TeaPot();
        stringToTeaPot(info, temp);
        return temp;
    }

    public void run(){
        //frame = new JFrame("Teapots");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        makeContentPane();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void makeContentPane() {
        panel.removeAll();
        switch (teaPotCount){
            case 1:
                frame.setSize(300,300);
                panel.setLayout(new GridLayout(1,1));
                break;
            case 2:
                frame.setSize(600,300);
                panel.setLayout(new GridLayout(1,2));
                break;
            case 3:
                frame.setSize(600,600);
                panel.setLayout(new GridLayout(2,2));
                break;
            case 4:
                frame.setSize(600,600);
                panel.setLayout(new GridLayout(2,2));
                break;
            default:
                System.err.println("No teapots in file");
                System.exit(1);
        }
        for(int i =0; i < teaPotCount; i++){
            panel.add(teaPots[i]);
        }
        panel.repaint();
        panel.revalidate();
    }

    private static int getNumberAfter(String string, String substring, int radix){
        int sequenceIndex = string.indexOf(substring);
        sequenceIndex += substring.length();
        int sequenceEnd = string.indexOf(',',sequenceIndex);
        if(sequenceEnd == -1){
            sequenceEnd = string.length();
        }
        return Integer.parseInt(string, sequenceIndex, sequenceEnd, radix);
    }

    public static void main(String[] args) throws IOException {

        File gif = new File("C:\\Users\\Felipe\\OneDrive\\Desktop\\ODku.gif");
        byte [] mage = Files.readAllBytes(gif.toPath());
        JLabel label = new JLabel(new ImageIcon(mage));

        JFrame f = new JFrame("Animation");
        f.getContentPane().add(label);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setSize(300,300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}