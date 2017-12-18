package gargoyle.netsquares.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Rectangle2D;


public final class SwingDraw {
    private SwingDraw() {
    }

    public static void drawStringCentered(Graphics2D g, Rectangle2D bounds, @NotNull String label) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label, (int) (bounds.getX() + ((bounds.getWidth() - fm.stringWidth(label))) / 2), (int) (bounds.getY() + fm.getAscent() + ((bounds.getHeight() - fm.getHeight())) / 2));
    }

    private static int getFitFontSize(Font font, @NotNull String text, int width, int height, Graphics g) {
        return Math.min((int) (font.getSize() * (double) width / (double) g.getFontMetrics(font).stringWidth(text)), height);
    }

    public static Rectangle2D getMaximumWindowBounds() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    }

    public static void setFitFonSize(JLabel label) {
        label.setFont(label.getFont().deriveFont((float) getFitFontSize(label.getFont(), label.getText(), label.getWidth(), label.getHeight(), label.getGraphics())));
    }

    public static void updateFontSize(Graphics g, Rectangle2D bounds, @NotNull String label) {
        g.setFont(g.getFont().deriveFont((float) getFitFontSize(g.getFont(), label, (int) bounds.getWidth(), (int) bounds.getHeight(), g)));
    }
}
