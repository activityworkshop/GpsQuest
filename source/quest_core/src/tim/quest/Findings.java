package tim.quest;

import java.util.ArrayList;

public class Findings extends ArrayList<Finding> {
    public void addError(String msg) {
        add(new Finding(Finding.Severity.ERROR, msg));
    }

    public void addWarning(String msg) {
        add(new Finding(Finding.Severity.WARNING, msg));
    }

    public void addInfo(String msg) {
        add(new Finding(Finding.Severity.INFO, msg));
    }

    public boolean allOk() {
        for (Finding finding : this) {
            if (finding.getSeverity() != Finding.Severity.INFO) {
                return false;
            }
        }
        return true;
    }

    public boolean hasErrors() {
        return hasSeverity(Finding.Severity.ERROR);
    }

    public boolean hasWarnings() {
        return hasSeverity(Finding.Severity.WARNING);
    }

    private boolean hasSeverity(Finding.Severity severity) {
        for (Finding finding : this) {
            if (finding.getSeverity() == severity) {
                return true;
            }
        }
        return false;
    }
}
