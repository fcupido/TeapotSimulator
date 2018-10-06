import javax.swing.*;
import java.awt.*;

public class TeaPot extends JPanel{

    private boolean power_on;       //bit 0
    private boolean system_enabled;   //bit 1
    private boolean water_enabled;  //bit 2
    private boolean heater_enabled; //bit 3
    private boolean device_ready;   //bit 4
    private boolean coffee_pot_inserted; //bit 11

    private boolean led1_on; //bit 12
    private boolean led2_on; //bit 13
    private boolean led3_on;//bit 14
    private boolean led4_on;//bit 15

    private int capacity;
    private int waterVolume;
    private int temperature;

    private TeaPot(boolean power_on, boolean system_enabled, boolean water_enabled,
                   boolean heater_enabled, boolean device_ready, boolean coffee_pot_inserted,
                   boolean led1_on, boolean led2_on, boolean led3_on,
                   boolean led4_on, int capacity, int waterVolume, int temperature) {
        this.power_on = power_on;
        this.system_enabled = system_enabled;
        this.water_enabled = water_enabled;
        this.heater_enabled = heater_enabled;
        this.coffee_pot_inserted = coffee_pot_inserted;
        this.device_ready = device_ready;
        this.led1_on = led1_on;
        this.led2_on = led2_on;
        this.led3_on = led3_on;
        this.led4_on = led4_on;
        this.capacity = capacity;
        this.waterVolume = waterVolume;
        this.temperature = temperature;


        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    frameForward();
                    repaint();
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });
        thread.start();
    }

    TeaPot(TeaPot o){
        this(o.power_on,o.system_enabled,o.water_enabled,
                o.heater_enabled,o.device_ready,o.coffee_pot_inserted,
                o.led1_on,o.led2_on,o.led3_on,o.led4_on,
                o.capacity,o.waterVolume,o.temperature);
    }


    TeaPot(){
        //This empty default constructor is required by view, do NOT start a thread here.
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        boolean error_sent = true; //Make false, it is very useful.

        drawHotplate(g);
        drawHeatingElement(g);
        drawLEDs(g);

        if(coffee_pot_inserted) {
            drawTeapotOutline(g);
        }

        if(!power_on){
            drawRedX(g);
            g.setColor(Color.black);
            if (!error_sent)g.drawString("Power is off(bit 0)",10,30);
            error_sent = true;
        } else if (!system_enabled){
            drawWhiteX(g);
            g.setColor(Color.black);
            if(!error_sent)g.drawString("The system is not enabled (bit 1)",10,30);
            error_sent = true;
        }

        if (!coffee_pot_inserted && !error_sent) {
            g.setColor(Color.black);
            g.drawString("Tea pot not inserted (bit 11)", 10, 30);
            error_sent = true;
        }

        //water
        if(water_enabled && coffee_pot_inserted) {
            drawWater(g);
        } else {
            g.setColor(Color.black);
            if(!error_sent)g.drawString("Water is not enabled (bit 2)",10,30);
            error_sent = true;
        }
        if(!device_ready){
            g.setColor(Color.black);
            if(!error_sent)g.drawString("Device is not ready(bit 4)",10,30);
        }

        //bubbles
        drawBubbles(g);
    }

    private void drawBubbles(Graphics g) {
        int boiling_point = 96; // Boiling point in calgary
        if(water_enabled && waterVolume > 0 && coffee_pot_inserted && temperature >= boiling_point) {
            double percentage = waterVolume / (double) capacity * 1.05;
            if (percentage > 1.05) percentage = 1.05;
            g.setColor(new Color(0xff, 0xff, 0xff, 100));
            for (int i = 0; i < (temperature - boiling_point + 5) / 2 + 3; i++) {
                g.fillOval((int) (Math.random() * 113 + 82),
                        158 - (int) (103 * Math.random() * percentage), 10, 10);
            }
        }
    }

    private void drawWater(Graphics g) {
        boolean spill = false;
        double percentage = waterVolume / (double) capacity * 1.05;
        if(waterVolume>capacity){
            percentage = 1.05;
            spill = true;
        }
        int[] xValues = new int[]{86, 200, 203, 203, 200, 86, 83, 83};
        int[] yValues = new int[]{(int) (169 - 112 * percentage), (int) (169 - 112 * percentage),
                (int) (169 - 109 * percentage), (int) (169 - 3 * percentage), 169, 169,
                (int) (169 - 3 * percentage), (int) (169 - 109 * percentage)};

        g.setColor(new Color(78, 119, 139,150));
        g.fillPolygon(xValues, yValues, 8);
        if(spill) {
            g.fillRect(20, 210, 260, 10);
        }
    }

    private void drawLEDs(Graphics g) {
        boolean [] led_colors = new boolean[]{led1_on,led2_on,led3_on,led4_on};
        for(int i =0; i < 4; i++){
            if(led_colors[i] && system_enabled && power_on && heater_enabled && device_ready){
                g.setColor(Color.orange);
            } else {
                g.setColor(Color.black);
            }
            g.fillRect(30 * i + 90, 202, 10,10);
        }
    }

    private void drawHeatingElement(Graphics g) {
        int r = 0;
        int gr = 0;
        int b = 0;
        if(heater_enabled && device_ready && power_on &&  power_on) {
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
        }
        g.setColor(new Color(r,gr,b));
        g.fillRect(70,185,145,10);
    }

    private void drawHotplate(Graphics g) {
        g.setColor(new Color(105,90,60));
        g.fillRect(65,197,155,20);
    }

    private void drawTeapotOutline(Graphics g) {
        int [] xValues = new int []{60,240,215,215,210,75,70,70};
        int [] yValues = new int []{50,50,120,175,180,180,175,80};
        g.setColor(new Color(168,223,230));
        g.fillPolygon(xValues,yValues,8);
    }

    private void drawRedX(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(15));
        g2.setColor(new Color(255,0,0));
        g2.drawLine(0,getWidth(),getWidth(),0);
        g2.drawLine(0,0,getHeight(),getHeight());
    }

    private void drawWhiteX(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(15));
        g2.setColor(new Color(255,255,255));
        g2.drawLine(0,getWidth(),getWidth(),0);
        g2.drawLine(0,0,getHeight(),getHeight());
    }


    private void frameForward(){

    }

    public static void main (String [] agrs){
        JFrame frame = new JFrame("Teapots");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setContentPane(new TeaPot(true,true,true,
                true,true,true,
                true,true,false,
                false,1000,1000,100));
        frame.setVisible(true);
    }

    public boolean isDevice_ready() {
        return device_ready;
    }

    void setDevice_ready(boolean device_ready) {
        this.device_ready = device_ready;
    }

    public boolean isPower_on() {
        return power_on;
    }

    void setPower_on(boolean power_on) {
        this.power_on = power_on;
    }

    public boolean isSystem_enabled() {
        return system_enabled;
    }

    void setSystem_enabled(boolean system_enabled) {
        this.system_enabled = system_enabled;
    }

    public boolean isWater_enabled() {
        return water_enabled;
    }

    void setWater_enabled(boolean water_enabled) {
        this.water_enabled = water_enabled;
    }

    public boolean isHeater_enabled() {
        return heater_enabled;
    }

    void setHeater_enabled(boolean heater_enabled) {
        this.heater_enabled = heater_enabled;
    }

    public boolean isCoffee_pot_inserted() {
        return coffee_pot_inserted;
    }

    void setCoffee_pot_inserted(boolean coffee_pot_inserted) {
        this.coffee_pot_inserted = coffee_pot_inserted;
    }

    public boolean isLed1_on() {
        return led1_on;
    }

    void setLed1_on(boolean led1_on) {
        this.led1_on = led1_on;
    }

    public boolean isLed2_on() {
        return led2_on;
    }

    void setLed2_on(boolean led2_on) {
        this.led2_on = led2_on;
    }

    public boolean isLed3_on() {
        return led3_on;
    }

    void setLed3_on(boolean led3_on) {
        this.led3_on = led3_on;
    }

    public boolean isLed4_on() {
        return led4_on;
    }

    void setLed4_on(boolean led4_on) {
        this.led4_on = led4_on;
    }

    public int getCapacity() {
        return capacity;
    }

    void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getWaterVolume() {
        return waterVolume;
    }

    void setWaterVolume(int waterVolume) {
        this.waterVolume = waterVolume;
    }

    public int getTemperature() {
        return temperature;
    }

    void setTemperature(int temperature) {
        this.temperature = temperature;
    }

}

