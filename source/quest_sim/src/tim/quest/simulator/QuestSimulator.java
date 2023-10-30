package tim.quest.simulator;

import tim.quest.simulator.gui.MainWindow;

public class QuestSimulator {
    public static void main(String[] args) {
        // TODO: read command line parameters (such as language?)
        MainWindow window = new MainWindow();
        window.launch("en");
    }
}
