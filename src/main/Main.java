package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
    private static JFrame frame;
    private static JPanel panel;
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
