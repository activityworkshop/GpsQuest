package tim.quest.simulator.gui;

import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel implements LanguageAware {
    private final JLabel topLabel = new JLabel("Log messages");
    private final JTextArea textArea = new JTextArea();

    public LogPanel(I18nTexts texts) {
        setLayout(new BorderLayout(5, 5));
        GuiTricks.makeFontBigger(topLabel);
        add(topLabel, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        setLanguage(texts);
    }

    public void setLanguage(I18nTexts texts) {
        topLabel.setText(texts.getText("logpanel.title"));
    }

    public void log(String message) {
        textArea.append(message);
        textArea.append("\n");
    }

    public void clear() {
        textArea.setText("");
    }
}
