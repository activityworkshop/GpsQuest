package tim.quest.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MainWindow {
    private JFrame frame;
    private final I18nTexts i18n = new I18nTexts();
    private final ArrayList<LanguageAware> components = new ArrayList<>();
    private LogPanel logPanel = null;

    public void launch(String language) {
        frame = new JFrame("Quest Simulator");
        i18n.setLanguage(language);
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        fillMenuBar(menuBar);
        components.add((texts) -> resetMenuBar());
        JPanel topPanel = new JPanel();
        logPanel = new LogPanel(i18n);
        components.add(logPanel);
        JSplitPane northSouthSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, logPanel);
        northSouthSplit.setResizeWeight(0.6);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(northSouthSplit, BorderLayout.CENTER);
        frame.pack();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        for (int i=0; i<5; i++) {
            logPanel.log("Message number " + i);
        }
    }

    private void resetMenuBar() {
        frame.getJMenuBar().removeAll();
        fillMenuBar(frame.getJMenuBar());
    }

    private void fillMenuBar(JMenuBar bar) {
        JMenu fileMenu = new JMenu(i18n.getText("menu.file"));
        fileMenu.add(new JMenuItem(i18n.getText("menu.file.openquest")));
        fileMenu.add(new JMenuItem(i18n.getText("menu.file.saveprogress")));
        fileMenu.add(new JMenuItem(i18n.getText("menu.file.resumefromsave")));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(i18n.getText("menu.file.exit")));
        bar.add(fileMenu);

        JMenu simulatorMenu = new JMenu(i18n.getText("menu.simulator"));
        JMenu changeLanguage = new JMenu(i18n.getText("menu.simulator.simulatorlanguage"));
        JMenuItem english = new JMenuItem(i18n.getText("menu.simulator.simulatorlanguage.english"));
        english.addActionListener(e -> setLanguage("en"));
        changeLanguage.add(english);
        JMenuItem german = new JMenuItem(i18n.getText("menu.simulator.simulatorlanguage.german"));
        german.addActionListener(e -> setLanguage("de"));
        changeLanguage.add(german);
        simulatorMenu.add(changeLanguage);
        simulatorMenu.add(new JMenuItem(i18n.getText("menu.simulator.questlanguage")));
        JMenuItem clearLogWindow = new JMenuItem(i18n.getText("menu.simulator.clearlog"));
        clearLogWindow.addActionListener(e -> logPanel.clear());
        simulatorMenu.add(clearLogWindow);
        bar.add(simulatorMenu);

        bar.validate();
        bar.repaint();
    }

    private void setLanguage(String languageCode) {
        System.out.println("Change language to '" + languageCode + "'");
        i18n.setLanguage(languageCode);
        SwingUtilities.invokeLater(() -> {
            for (LanguageAware component : components) {
                component.setLanguage(i18n);
            }
        });
    }

    private void exit() {
        // TODO: Check whether to save the current quest's progress or not (if Quest loaded and started)
        System.exit(0);
    }
}
