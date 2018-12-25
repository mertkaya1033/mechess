package main;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import pieces.Bishop;


public class Display extends JPanel implements MouseListener,
        MouseMotionListener, ActionListener {

    private Timer tm = new Timer(30, this);
    private Board board;


    public Display() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        board = new Board();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.display(g);
        tm.start();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        board.clicked(arg0);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

}
