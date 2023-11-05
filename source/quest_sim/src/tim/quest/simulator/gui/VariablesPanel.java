package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;

import javax.swing.*;
import java.awt.*;

public class VariablesPanel extends TitledBorderPanel {
    private final JTable mainTable;
    private final VariableTableModel tableModel;

    public VariablesPanel(I18nTexts texts) {
        super(texts.getText("variablespanel.title"));
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
        setTitle(texts.getText("variablespanel.title"));
        tableModel.setLanguage(texts);
        tableModel.fireTableStructureChanged();
    }
}
