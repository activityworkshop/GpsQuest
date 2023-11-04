package tim.quest.simulator.gui;

import tim.quest.Findings;
import tim.quest.load.QuestFileException;
import tim.quest.load.QuestLoader;
import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;
import tim.quest.simulator.WindowController;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWindow implements WindowController {
    private JFrame frame = null;
    private final I18nTexts i18n = new I18nTexts();
    private final MenuManager menuManager = new MenuManager(this);
    private final ArrayList<LanguageAware> components = new ArrayList<>();
    private LogPanel logPanel = null;
    private InfoPanel infoPanel = null;
    private VariablesPanel variablesPanel = null;
    private ZonesPanel zonesPanel = null;
    private JFileChooser fileChooser = null;


    public void launch(String language) {
        frame = new JFrame("Quest Simulator");
        setIcon(frame, "quest-icon");
        i18n.setLanguage(language);
        JMenuBar menuBar = menuManager.getMenuBar();
        frame.setJMenuBar(menuBar);
        menuManager.setLanguage(i18n);
        infoPanel = new InfoPanel(i18n);
        // Two panels side by side
        zonesPanel = new ZonesPanel(i18n, this);
        variablesPanel = new VariablesPanel(i18n);
        JSplitPane eastWestSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, zonesPanel, variablesPanel);
        eastWestSplit.setDividerLocation(0.5);
        eastWestSplit.setResizeWeight(0.5);
        // horizontal divider and log panel below
        logPanel = new LogPanel(i18n);
        JSplitPane northSouthSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, eastWestSplit, logPanel);
        northSouthSplit.setResizeWeight(0.6);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(infoPanel, BorderLayout.NORTH);
        frame.getContentPane().add(northSouthSplit, BorderLayout.CENTER);
        frame.pack();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitWindow();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(650, 450);

        // Ensure everything is updated when the language is changed
        addToI18nUpdate(menuManager, infoPanel, zonesPanel, variablesPanel, logPanel);
    }

    private void setIcon(JFrame frame, String iconName) {
        try
        {
            ArrayList<Image> icons = new ArrayList<>();
            String[] resolutions = {"_64", "_128"};
            for (String r : resolutions) {
                URL url = getClass().getResource("images/" + iconName + r + ".png");
                if (url != null) {
                    icons.add(new ImageIcon(url).getImage());
                }
            }
            frame.setIconImages(icons);
        }
        catch (Exception e)
        {
            // setting a list of icon images didn't work, so try with just one image instead
            try {
                URL url = getClass().getResource("images/" + iconName + "_64.png");
                if (url != null) {
                    frame.setIconImage(new ImageIcon(url).getImage());
                }
            }
            catch (Exception e2) {
                System.err.println("Failed to set frame's icon image: " + e2.getMessage());
            }
        }
    }

    public void setLanguage(String languageCode) {
        logPanel.log("Change language to '" + languageCode + "'");
        i18n.setLanguage(languageCode);
        redrawComponents();
    }

    private void redrawComponents() {
        SwingUtilities.invokeLater(() -> {
            for (LanguageAware component : components) {
                component.setLanguage(i18n);
            }
        });
    }

    public void openQuest() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory() || file.getName().endsWith(".quest");
                }

                public String getDescription() {
                    return "Quest files";
                }
            });
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setMultiSelectionEnabled(false);
        }
        // Show the open dialog
		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                Findings findings = new Findings();
                Quest quest = QuestLoader.fromFile(fileChooser.getSelectedFile(), findings);
                logPanel.log("Opened the Quest file '" + fileChooser.getSelectedFile().getAbsolutePath() + "'");
                infoPanel.setQuest(quest);
                variablesPanel.setQuest(quest);
                zonesPanel.setQuest(quest);
                menuManager.setQuest(quest);
                redrawComponents();
            } catch (QuestFileException e) {
                logPanel.log("Failed to open the Quest file '" + fileChooser.getSelectedFile().getAbsolutePath() + "'");
            }
        }
    }

    public void clearLogPanel() {
        logPanel.clear();
    }

    private void addToI18nUpdate(LanguageAware ... awareComponents) {
        components.addAll(Arrays.asList(awareComponents));
    }

    public void exitWindow() {
        // TODO: Check whether to save the current quest's progress or not (if Quest loaded and started)
        System.exit(0);
    }

    public void enterZone(String zoneId) {
        logPanel.log("Entered the zone '" + zoneId + "'");
    }

    public void exitZone(String zoneId) {
        logPanel.log("Exited the zone '" + zoneId + "'");
    }
}
