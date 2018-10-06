import javax.swing.*;
import java.awt.*;

public class View implements Runnable {
    private TeaPot [] teaPots;
    private int teaPotCount;
    //TODO Add starting values (needed to decide starting size, instantiate teapots)
    View(String [] info) {
        teaPots = new TeaPot[4];
        teaPotCount = getTeaPotCount(info);
        for(int i =0; i<teaPotCount; i++) {
            teaPots[i] = new TeaPot(extractTeaPot(info[i])); //Starting all the teapot threads
        }
    }
    private int getTeaPotCount(String[] info){
        int expected = Integer.parseInt(info[0].substring(7,8));
        if(info.length < expected){
            return info.length;
        } else {
            return expected;
        }
    }
    void update(String [] strings){
        for(int i =0; i<teaPotCount; i++) {
            stringToTeaPot(strings[i], teaPots[i]);
        }
    }

    private void stringToTeaPot(String info, TeaPot teaPot) {
        int register = getFirstHex(info);
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

        int capacity = getNumberAfter(info,"mWL ");
        int currentTemperature = getNumberAfter(info, "CT ");
        int waterVolume = getNumberAfter(info,"WL ");

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
    }

    public void run(){
        JFrame frame = new JFrame("Teapots");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        switch (teaPotCount){
            case 0: System.err.println("No teapots in file");
                    System.exit(1);
            case 1:
                frame.setSize(300,300);
                frame.setLayout(new GridLayout(1,1));
                break;
            case 2:
                frame.setSize(600,300);
                frame.setLayout(new GridLayout(1,2));
                break;
            default:
                frame.setSize(600,600);
                frame.setLayout(new GridLayout(2,2));
                break;
        }
        for(int i =0; i < teaPotCount; i++){
            frame.add(teaPots[i]);
        }
        //frame.setContentPane(teaPots[0]);
        frame.setVisible(true);
    }

    private TeaPot extractTeaPot(String info){
        //TODO: add support for 1 and 2 digit TW, CT, mWL
        int register = getFirstHex(info);
        boolean power_on = (register & 0x0001) != 0; //if not equal 0, then it's true
        boolean leds_enabled = (register & 0x0002) != 0;//bit 1
        boolean water_enabled = (register & 0x0004) !=0;//bit 2
        boolean heater_enabled = (register &0x0008) != 0;//bit 3
        boolean coffeePotInserted = (register & 0x0800) != 0;// bit 11
        boolean led1 = (register & 0x1000) != 0;//bit 12
        boolean led2 = (register & 0x2000) != 0;//bit 12
        boolean led3 = (register & 0x4000) != 0;//bit 12
        boolean led4 = (register & 0x8000) != 0;//bit 12

        int capacity = getNumberAfter(info,"mWL ");
        int currentTemperature = getNumberAfter(info, "CT ");
        int waterVolume = getNumberAfter(info,"WL ");

        TeaPot temp = new TeaPot();
        temp.setCapacity(capacity);
        temp.setCoffee_pot_inserted(coffeePotInserted);
        temp.setHeater_enabled(heater_enabled);
        temp.setLed1_on(led1);
        temp.setLed2_on(led2);
        temp.setLed3_on(led3);
        temp.setLed4_on(led4);
        temp.setPower_on(power_on);
        temp.setSystem_enabled(leds_enabled);
        temp.setWater_enabled(water_enabled);
        temp.setTemperature(currentTemperature);
        temp.setWaterVolume(waterVolume);

        return temp;
    }

    private static int getNumberAfter(String string, String substring){
        int sequenceIndex = string.indexOf(substring);
        sequenceIndex += substring.length();
        int sequenceEnd = string.indexOf(',',sequenceIndex);
        if(sequenceEnd == -1){
            sequenceEnd = string.length();
        }
        return Integer.parseInt(string,sequenceIndex,sequenceEnd,10);
    }

    public static void main (String [] args){
        String [] info = new String[] {
                "P 1, T 4, CR 0x1F0f, TW 200,  HR 10, HRB 10, CT 700, mWL 200, CN 2",
                "P 2, T 4, CR 0xC01F, TW 100,  HR 10, HRB 10, CT 60,  mWL 200, CN 2",
                "P 3, T 4, CR 0x3F1F, TW 140,  HR 10, HRB 10, CT 200, mWL 200, CN 2",
                "P 4, T 4, CR 0x9F1F, TW 500,  HR 10, HRB 10, CT 700, mWL 200, CN 2"};
        System.out.println(getNumberAfter(info[1],"CN "));
    }

    private int getFirstHex(String info){
        return Integer.parseUnsignedInt(info,info.indexOf("0x")+2,(info.indexOf("0x")+ 6),16);
    }
}