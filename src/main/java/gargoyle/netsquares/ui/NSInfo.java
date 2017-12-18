package gargoyle.netsquares.ui;

import gargoyle.netsquares.i18n.Messages;
import gargoyle.netsquares.model.Game;
import gargoyle.netsquares.ui.painter.NSPainter;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class NSInfo extends JPanel {
    private final Messages messages;
    private Game game;
    private NSPainter painter;

    public NSInfo(Messages messages, NSPainter painter) {
        this.messages = messages;
        this.painter = painter;
        setOpaque(false);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public NSPainter getPainter() {
        return painter;
    }

    public void setPainter(NSPainter painter) {
        this.painter = painter;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game != null) {
            painter.paintInfo((Graphics2D) g, game, new Rectangle2D.Double.Double(0, 0, getWidth(), getHeight()), messages);
        }
    }
}
