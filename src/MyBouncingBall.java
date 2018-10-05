import javax.swing.*;
import java.awt.*;

public class MyBouncingBall extends JPanel{
    // Box height and width
    int width;
    int height;

    // Ball Size
    float radius = 40;
    float diameter = radius * 2;

    // Center of Call
    float X = radius + 50;
    float Y = radius + 20;

    // Direction
    float dx = 3;
    float dy = 3;

    //Color
    Color color = Color.BLUE;

    public MyBouncingBall(){
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
    public void frameForward(){ //Should only affect
        width = getWidth();
        height = getHeight();
        //System.out.println("Frame Forward");
        X = X + dx ;
        Y = Y + dy;

        if (X - radius < 0) {
            dx = -dx;
            X = radius;
        } else if (X + radius > width) {
            dx = -dx;
            X = width - radius;
        }

        if (Y - radius < 0) {
            dy = -dy;
            Y = radius;
        } else if (Y + radius > height) {
            dy = -dy;
            Y = height - radius;
        }
    }
    public void changeValues(){
        if(color == Color.BLUE) {color = Color.GRAY;}
        else {color = Color.BLUE;}
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        //System.out.println("painting component");
        g.fillOval((int)(X-radius), (int)(Y-radius), (int)diameter, (int)diameter);
    }
}
