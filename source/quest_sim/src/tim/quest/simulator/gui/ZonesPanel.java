package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.WindowController;

import javax.swing.*;
import java.awt.*;

public class ZonesPanel extends TitledBorderPanel {
    private final ZoneTable mainTable;
    private final ZoneTableModel tableModel;

    public ZonesPanel(I18nTexts texts, WindowController controller) {
        super(texts.getText("zonespanel.title"));
        tableModel = new ZoneTableModel();
        mainTable = new ZoneTable(texts, tableModel, controller);
        add(new JScrollPane(mainTable), BorderLayout.CENTER);
        setLanguage(texts);
        setQuest(null);
    }

    public void setQuest(Quest quest) {
        tableModel.setQuest(quest);
        mainTable.setVisible(quest != null);
    }

    public void setLanguage(I18nTexts texts) {
        setTitle(texts.getText("zonespanel.title"));
        mainTable.setLanguage(texts);
        tableModel.setLanguage(texts);
        tableModel.fireTableStructureChanged();
    }
}
