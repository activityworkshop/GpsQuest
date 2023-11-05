package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ZoneTableModel extends AbstractTableModel implements LanguageAware {
    private Quest quest = null;
    private String columnHeading = "";
    private final ArrayList<String> zoneNames = new ArrayList<>();


    public void setQuest(Quest quest) {
        this.quest = quest;
        zoneNames.clear();
    }

    public int getRowCount() {
        return quest == null ? 0 : quest.getZones().size();
    }

    public int getColumnCount() {
        return 3;
    }

    public String getColumnName(int col) {
        return col == 0 ? columnHeading : "";
    }

    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    public Object getValueAt(int row, int col) {
        if (quest == null) {
            return "";
        }
        if (zoneNames.isEmpty()) {
            zoneNames.addAll(quest.getZoneNames());
        }
        return col == 0 ? getZoneName(row) : "";
    }

    public String getZoneName(int row) {
        if (quest == null) {
            return "";
        }
        if (zoneNames.isEmpty()) {
            zoneNames.addAll(quest.getZoneNames());
        }
        return zoneNames.get(row);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0 && rowIndex >= 0;
    }

    public void setLanguage(I18nTexts texts) {
        columnHeading = texts.getText("zones.column.name");
    }
}
