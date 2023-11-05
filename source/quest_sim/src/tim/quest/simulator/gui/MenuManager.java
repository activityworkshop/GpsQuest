package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;
import tim.quest.simulator.WindowController;

import javax.swing.*;
import java.awt.event.ActionListener;

/** Responsible for building and modifying the menu bar */
public class MenuManager implements LanguageAware {
    private final WindowController parent;
    private final JMenuBar menuBar;
    private boolean loadedQuest = false;


    public MenuManager(WindowController parent) {
        this.parent = parent;
        menuBar = new JMenuBar();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public void setQuest(Quest quest) {
        loadedQuest = (quest != null);
    }

    public void setLanguage(I18nTexts i18n) {
        menuBar.removeAll();

        JMenu fileMenu = new JMenu(i18n.getText("menu.file"));
        fileMenu.add(makeMenuItem(i18n, "menu.file.openquest", e -> parent.openQuest(), true));
        fileMenu.add(makeMenuItem(i18n, "menu.file.saveprogress", e -> {}, loadedQuest));
        fileMenu.add(makeMenuItem(i18n, "menu.file.resumefromsave", e -> {}, loadedQuest));
        fileMenu.addSeparator();
        fileMenu.add(makeMenuItem(i18n, "menu.file.exit", e -> parent.exitWindow()));
        menuBar.add(fileMenu);

        JMenu simulatorMenu = new JMenu(i18n.getText("menu.simulator"));
        JMenu changeLanguage = new JMenu(i18n.getText("menu.simulator.simulatorlanguage"));
        changeLanguage.add(makeMenuItem(i18n, "menu.simulator.simulatorlanguage.english", e -> parent.setLanguage("en")));
        changeLanguage.add(makeMenuItem(i18n, "menu.simulator.simulatorlanguage.german", e -> parent.setLanguage("de")));
        simulatorMenu.add(changeLanguage);
        simulatorMenu.add(makeMenuItem(i18n, "menu.simulator.questlanguage", e -> parent.selectQuestLanguage(), loadedQuest));
        simulatorMenu.add(makeMenuItem(i18n, "menu.simulator.clearlog", e -> parent.clearLogPanel()));
        menuBar.add(simulatorMenu);

        menuBar.validate();
        menuBar.repaint();
    }

    private JMenuItem makeMenuItem(I18nTexts i18n, String key, ActionListener listener) {
        return makeMenuItem(i18n, key, listener, true);
    }

    private JMenuItem makeMenuItem(I18nTexts i18n, String key, ActionListener listener,
                                   boolean enabled) {
        JMenuItem menuItem = new JMenuItem(i18n.getText(key));
        menuItem.addActionListener(listener);
        menuItem.setEnabled(enabled);
        return menuItem;
    }
}
