package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class VariableTableModel extends AbstractTableModel implements LanguageAware {
    private Quest quest = null;
    private String columnHeadingVariable = "";
    private String columnHeadingValue = "";
    private final ArrayList<String> variableNames = new ArrayList<>();


    public void setQuest(Quest quest) {
        this.quest = quest;
        variableNames.clear();
    }

    public int getRowCount() {
        return quest == null ? 0 : quest.getNumVariables();
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int col) {
        return col == 0 ? columnHeadingVariable : columnHeadingValue;
    }

    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    public Object getValueAt(int row, int col) {
        if (quest == null) {
            return "";
        }
        if (variableNames.isEmpty()) {
            variableNames.addAll(quest.getVariableNames());
        }
        String varName = variableNames.get(row);
        return col == 0 ? varName : quest.getVariableValue(varName);
    }

    public void setLanguage(I18nTexts texts) {
        columnHeadingVariable = texts.getText("variables.column.variable");
        columnHeadingValue = texts.getText("variables.column.value");
    }
}
