package tim.quest.simulator;

public interface WindowController {

    void setLanguage(String languageCode);

    void openQuest();

    void clearLogPanel();

    void exitWindow();

    void enterZone(String zoneId);
    void exitZone(String zoneId);
}
