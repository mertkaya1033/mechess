package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main.java
 * <p>
 * Description: This is the class which contains the main method of the whole program.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Main {
    private static JFrame frame;
    private static JPanel panel;

    /**
     * main(String[] args)
     * <p>
     * Description: main method
     *
     * @param args unused
     */
    public static void main(String[] args) {
        frame = new JFrame("Chess");
        panel = new Display();
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
    }


}
