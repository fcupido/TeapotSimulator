import javax.swing.*;
import java.awt.*;

public class TeaPot extends JPanel{
    int width = 300;
    int height = 300;

    private boolean power_on;       //bit 0
    private boolean leds_enabled;   //bit 1
    private boolean water_enabled;  //bit 2
    private boolean heater_enabled; //bit 3

    private boolean coffe_pot_inserted; //bit 11

    private boolean led1_on; //bit 12
    private boolean led2_on; //bit 13
    private boolean led3_on;//bit 14
    private boolean led4_on;//bit 15

    private int capacity;
    private int waterVolume;
    private int temperature;

    public TeaPot(boolean power_on, boolean leds_enabled, boolean water_enabled,
                  boolean heater_enabled, boolean coffe_pot_inserted,
                  boolean led1_on, boolean led2_on, boolean led3_on,
                  boolean led4_on, int capacity, int waterVolume, int temperature) {
        this.power_on = power_on;
        this.leds_enabled = leds_enabled;
        this.water_enabled = water_enabled;
        this.heater_enabled = heater_enabled;
        this.coffe_pot_inserted = coffe_pot_inserted;
        this.led1_on = led1_on;
        this.led2_on = led2_on;
        this.led3_on = led3_on;
        this.led4_on = led4_on;
        this.capacity = capacity;
        this.waterVolume = waterVolume;
        this.temperature = temperature;


        Thread thread = new Thread() {
            public void run()
            {
                while (true) {
                    try {
                        frameForward();
                        repaint();
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        };
        thread.start();
    }

    TeaPot(TeaPot o){
        this(o.power_on,o.leds_enabled,o.water_enabled,o.heater_enabled,o.coffe_pot_inserted,o.led1_on,o.led2_on,o.led3_on,o.led4_on,o.capacity,o.waterVolume,o.temperature);
    }

    public TeaPot (){}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));

        if(!power_on){//red X
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(15));
            g2.setColor(new Color(255,0,0));
            g2.drawLine(0,getWidth(),getWidth(),0);
            g2.drawLine(0,0,getHeight(),getHeight());
            g.setColor(Color.black);
            g.drawString("Power is off",10,30);
            return;
        } else if (power_on && !leds_enabled){ //White x
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(15));
            g2.setColor(new Color(255,255,255));
            g2.drawLine(0,getWidth(),getWidth(),0);
            g2.drawLine(0,0,getHeight(),getHeight());
            g.setColor(Color.black);
            g.drawString("LEDs are not enabled",10,30);
            return;
        }
        //teapot
        //TODO: make it look nice
        int [] xValues = new int []{60,240,215,215,210,75,70,70};
        int [] yValues = new int []{50,50,120,175,180,180,175,80};
        g.setColor(new Color(168,223,230));
        g.fillPolygon(xValues,yValues,8);
        //hotplate
        g.setColor(new Color(105,90,60));
        g.fillRect(65,197,155,20);

        //heating element
        int r = 0;
        int gr = 0;
        int b = 0;
        if(heater_enabled) {
            if (temperature > 0 && temperature < 255) {
                r = temperature;
            } else if (temperature > 255 && temperature < 255 + 255) {
                r = 255;
                gr = temperature - 255;
                b = temperature - 255;
            } else if (temperature > 255 + 255) {
                r = 255;
                gr = 255;
                b = 255;
            }
        } else {
            g.setColor(Color.black);
            g.drawString("Heater is not enabled (bit 3)",10,30);
        }
        g.setColor(new Color(r,gr,b));
        g.fillRect(70,185,145,10);

        //LEDs
        if(leds_enabled);
        boolean [] led_colors = new boolean[]{led1_on,led2_on,led3_on,led4_on};
        for(int i =0; i < 4; i++){
            if(led_colors[i]){
                g.setColor(Color.orange);
            } else {
                g.setColor(Color.black);
            }
            g.fillRect(30 * i + 90, 202, 10,10);
        }
        //water
        if(water_enabled) {
            boolean spill = false;
            double percentage = waterVolume / (double) capacity * 1.05;
            if(waterVolume>capacity){
                percentage = 1.05;
                spill = true;
            }
            xValues = new int[]{86, 200, 203, 203, 200, 86, 83, 83};
            yValues = new int[]{(int) (169 - 112 * percentage), (int) (169 - 112 * percentage),
                    (int) (169 - 109 * percentage), (int) (169 - 3 * percentage), 169, 169,
                    (int) (169 - 3 * percentage), (int) (169 - 109 * percentage)};

            g.setColor(new Color(78, 119, 139,150));
            g.fillPolygon(xValues, yValues, 8);
            if(spill) {
                g.fillRect(20, 210, 260, 10);
            }
        } else if (heater_enabled){
            g.setColor(Color.black);
            g.drawString("Water is not enabled (bit 2)",10,30);
            return;
        }
        //bubbles

        //TODO: bubbles move upward instead of random
        if(heater_enabled) {
            if (temperature >= 212) {
                double percentage = waterVolume / (double) capacity * 1.05;
                if(percentage > 1.05) percentage = 1.05;
                g.setColor(new Color(0xff, 0xff, 0xff, 100));
                for (int i = 0; i < (temperature - 209) / 2 + 3; i++) {
                    g.fillOval((int) (Math.random() * 113 + 82),
                            158 - (int)(103 * Math.random() * percentage), 10, 10);
                }//(int) (Math.random() * 103 + 56)
            }
        }
    }


    public void frameForward(){

    }

    public static void main (String [] agrs){
        JFrame frame = new JFrame("Teapots");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setContentPane(new TeaPot(true,true,true,
                true,true,
                true,true,false,
                false,1000,1000,100));
        frame.setVisible(true);
    }

    public boolean isPower_on() {
        return power_on;
    }

    public void setPower_on(boolean power_on) {
        this.power_on = power_on;
    }

    public boolean isLeds_enabled() {
        return leds_enabled;
    }

    public void setLeds_enabled(boolean leds_enabled) {
        this.leds_enabled = leds_enabled;
    }

    public boolean isWater_enabled() {
        return water_enabled;
    }

    public void setWater_enabled(boolean water_enabled) {
        this.water_enabled = water_enabled;
    }

    public boolean isHeater_enabled() {
        return heater_enabled;
    }

    public void setHeater_enabled(boolean heater_enabled) {
        this.heater_enabled = heater_enabled;
    }

    public boolean isCoffe_pot_inserted() {
        return coffe_pot_inserted;
    }

    public void setCoffe_pot_inserted(boolean coffe_pot_inserted) {
        this.coffe_pot_inserted = coffe_pot_inserted;
    }

    public boolean isLed1_on() {
        return led1_on;
    }

    public void setLed1_on(boolean led1_on) {
        this.led1_on = led1_on;
    }

    public boolean isLed2_on() {
        return led2_on;
    }

    public void setLed2_on(boolean led2_on) {
        this.led2_on = led2_on;
    }

    public boolean isLed3_on() {
        return led3_on;
    }

    public void setLed3_on(boolean led3_on) {
        this.led3_on = led3_on;
    }

    public boolean isLed4_on() {
        return led4_on;
    }

    public void setLed4_on(boolean led4_on) {
        this.led4_on = led4_on;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getWaterVolume() {
        return waterVolume;
    }

    public void setWaterVolume(int waterVolume) {
        this.waterVolume = waterVolume;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

}

