package gargoyle.netsquares.gui.util;

import gargoyle.netsquares.gui.i.Adaptable;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

public final class AdaptHelper {

    private AdaptHelper() {
        super();
    }

    private static final AdaptContainerListener adaptContainerListener = new AdaptContainerListener();
    private static final AdaptComponentListener adaptComponentListener = new AdaptComponentListener();


    public static void addAdaptListeners(Adaptable adaptable) {
        if (adaptable instanceof Component) {
            addComponentListener((Component) adaptable);
        }
        if (adaptable instanceof Container) {
            addContainerListener((Container) adaptable);
        }
    }

    public static void removeAdaptListeners(Adaptable adaptable) {
        if (adaptable instanceof Component) {
            removeComponentListener((Component) adaptable);
        }
        if (adaptable instanceof Container) {
            removeContainerListener((Container) adaptable);
        }
    }

    public static void removeAdaptAllListeners(Adaptable adaptable) {
        if (adaptable instanceof Component) {
            removeComponentListener((Component) adaptable);
        }
        if (adaptable instanceof Container) {
            final Container container = (Container) adaptable;
            for (int i = 0; i < container.getComponentCount(); i++) {
                Component component = container.getComponent(i);
                if (component instanceof Adaptable) {
                    removeAdaptAllListeners((Adaptable) component);
                }
            }
        }
    }

    private static void removeComponentListener(Component component) {
        component.removeComponentListener(adaptComponentListener);
        if (component instanceof Container) removeContainerListener((Container) component);
    }

    private static void removeContainerListener(Container container) {
        container.removeContainerListener(adaptContainerListener);
    }


    private static void addContainerListener(Container container) {
        container.addContainerListener(adaptContainerListener);
    }

    private static void addComponentListener(Component component) {
        component.addComponentListener(adaptComponentListener);
        if (component instanceof Container) addContainerListener((Container) component);
    }

    private static void adaptComponentRemoved(Component child) {
        child.removeComponentListener(adaptComponentListener);
        if (child instanceof Container) removeContainerListener((Container) child);
    }

    private static void adaptComponentAdded(Component child) {
        addComponentListener(child);
        if (child instanceof Container) addContainerListener((Container) child);
    }

    private static void adapt(Component component) {
        if (component instanceof Adaptable) {
            ((Adaptable) component).adapt();
        }
        if (component instanceof Container) {
            final Container container = (Container) component;
            for (int i = 0; i < container.getComponentCount(); i++) {
                adapt(container.getComponent(i));
            }
        }
    }

    private static class AdaptComponentListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
//            System.out.println("resized "+e.getComponent());
            adapt(e.getComponent());
        }

        @Override
        public void componentShown(ComponentEvent e) {
//            System.out.println("shown "+e.getComponent());
            adapt(e.getComponent());
        }
    }

    private static class AdaptContainerListener extends ContainerAdapter {
        @Override
        public void componentAdded(ContainerEvent e) {
//            System.out.println("added "+e.getChild()+" to "+e.getContainer());
            adaptComponentAdded(e.getChild());
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
//            System.out.println("removed "+e.getChild()+" from "+e.getContainer());
            adaptComponentRemoved(e.getChild());
        }
    }
}
