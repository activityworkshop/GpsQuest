package tim.quest;

/** Represents a problem found during validation */
public class Finding {
    /** Severity of finding */
    public enum Severity {
        INFO("    info"), WARNING(" Warning"), ERROR("ERROR   ");
        private final String desc;
        Severity(String d) {
            desc = d;
        }
        String getDesc() {
            return desc;
        }
    }

    private final Severity severity;
    private final String text;

    public Finding(Severity severity, String text) {
        this.severity = severity;
        this.text = text;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getTypeDesc() {
        return severity.getDesc();
    }

    public String getText() {
        return text;
    }
}
