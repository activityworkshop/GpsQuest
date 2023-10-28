package tim.quest.simulator;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel implements LanguageAware {
    private final JLabel topLabel = new JLabel("Log messages");
    private final JTextArea textArea = new JTextArea();

    public LogPanel(I18nTexts texts) {
        setLayout(new BorderLayout(5, 5));
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
