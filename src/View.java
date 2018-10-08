import javax.swing.*;
import java.awt.*;

public class View implements Runnable {
    private TeaPot [] teaPots;
    private int teaPotCount;
    private JFrame frame;
    private JPanel panel;

    //TODO Add starting values (needed to decide starting size, instantiate teapots)
    View(String [] info) {
        teaPots = new TeaPot[4];
        teaPotCount = getTeaPotCount(info);
        for(int i =0; i<teaPotCount; i++) {
            teaPots[i] = new TeaPot(extractTeaPot(info[i])); //Starting all the teapot threads
            Thread t = new Thread(teaPots[i]);
            t.start();
        }
        frame = new JFrame("Teapots");
        panel = new JPanel();
        frame.setContentPane(panel);
    }
    private int getTeaPotCount(String[] info){
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
        if(newTeapotCount == teaPotCount){
            for(int i = 0; i < teaPotCount; i++) {
                stringToTeaPot(strings[i], teaPots[i]);
            }
        } else if (newTeapotCount >= 1 && newTeapotCount <= 4){
            for(int i = 0; i < teaPotCount; i++){
                teaPots[i].setNotQuit(false);
            }
            teaPotCount = newTeapotCount;
            for(int i =0; i<teaPotCount; i++) {
                teaPots[i] = new TeaPot(extractTeaPot(strings[i])); //Starting all the teapot threads
                Thread t = new Thread(teaPots[i]);
                t.start();
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

    public static void main (String [] args){
        String [] info = new String[] {
                "P 1, T 4, CR 0x1F0f, TW 200,  HR 10, HRB 10, CT 700, mWL 200, CN 2",
                "P 2, T 4, CR 0xC01F, TW 100,  HR 10, HRB 10, CT 60,  mWL 200, CN 2",
                "P 3, T 4, CR 0x3F1F, TW 140,  HR 10, HRB 10, CT 200, mWL 200, CN 2",
                "P 4, T 4, CR 0x9F1F, TW 500,  HR 10, HRB 10, CT 700, mWL 200, CN 2"};
        System.out.println(getNumberAfter(info[1],"CN ",10));
    }

//    private int getFirstHex(String info){
//        return Integer.parseUnsignedInt(info,info.indexOf("0x")+2,(info.indexOf("0x")+ 6),16);
//    }
}