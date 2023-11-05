package tim.quest.simulator.gui;

import tim.quest.simulator.LanguageAware;

import javax.swing.*;
import java.awt.*;

abstract class TitledBorderPanel extends JPanel implements LanguageAware {
    private final JLabel topLabel;

    TitledBorderPanel(String titleText) {
        setLayout(new BorderLayout(5, 5));
        topLabel = new JLabel(titleText);
        GuiTricks.makeFontBigger(topLabel);
        add(topLabel, BorderLayout.NORTH);
    }

    protected void setTitle(String titleText) {
        topLabel.setText(titleText);
    }
}
