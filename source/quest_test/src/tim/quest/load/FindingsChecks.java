package tim.quest.load;

import tim.quest.Finding;

import java.util.List;

public class FindingsChecks {

    /** Check whether the list contains an error finding with the given message */
    static boolean containsError(List<Finding> findings, String msg) {
        return contains(Finding.Severity.ERROR, findings, msg);
    }

    /** Check whether the list contains a warning with the given message */
    static boolean containsWarning(List<Finding> findings, String msg) {
        return contains(Finding.Severity.WARNING, findings, msg);
    }

    private static boolean contains(Finding.Severity severity, List<Finding> findings, String msg) {
        for (Finding finding : findings) {
            if (finding.getSeverity() == severity) {
                if (msg == null) {
                    System.out.println(finding.getText());
                    return true;
                }
                else if (finding.getText().contains(msg)) {
                    return true;
                }
            }
        }
        return false;
    }
}
