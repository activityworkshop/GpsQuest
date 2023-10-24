package tim.quest.load;

/** Thrown when there's a problem with parsing the quest file itself */
public class QuestFileException extends Exception {
    public QuestFileException(String message) {
        super(message);
    }
}
