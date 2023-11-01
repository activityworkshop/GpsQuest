package tim.quest.simulator.gui;

import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;
import tim.quest.simulator.WindowController;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ZoneTable extends JTable {
    private final ZoneTableModel tableModel;
    private final ButtonRenderer enterRenderer;
    private final ButtonRenderer exitRenderer;
    private final ButtonCellEditor enterEditor;
    private final ButtonCellEditor exitEditor;


    /** Inner class to render the buttons in the table cells (only render, not edit, so the buttons don't have listeners) */
    private static class ButtonRenderer implements TableCellRenderer, LanguageAware {
        private I18nTexts i18n;
        private final String key;

        private ButtonRenderer(I18nTexts texts, String key) {
            i18n = texts;
            this.key = key;
        }

        public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int i, int i1) {
            return new JButton(i18n.getText("zones.button." + key));
        }
        public void setLanguage(I18nTexts texts) {
            i18n = texts;
        }
    }

    /** Inner class to provide the actual buttons in the table cells (edit, not just render, so the buttons have real listeners) */
    private class ButtonCellEditor extends DefaultCellEditor implements LanguageAware {
        private I18nTexts i18n;
        private final String key;
        private final ButtonAction action;

        private ButtonCellEditor(I18nTexts texts, String key, ButtonAction action) {
            super(new JCheckBox());
            i18n = texts;
            this.key = key;
            this.action = action;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JButton button = new JButton(i18n.getText("zones.button." + key));
            button.addActionListener(e -> action.fireControllerMethod(tableModel.getZoneName(row)));
            return button;
        }

        public void setLanguage(I18nTexts texts) {
            i18n = texts;
        }
    }

    private interface ButtonAction {
        void fireControllerMethod(String zoneName);
    }


    /** Main class for the zone table */
    public ZoneTable(I18nTexts texts, ZoneTableModel tableModel, WindowController controller) {
        super(tableModel);
        this.tableModel = tableModel;
        enterRenderer = new ButtonRenderer(texts, "enter");
        exitRenderer = new ButtonRenderer(texts, "exit");

        enterEditor = new ButtonCellEditor(texts, "enter", controller::enterZone);
        exitEditor = new ButtonCellEditor(texts, "exit", controller::exitZone);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 1) {
            return enterRenderer;
        }
        else if (column == 2) {
            return exitRenderer;
        }
        return super.getCellRenderer(row, column);
    }

    public TableCellEditor getCellEditor(int row, int column) {
        if (column == 1) {
            return enterEditor;
        }
        else if (column == 2) {
            return exitEditor;
        }
        return super.getCellEditor(row, column);
    }

    public void setLanguage(I18nTexts texts) {
        enterRenderer.setLanguage(texts);
        exitRenderer.setLanguage(texts);
        enterEditor.setLanguage(texts);
        exitEditor.setLanguage(texts);
    }
}
