package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * Button.java
 * <p>
 * Description: Used to create buttons on the screen
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Button {

    private int xPos, yPos,    //positions of the rectangle referenced from top left corner
            width, height, fontSize, roundSize,
            txtXpos, txtYpos, // positions of the text that will be displayed referenced from left bottom corner
            x, y;    //positions of the button referenced from the middle of the button
    private String txt;
    private Color colour, txtColour;
    private Font font;
    private boolean mouseIn = false;
    private Display.Scene job;
    private JPanel panel;

    /**
     * Button(JPanel panel, String txt, int x, int y, int fontSize, Display.Scene job)
     * <p>
     * Description: constructor
     *
     * @param panel    the panel where the buttons is going to be displayed
     * @param txt      the text that is displayed on the button
     * @param x        the x position of the button referenced from the middle
     * @param y        the y position of the button referenced from the middle
     * @param fontSize the font size of the text
     * @param job      the scene it is suppose to switch to.
     */
    public Button(JPanel panel, String txt, int x, int y, int fontSize, Display.Scene job) {
        this.colour = new Color(245, 147, 66);
        this.txtColour = Color.WHITE;
        this.panel = panel;

        this.x = x;
        this.y = y;
        this.txt = txt;
        this.job = job;
        this.fontSize = fontSize;
        this.font = new Font("Comic Sans MS", Font.PLAIN, this.fontSize);

        this.width = panel.getFontMetrics(this.font).stringWidth(txt);
        this.height = panel.getFontMetrics(this.font).getHeight();
        this.roundSize = 30;

        this.xPos = (x - this.roundSize / 2) - (width / 2);
        this.yPos = y - this.height / 2;

        this.txtXpos = x - this.width / 2;
        this.txtYpos = y + this.height / 2 - panel.getFontMetrics(font).getDescent();
    }

    /**
     * display(Graphics g)
     * <p>
     * Description: draws the button to the frame
     *
     * @param g the graphics panel of the program
     */
    public void display(Graphics g) {

        g.setFont(this.font);
        g.setColor(colour);
        g.fillRoundRect(xPos, yPos, width + roundSize, height, roundSize, height);

        g.setColor(txtColour);
        g.drawString(txt, txtXpos, txtYpos);
        //if mouse is on, draw the outline
        if (this.mouseIn) {
            g.setColor(Color.blue);
            g.drawRoundRect(xPos, yPos, width + roundSize, height, roundSize, height);
        }
    }

    /**
     * mouseOver(MouseEvent e)
     * <p>
     * Description: checks if the mouse is on
     */
    public void mouseOver(MouseEvent e) {
        boolean isMouseXIn = e.getX() > this.x - (this.width / 2 + this.roundSize / 2) && e.getX() < this.x + (this.width / 2 + this.roundSize / 2);
        boolean isMouseYIn = e.getY() > this.y - this.height / 2 && e.getY() < this.y + this.height / 2;
        if (isMouseXIn && isMouseYIn) {
            this.mouseIn = true;
        } else {
            this.mouseIn = false;
        }
    }

    /**
     * clicked(MouseEvent e)
     * <p>
     * Description: does its job if the mouse is clicked on the button
     *
     * @return if mouse is in, its job, if not, empty string
     */
    public Display.Scene clicked(MouseEvent e) {
        mouseOver(e);
        if (mouseIn) {
            mouseIn = false;
            return this.job;
        }
        return null;
    }
}