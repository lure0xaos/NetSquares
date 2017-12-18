package gargoyle.netsquares.ui.painter.state;

import gargoyle.netsquares.util.Assert;

import java.awt.Graphics2D;
import java.util.Stack;

public final class GraphicsStateManager implements AutoCloseable {
    private final boolean autoRestore;
    private final Graphics2D graphics;
    private final Stack<GraphicsState> states = new Stack<>();

    public GraphicsStateManager(Graphics2D graphics, boolean autoRestore) {
        this.graphics = graphics;
        this.autoRestore = autoRestore;
        if (autoRestore) {
            save();
        }
    }

    private void checkEmpty() {
        Assert.assertTrue(states.empty(), "");
    }

    @Override
    public void close() {
        if (autoRestore) {
            while (!isEmpty()) {
                restore();
            }
        }
        checkEmpty();
        reset();
    }

    public GraphicsState getState() {
        return states.peek();
    }

    private boolean isEmpty() {
        return states.empty();
    }

    private void reset() {
        states.clear();
    }

    public void restore() {
        GraphicsState state = states.pop();
        if (state != null) {
            state.restore(graphics);
        }
    }

    public void save() {
        states.push(new GraphicsState(graphics));
    }

    @Override
    public String toString() {
        return "GraphicsStateManager{" +
                "autoRestore=" + autoRestore +
                ", top state=" + states.peek() +
                ", size=" + states.size() +
                ", states=" + states +
                '}';
    }
}
