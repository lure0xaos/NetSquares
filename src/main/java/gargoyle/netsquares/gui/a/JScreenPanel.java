package gargoyle.netsquares.gui.a;

import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.gui.i.NSScreen;
import gargoyle.netsquares.logic.i.IGame;

import javax.swing.*;
import java.awt.*;

public abstract class JScreenPanel extends JPanel implements NSScreen {

    private final IGameNotifier notifier;

    public JScreenPanel(IGameNotifier notifier, String name, LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        this.notifier = notifier;
        setName(name);
    }

    public JScreenPanel(IGameNotifier notifier, String name) {
        this.notifier = notifier;
        setName(name);
    }

    public JScreenPanel(IGameNotifier notifier, String name, boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        this.notifier = notifier;
        setName(name);
    }

    public JScreenPanel(IGameNotifier notifier, String name, LayoutManager layout) {
        super(layout);
        this.notifier = notifier;
        setName(name);
    }

    @Override
    public Component add(Component comp) {
        check(comp);
        return super.add(comp);
    }

    @Override
    public Component add(Component comp, int index) {
        check(comp);
        return super.add(comp, index);
    }

    @Override
    public Component add(String name, Component comp) {
        check(comp);
        return super.add(name, comp);
    }

    @Override
    public void add(Component comp, Object constraints) {
        check(comp);
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        check(comp);
        super.add(comp, constraints, index);
    }

    private void check(Component comp) {
        if (!(comp instanceof IGameNotifier)) {
            throw new IllegalArgumentException("component must implement IGameNotifier");
        }
    }

    @Override
    public void bind(IGame game) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c instanceof IGameNotifier)
                game.addGameNotifier((IGameNotifier) c);
        }
    }

    @Override
    public void unbind(IGame game) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c instanceof IGameNotifier)
                game.removeGameNotifier((IGameNotifier) c);
        }
    }

    @Override
    public GameNote notify(GameNote note) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c instanceof IGameNotifier)
                ((IGameNotifier) c).notify(note);
        }
        repaint();
        return null;
    }
}
