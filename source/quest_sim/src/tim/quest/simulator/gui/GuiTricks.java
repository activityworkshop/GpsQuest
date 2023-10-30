package tim.quest.simulator.gui;

import java.awt.*;

public class GuiTricks {
    public static void makeFontBigger(Component component) {
        Font biggerFont = component.getFont();
        biggerFont = biggerFont.deriveFont(Font.BOLD, biggerFont.getSize2D() + 2.0f);
        component.setFont(biggerFont);
    }
}
