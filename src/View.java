import javafx.scene.shape.TriangleMesh;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class View implements Runnable {

    View() {

    }

    public void run(){
        JFrame frame = new JFrame("Bouncing Ball");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600,800);
        frame.setContentPane(new BouncingBall());
        frame.setVisible(true);
    }
}