import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TeaPot extends JPanel implements Runnable, FireImage {

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

    private int HR;
    private int HRB;

    private int capacity;
    private int waterVolume;
    private int temperature;
    private int frameRate;

    private boolean notQuit;
    private boolean destroyed;


    TeaPot(boolean power_on, boolean system_enabled, boolean water_enabled,
           boolean heater_enabled, boolean device_ready, boolean coffee_pot_inserted,
           int HR, int HRB,
           boolean led1_on, boolean led2_on, boolean led3_on,
           boolean led4_on, int capacity, int waterVolume, int temperature,
           int frameRate) {
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
        this.HR = HR;
        this.HRB = HRB;
        this.capacity = capacity;
        this.waterVolume = waterVolume;
        this.temperature = temperature;
        this.frameRate = frameRate;
        notQuit = true;
        destroyed = temperature > 250;
    }

    public void run() {
        while (notQuit) {
            try {
                repaint();
                Thread.sleep(1000 / frameRate);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static void main (String [] args){
        File gif = new File("C:\\Users\\Felipe\\OneDrive\\Documents\\School\\ENCM 511\\Animation\\src\\145.gif");
        byte[] mage = new byte[0];
        try {
            mage = Files.readAllBytes(gif.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("byte [] image = new byte [] {");
        for(int i = 0; i < mage.length; i++){
            System.out.print(mage[i]);
            System.out.print(",");
            if(i%50 == 49) System.out.println();
        }
        System.out.println("};");
    }

    TeaPot(){}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        boolean error_sent = false; //Make false, it is very useful.

        showFrameRate(g);
        drawHotplate(g);
        drawHR(g);
        drawLEDs(g);

        if (coffee_pot_inserted) {
            drawTeapotOutline(g);
        }

        if (!power_on) {
            drawRedX(g);
            g.setColor(Color.black);
            if (!error_sent) g.drawString("Power is off(bit 0)", 10, 30);
            error_sent = true;
        } else if (!system_enabled) {
            drawWhiteX(g);
            g.setColor(Color.black);
            if (!error_sent) g.drawString("The system is not enabled (bit 1)", 10, 30);
            error_sent = true;
        }

        if (!coffee_pot_inserted && !error_sent) {
            g.setColor(Color.black);
            g.drawString("Tea pot not inserted (bit 11)", 10, 30);
            error_sent = true;
        }

        //water
        if (water_enabled && coffee_pot_inserted) {
            drawWater(g);
        } else {
            g.setColor(Color.black);
            if (!error_sent) g.drawString("Water is not enabled (bit 2)", 10, 30);
            error_sent = true;
        }
        if (!heater_enabled && !error_sent) {
            g.setColor(Color.black);
            if (!error_sent) g.drawString("Heater is not enabled (bit 3)", 10, 30);
            error_sent = true;
        }
        if (!device_ready) {
            g.setColor(Color.black);
            if (!error_sent) g.drawString("Device is not ready(bit 4)", 10, 30);
        }


        //bubbles
        drawBubbles(g);
        if(!destroyed && temperature > 250 && system_enabled && heater_enabled && power_on && device_ready){
            destroyed = true;
            addFire();
            System.out.println("Added fire");
        }
    }

    private void showFrameRate(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        g.setColor(Color.black);
        g.drawString(frameRate + " Hz", 10, 260);
    }

    private void drawBubbles(Graphics g) {
        int boiling_point = 96; // Boiling point in calgary
        if (water_enabled && waterVolume > 0 && coffee_pot_inserted && temperature >= boiling_point && device_ready) {
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
        if (waterVolume > capacity) {
            percentage = 1.05;
            spill = true;
        }
        int[] xValues = new int[]{86, 200, 203, 203, 200, 86, 83, 83};
        int[] yValues = new int[]{(int) (169 - 112 * percentage), (int) (169 - 112 * percentage),
                (int) (169 - 109 * percentage), (int) (169 - 3 * percentage), 169, 169,
                (int) (169 - 3 * percentage), (int) (169 - 109 * percentage)};

        g.setColor(new Color(78, 119, 139, 150));
        g.fillPolygon(xValues, yValues, 8);
        if (spill) {
            g.fillRect(20, 210, 260, 10);
        }
    }

    private void drawLEDs(Graphics g) {
        boolean[] led_colors = new boolean[]{led1_on, led2_on, led3_on, led4_on};
        for (int i = 0; i < 4; i++) {
            if (led_colors[i] && system_enabled && power_on && device_ready) {
                g.setColor(Color.orange);
            } else {
                g.setColor(Color.black);
            }
            g.fillRect(30 * i + 90, 202, 10, 10);
        }
    }

    private void drawHR(Graphics g) {
        int HRr = 0;
        int HRBr = 0;
        if (heater_enabled && device_ready && power_on) {
            if (HR != 0 && HRB != 0 && HRB <= 15) {
                if (HRB <= 7) {
                    HRBr = HRB * 36;
                } else {
                    HRBr = 0xff;
                }
                HRr = (HR - 20) * 6;
                if (HRr < 0) HRr = 0;
                if (HRr > 255) HRr = 255;
            }
        }
        g.setColor(new Color(HRBr, 0, 0));
        g.fillRect(70, 180, 145, 5);

        g.setColor(new Color(HRr, 0, 0));
        g.fillRect(70, 185, 145, 10);
    }

    private void drawHotplate(Graphics g) {
        g.setColor(new Color(105, 90, 60));
        g.fillRect(65, 197, 155, 20);
    }

    private void drawTeapotOutline(Graphics g) {
        int[] xValues = new int[]{60, 240, 215, 215, 210, 75, 70, 70};
        int[] yValues = new int[]{50, 50, 120, 175, 180, 180, 175, 80};
        g.setColor(new Color(168, 223, 230));
        g.fillPolygon(xValues, yValues, 8);
    }

    private void drawRedX(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(15));
        g2.setColor(new Color(255, 0, 0));
        g2.drawLine(0, getWidth(), getWidth(), 0);
        g2.drawLine(0, 0, getHeight(), getHeight());
    }

    private void drawWhiteX(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(15));
        g2.setColor(new Color(255, 255, 255));
        g2.drawLine(0, getWidth(), getWidth(), 0);
        g2.drawLine(0, 0, getHeight(), getHeight());
    }

    void setFrameRate(int frameRate) {
        if (frameRate > 0 && frameRate <= 1000) {
            this.frameRate = frameRate;
        } else {
            this.frameRate = 1;
        }
    }

    private void addFire() {
        setLayout(new BorderLayout());
        add(Box.createVerticalStrut(30), BorderLayout.PAGE_END);
        add(Box.createHorizontalStrut(15), BorderLayout.LINE_END);

        File gif = new File("C:\\Users\\Felipe\\OneDrive\\Documents\\School\\ENCM 511\\Animation\\src\\145.gif");
        byte[] mage = new byte[0];
        try {
            mage = Files.readAllBytes(gif.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(new JLabel(new ImageIcon(mage)),BorderLayout.CENTER);
        revalidate();
    }

    void setDevice_ready(boolean device_ready) {
        this.device_ready = device_ready;
    }

    void setHR(int HR) {
        this.HR = HR;
    }

    void setHRB(int HRB) {
        this.HRB = HRB;
    }

    void setPower_on(boolean power_on) {
        this.power_on = power_on;
    }

    void setSystem_enabled(boolean system_enabled) {
        this.system_enabled = system_enabled;
    }

    void setWater_enabled(boolean water_enabled) {
        this.water_enabled = water_enabled;
    }

    void setHeater_enabled(boolean heater_enabled) {
        this.heater_enabled = heater_enabled;
    }

    void setCoffee_pot_inserted(boolean coffee_pot_inserted) {
        this.coffee_pot_inserted = coffee_pot_inserted;
    }

    void setLed1_on(boolean led1_on) {
        this.led1_on = led1_on;
    }

    void setLed2_on(boolean led2_on) {
        this.led2_on = led2_on;
    }

    void setLed3_on(boolean led3_on) {
        this.led3_on = led3_on;
    }

    void setLed4_on(boolean led4_on) {
        this.led4_on = led4_on;
    }

    void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    void setWaterVolume(int waterVolume) {
        this.waterVolume = waterVolume;
    }

    void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public boolean isNotQuit() {
        return notQuit;
    }

    public void setNotQuit(boolean notQuit) {
        this.notQuit = notQuit;
    }
}

