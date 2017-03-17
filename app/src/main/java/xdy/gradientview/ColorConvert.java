package xdy.gradientview;

public class ColorConvert {
    /**
     * * Returns a web browser-friendly HEX value representing the colour in the
     * default sRGB * ColorModel. * * @param r red * @param g green * @param b
     * blue * @return a browser-friendly HEX value
     */
    public static String toHex(int r, int g, int b) {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g)
                + toBrowserHexValue(b);
    }

    //转换RGB颜色数值
    public static String toHex16(int[] rgb) {
        return "#" + toBrowserHexValue(rgb[0]) + toBrowserHexValue(rgb[1])
                + toBrowserHexValue(rgb[2]);
    }

    private static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }
} 