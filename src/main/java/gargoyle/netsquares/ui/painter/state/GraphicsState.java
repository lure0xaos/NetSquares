package gargoyle.netsquares.ui.painter.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Objects;

public final class GraphicsState {
    private static long lastId;
    private final Shape clip;
    private final Color color;
    private final Font font;
    private final long id;

    public GraphicsState(Graphics2D g) {
        id = lastId;
        lastId++;
        clip = g.getClip();
        color = g.getColor();
        font = g.getFont();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphicsState)) return false;
        GraphicsState that = (GraphicsState) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        return String.format("GraphicsState{id=%d, clip=%s, color=%s, font=%s}", id, clip, color, font);
    }

    public void restore(Graphics2D g) {
        g.setClip(null);
        g.setClip(clip);
        g.setColor(color);
        g.setFont(font);
    }
}
