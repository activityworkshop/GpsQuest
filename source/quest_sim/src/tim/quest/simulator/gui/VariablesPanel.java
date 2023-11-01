package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.*;
import java.awt.*;

public class VariablesPanel extends JPanel implements LanguageAware {
    private final JLabel topLabel;
    private final JTable mainTable;
    private final VariableTableModel tableModel;

    public VariablesPanel(I18nTexts texts) {
        setLayout(new BorderLayout(5, 5));
        topLabel = new JLabel(texts.getText("variablespanel.title"));
        GuiTricks.makeFontBigger(topLabel);
        add(topLabel, BorderLayout.NORTH);
        tableModel = new VariableTableModel();
        mainTable = new JTable(tableModel);
        add(new JScrollPane(mainTable), BorderLayout.CENTER);
        setLanguage(texts);
        setQuest(null);
    }

    public void setQuest(Quest quest) {
        tableModel.setQuest(quest);
        mainTable.setVisible(quest != null);
    }

    public void setLanguage(I18nTexts texts) {
        topLabel.setText(texts.getText("variablespanel.title"));
        tableModel.setLanguage(texts);
        tableModel.fireTableStructureChanged();
    }
}
