package gargoyle.netsquares.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class Images {
    private Images() {
        super();
    }

    public static Image makeRectangular(final Image image) {
        final int width = image.getWidth(null);
        final int height = image.getHeight(null);
        final int size = Math.max(width, height);
        final BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        final int x = (size - width) / 2;
        final int y = (size - height) / 2;
        img.getGraphics().drawImage(image, x, y, null);
        return img;
    }

    public static Image fitImage(final Image image, final Rectangle rectangle) {
        final Rectangle fitImage = fitImage(new Rectangle(image.getWidth(null), image.getHeight(null)), rectangle);
        return image.getScaledInstance(fitImage.width, fitImage.height, Image.SCALE_SMOOTH);
    }

    public static Rectangle fitImage(final Rectangle image, final Rectangle rectangle) {
        final int width = image.width;
        final int height = image.height;
        final int w;
        final int h;
        if (width > height) {
            h = rectangle.height;
            w = (int) (rectangle.height / (double) width * height);
        } else {
            w = rectangle.width;
            h = (int) (rectangle.width / (double) width * height);
        }
        final int x = rectangle.x + (rectangle.width - w) / 2;
        final int y = rectangle.y + (rectangle.height - h) / 2;
        return new Rectangle(x, y, w, h);
    }
}
