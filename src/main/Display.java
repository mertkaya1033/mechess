package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;


public class Display extends JPanel implements MouseListener,
        MouseMotionListener, ActionListener {

    public enum Scene {
        GAMEPLAY, MAINMENU, INSTRUCTIONS
    }

    private Image background = new ImageIcon("src/images/Wooden-Background.jpg").getImage().getScaledInstance(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT, 0);
    private Image king = new ImageIcon("src/images/white_KING.png").getImage().getScaledInstance(Constants.FRAME_WIDTH - 200, Constants.FRAME_WIDTH - 200, 0);
    private Scene scene = Scene.MAINMENU;
    private int titleFontSize = 96;
    private Timer tm = new Timer(5, this);
    private Button playButton = new Button(this, "Play", Constants.FRAME_WIDTH / 2, 475, 49, Scene.GAMEPLAY);
    private Button instructionsButton = new Button(this, "Instructions", Constants.FRAME_WIDTH / 2, 550, 49, Scene.INSTRUCTIONS);
    private Button mainMenuButton = new Button(this, "Main Menu", Constants.FRAME_WIDTH / 2, 600, 49, Scene.MAINMENU);
    private Board board;

    /**
     *
     */
    public Display() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        board = new Board();
    }

    /**
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        if (scene == Scene.GAMEPLAY) {
            board.display(g);
            mainMenuButton.display(g);
        } else if (scene == Scene.MAINMENU) {
            mainMenuDisplay(g);
        } else if (scene == Scene.INSTRUCTIONS) {
            instructionsDisplay(g);
        }
        tm.start();
    }

    private void mainMenuDisplay(Graphics g) {
        drawTextMiddle(g, "MECHESS", Constants.FRAME_WIDTH / 2, 350, titleFontSize, Color.white);
        drawTextMiddle(g, "Mert Kaya", Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT - 100, 30, new Color(255, 255, 255, 125));
        g.drawImage(king, 100, -50, null);
        playButton.display(g);
        instructionsButton.display(g);

    }

    private void instructionsDisplay(Graphics g) {
        drawTextMiddle(g, "INSTRUCTIONS", Constants.FRAME_WIDTH/2, 120, 60, new Color(255,255,255, 125));
        drawTextMiddle(g, "1. click on the square to select it", Constants.FRAME_WIDTH/2, 300, 20, Color.white);
        drawTextMiddle(g, "2. click on the square where you want to move your piece", Constants.FRAME_WIDTH/2, 350, 20, Color.white);
        drawTextMiddle(g, "3. if you want to unselect the square, click on it again", Constants.FRAME_WIDTH/2, 400, 20, Color.white);
        drawTextMiddle(g, "THAT'S IT! NOW YOU CAN PLAY!", Constants.FRAME_WIDTH/2, 450, 20, Color.white);
        mainMenuButton.display(g);
    }

    /**
     * draws string referenced from the middle of the text
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
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    /**
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Scene job;
        if (scene == Scene.GAMEPLAY) {
            board.clicked(e);

            job = mainMenuButton.clicked(e);
            if (job != null){
                scene = job;
                board.reset();
            }
        }
        else if (scene == Scene.MAINMENU) {
            job = playButton.clicked(e);
            if (job != null) scene = job;
            job = instructionsButton.clicked(e);
            if (job != null) scene = job;
        }else if(scene == Scene.INSTRUCTIONS){
            job = mainMenuButton.clicked(e);
            if (job != null) scene = job;
        }
        repaint();
    }

    /**
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (scene == Scene.MAINMENU) {
            playButton.mouseOver(e);
            instructionsButton.mouseOver(e);
        }else if(scene == Scene.INSTRUCTIONS){
            mainMenuButton.mouseOver(e);
        }else if(scene == Scene.GAMEPLAY){
            mainMenuButton.mouseOver(e);
        }
    }

    /**
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
