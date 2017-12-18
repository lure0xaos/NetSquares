package gargoyle.netsquares.util;

import java.awt.Color;

public final class InterpolUtils {
    private InterpolUtils() {
    }

    public static Color interpolate(Color color1, Color color2, int steps, int step) {
        int red = interpolate(color1.getRed(), color2.getRed(), steps, step);
        int green = interpolate(color1.getGreen(), color2.getGreen(), steps, step);
        int blue = interpolate(color1.getBlue(), color2.getBlue(), steps, step);
        return new Color(red, green, blue);
    }

    private static int interpolate(int value1, int value2, int steps, int step) {
        return (int) (value1 + (value2 - value1) / (double) steps * step);
    }

    public static int toRange(int v, int min, int max) {
        return Math.max(min, Math.min(v, max));
    }
}
