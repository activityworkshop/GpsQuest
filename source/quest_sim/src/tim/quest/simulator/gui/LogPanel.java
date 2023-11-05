package tim.quest.simulator.gui;

import tim.quest.simulator.I18nTexts;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends TitledBorderPanel {
    private final JTextArea textArea = new JTextArea();

    public LogPanel(I18nTexts texts) {
        super(texts.getText("logpanel.title"));
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        setLanguage(texts);
    }

    public void setLanguage(I18nTexts texts) {
        setTitle(texts.getText("logpanel.title"));
    }

    public void log(String message) {
        textArea.append(message);
        textArea.append("\n");
    }

    public void clear() {
        textArea.setText("");
    }
}
