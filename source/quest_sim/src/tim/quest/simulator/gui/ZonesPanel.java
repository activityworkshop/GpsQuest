package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;
import tim.quest.simulator.WindowController;

import javax.swing.*;
import java.awt.*;

public class ZonesPanel extends JPanel implements LanguageAware {
    private final JLabel topLabel;
    private final ZoneTable mainTable;
    private final ZoneTableModel tableModel;

    public ZonesPanel(I18nTexts texts, WindowController controller) {
        setLayout(new BorderLayout(5, 5));
        topLabel = new JLabel(texts.getText("zonespanel.title"));
        GuiTricks.makeFontBigger(topLabel);
        add(topLabel, BorderLayout.NORTH);
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
        topLabel.setText(texts.getText("zonespanel.title"));
        mainTable.setLanguage(texts);
        tableModel.setLanguage(texts);
        tableModel.fireTableStructureChanged();
    }
}
