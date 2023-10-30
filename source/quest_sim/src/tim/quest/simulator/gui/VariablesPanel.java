package tim.quest.simulator.gui;

import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.*;

public class VariablesPanel extends JPanel implements LanguageAware {
    private final JLabel topLabel;

    public VariablesPanel(I18nTexts texts) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        topLabel = new JLabel(texts.getText("variablespanel.title"));
        GuiTricks.makeFontBigger(topLabel);
        add(topLabel);
        setLanguage(texts);
    }

    public void setLanguage(I18nTexts texts) {
        topLabel.setText(texts.getText("variablespanel.title"));
    }
}
