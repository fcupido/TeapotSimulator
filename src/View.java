import javax.swing.*;

public class View implements Runnable {
    TeaPot teaPot;

    //TODO Add starting values (needed to decide starting size, instantiate teapots)
    View(String [] info) {
        teaPot = new TeaPot(extractTeaPot(info[0]));
    }
    void update(String [] strings){
        String info = strings[0];
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

        int capacity = Integer.parseInt(info,info.indexOf("mWL")+4,
                info.lastIndexOf(','),10);
        int currentTemperature = Integer.parseInt(info,info.indexOf("CT") + 3,
                info.indexOf("mWL") - 2,10);

        teaPot.setCapacity(capacity);
        teaPot.setCoffee_pot_inserted(coffeePotInserted);
        teaPot.setHeater_enabled(heater_enabled);
        teaPot.setLed1_on(led1);
        teaPot.setLed2_on(led2);
        teaPot.setLed3_on(led3);
        teaPot.setLed4_on(led4);
        teaPot.setPower_on(power_on);
        teaPot.setLeds_enabled(leds_enabled);
        teaPot.setWater_enabled(water_enabled);
        teaPot.setTemperature(currentTemperature);
        teaPot.setWaterVolume(Integer.parseInt(info,info.indexOf("TW") + 3,
                info.indexOf("HR") -3,10));
    }

    public void run(){
        JFrame frame = new JFrame("Bouncing Ball");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setContentPane(teaPot);
        frame.setVisible(true);
    }

    TeaPot extractTeaPot(String info){
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

        int capacity = Integer.parseInt(info,info.indexOf("mWL")+4,
                info.lastIndexOf(','),10);
        int currentTemperature = Integer.parseInt(info,info.indexOf("CT") + 3,
                info.indexOf("mWL") - 2,10);

        TeaPot temp = new TeaPot();
        temp.setCapacity(capacity);
        temp.setCoffee_pot_inserted(coffeePotInserted);
        temp.setHeater_enabled(heater_enabled);
        temp.setLed1_on(led1);
        temp.setLed2_on(led2);
        temp.setLed3_on(led3);
        temp.setLed4_on(led4);
        temp.setPower_on(power_on);
        temp.setLeds_enabled(leds_enabled);
        temp.setWater_enabled(water_enabled);
        temp.setTemperature(currentTemperature);
        temp.setWaterVolume(Integer.parseInt(info,info.indexOf("TW") + 3,
                info.indexOf("HR") -3,10));

        return temp;
    }

    public static void main (String [] args){
        String [] info = new String[] {
                "P 1, T 4, CR 0x1F0f, TW 200,  HR 10, HRB 10, CT 700, mWL 200, CN 2",
                "P 2, T 4, CR 0xC01F, TW 100,  HR 10, HRB 10, CT 60,  mWL 200, CN 2",
                "P 3, T 4, CR 0x3F1F, TW 140,  HR 10, HRB 10, CT 200, mWL 200, CN 2",
                "P 4, T 4, CR 0x9F1F, TW 500,  HR 10, HRB 10, CT 700, mWL 200, CN 2"};
        View view = new View(info);
        view.run();
    }

    int getFirstHex (String info){
        return Integer.parseUnsignedInt(info,info.indexOf("0x")+2,(info.indexOf("0x")+ 6),16);
    }
}