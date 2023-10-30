package tim.quest.simulator.gui;

import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.*;

public class ZonesPanel extends JPanel implements LanguageAware {
    private final JLabel topLabel;

    public ZonesPanel(I18nTexts texts) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        topLabel = new JLabel(texts.getText("zonespanel.title"));
        GuiTricks.makeFontBigger(topLabel);
        add(topLabel);
        setLanguage(texts);
    }

    public void setLanguage(I18nTexts texts) {
        topLabel.setText(texts.getText("zonespanel.title"));
    }
}
