package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.WindowController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LanguageDialog {
    private final WindowController controller;
    private final Quest quest;
    private final JFrame parentFrame;
    private final I18nTexts texts;
    private final JDialog dialog;
    private java.util.List<String> languages;
    private JList<String> listbox;


    public LanguageDialog(WindowController controller, Quest quest, JFrame parentFrame, I18nTexts texts) {
        this.controller = controller;
        this.quest = quest;
        this.parentFrame = parentFrame;
        this.texts = texts;
        dialog = new JDialog(parentFrame, texts.getText("dialog.selectlanguage.title"));
        dialog.setLocationRelativeTo(parentFrame);
    }

    public void show() {
        if (quest.getNumLanguages() == 1) {
            String language = quest.getLanguages().get(0);
            boolean languageGiven = language != null && !language.isEmpty();
            String message = languageGiven ? texts.getTextWithString("dialog.selectlanguage.onlyone", language)
                    : texts.getText("dialog.selectlanguage.nonegiven");
            JOptionPane.showMessageDialog(parentFrame, message, texts.getText("dialog.selectlanguage.title"),
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Add components to the dialog and show it
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.add(new JLabel(texts.getText("dialog.selectlanguage.intro")), BorderLayout.NORTH);
        languages = quest.getLanguages();
        // JList inside a scrollpane
        listbox = new JList<>(languages.toArray(new String[0]));
        listbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() > 1) {
                    finish();
                }
            }
        });
        mainPanel.add(new JScrollPane(listbox), BorderLayout.CENTER);
        // Buttons at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton(texts.getText("button.ok"));
        okButton.addActionListener(e -> finish());
        buttonPanel.add(okButton);
        JButton cancelButton = new JButton(texts.getText("button.cancel"));
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        // Put it all together
        dialog.getContentPane().add(mainPanel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void finish() {
        int selectedIndex = listbox.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < languages.size()) {
            controller.selectQuestLanguage(languages.get(selectedIndex));
        }
        dialog.dispose();
    }
}
