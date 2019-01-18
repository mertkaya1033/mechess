package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

/**
 * Display.java
 * <p>
 * Description: Within this class all the visuals and the required processing for the game
 * takes place.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Display extends JPanel implements MouseListener,
        MouseMotionListener, ActionListener {

    //represents each scene in the whole game
    public enum Scene {
        GAMEPLAY, MAINMENU, INSTRUCTIONS
    }

    private Image background = new ImageIcon("src/images/Wooden-Background.jpg").getImage().getScaledInstance(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT, 0);
    private Image king = new ImageIcon("src/images/white_KING.png").getImage().getScaledInstance(Constants.FRAME_WIDTH - 200, Constants.FRAME_WIDTH - 200, 0);
    private Scene scene = Scene.MAINMENU;//used to determine the current scene
    private int titleFontSize = 96;//font size for the mechess title
    private Timer tm = new Timer(5, this);//to frequently update the game each 5 ms
    private Button playButton = new Button(this, "Play", Constants.FRAME_WIDTH / 2, 475, 49, Scene.GAMEPLAY);
    private Button instructionsButton = new Button(this, "Instructions", Constants.FRAME_WIDTH / 2, 550, 49, Scene.INSTRUCTIONS);
    private Button mainMenuButton = new Button(this, "Main Menu", Constants.FRAME_WIDTH / 2, 600, 49, Scene.MAINMENU);
    private Board board;//the game board

    /**
     * Display()
     * <p>
     * Description: constructor
     */
    public Display() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        board = new Board();
    }

    /**
     * void paintComponent(Graphics g)
     * <p>
     * Description: This method is where the displaying takes placE
     *
     * @param g the graphics where everything gets displayed
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        if (scene == Scene.GAMEPLAY) {
            board.display(g);
            if (board.getResult().equals("white wins")) {
                drawTextMiddle(g, "WHITE WON", Constants.FRAME_WIDTH / 2, 250, 90, new Color(245, 147, 66));
            } else if (board.getResult().equals("black wins")) {
                drawTextMiddle(g, "BLACK WON", Constants.FRAME_WIDTH / 2, 250, 90, new Color(245, 147, 66));
            } else if (board.getResult().equals("stalemate")) {
                drawTextMiddle(g, "STALEMATE", Constants.FRAME_WIDTH / 2, 250, 90, new Color(245, 147, 66));
            }
            mainMenuButton.display(g);
        } else if (scene == Scene.MAINMENU) {
            mainMenuDisplay(g);
        } else if (scene == Scene.INSTRUCTIONS) {
            instructionsDisplay(g);
        }
        tm.start();
    }

    /**
     * mainMenuDisplay(Graphics g)
     * <p>
     * Description: does the displaying for the main menu scene
     *
     * @param g the graphics where the displaying for the main menu takes place
     */
    private void mainMenuDisplay(Graphics g) {
        drawTextMiddle(g, "MECHESS", Constants.FRAME_WIDTH / 2, 350, titleFontSize, Color.white);
        drawTextMiddle(g, "Mert Kaya", Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT - 100, 30, new Color(255, 255, 255, 125));
        g.drawImage(king, 100, -40, null);
        playButton.display(g);
        instructionsButton.display(g);
    }

    /**
     * instructionsDisplay(Graphics g)
     * <p>
     * Description: does the displaying for the instructions scene
     *
     * @param g the graphics where the displaying for the instructions takes place
     */
    private void instructionsDisplay(Graphics g) {
        drawTextMiddle(g, "INSTRUCTIONS", Constants.FRAME_WIDTH / 2, 120, 60, new Color(255, 255, 255, 125));
        drawTextMiddle(g, "1. click on the square to select it", Constants.FRAME_WIDTH / 2, 300, 20, Color.white);
        drawTextMiddle(g, "2. click on the square where you want to move your piece", Constants.FRAME_WIDTH / 2, 350, 20, Color.white);
        drawTextMiddle(g, "3. if you want to unselect the square, click on it again", Constants.FRAME_WIDTH / 2, 400, 20, Color.white);
        drawTextMiddle(g, "THAT'S IT! NOW YOU CAN PLAY!", Constants.FRAME_WIDTH / 2, 450, 20, Color.white);
        mainMenuButton.display(g);
    }

    /**
     * drawTextMiddle(Graphics g, String txt, int x, int y, int fontSize, Color color)
     * <p>
     * Description: draws string referenced from the middle of the text
     *
     * @param g        the graphics panel of the program
     * @param txt      the text that will be displayed
     * @param x        x position of the text referenced from middle
     * @param y        y position of the text referenced from middle
     * @param fontSize the font size
     * @param color    color of the text
     */
    public void drawTextMiddle(Graphics g, String txt, int x, int y, int fontSize, Color color) {
        int xPos, yPos;
        Font font = new Font("Comic Sans MS", Font.BOLD, fontSize);
        g.setFont(font);
        g.setColor(color);
        xPos = x - this.getFontMetrics(font).stringWidth(txt) / 2;
        yPos = y + this.getFontMetrics(font).getHeight() / 2;
        g.drawString(txt, xPos, yPos);
    }

    /**
     * actionPerformed(ActionEvent e)
     * <p>
     * Description: Recalls the paintComponent method
     *
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    /**
     * mousePressed(MouseEvent e)
     * <p>
     * Description: It runs when the user presses on his/her mouse
     *
     * @param e used to get the x and y position of the mouse
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Scene job;//used to determine if a button has been pressed
        if (scene == Scene.GAMEPLAY) {
            board.clicked(e);
            job = mainMenuButton.clicked(e);
            //job of a main menu button
            if (job != null) {
                scene = job;
                board.reset();
            }
        } else if (scene == Scene.MAINMENU) {
            job = playButton.clicked(e);
            if (job != null) scene = job;

            job = instructionsButton.clicked(e);
            if (job != null) scene = job;
        } else if (scene == Scene.INSTRUCTIONS) {
            job = mainMenuButton.clicked(e);
            if (job != null) scene = job;
        }
        repaint();
    }

    /**
     * mouseMoved(MouseEvent e)
     * <p>
     * Description: It runs when the user moves the mouse
     *
     * @param e used to determine the x and the y position of the mouse
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (scene == Scene.MAINMENU) {
            playButton.mouseOver(e);
            instructionsButton.mouseOver(e);
        } else if (scene == Scene.INSTRUCTIONS) {
            mainMenuButton.mouseOver(e);
        } else if (scene == Scene.GAMEPLAY) {
            mainMenuButton.mouseOver(e);
        }
        repaint();
    }

    /****** UNUSED LISTENER METHODS ******/
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
