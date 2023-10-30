package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.*;

public class InfoPanel extends JPanel implements LanguageAware {
    private Quest quest = null;
    private final JLabel topLabel;
    private final JLabel questNameLabel;
    private final JLabel questDescriptionLabel;

    public InfoPanel(I18nTexts texts) {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        topLabel = new JLabel(texts.getText("infopanel.title"));
        GuiTricks.makeFontBigger(topLabel);
        questNameLabel = new JLabel(texts.getText("infopanel.name"));
        questDescriptionLabel = new JLabel(texts.getText("infopanel.description"));
        for (JLabel label : new JLabel[] {topLabel, questNameLabel, questDescriptionLabel}) {
            add(label);
        }
        setLanguage(texts);
    }

    public void setLanguage(I18nTexts texts) {
        topLabel.setText(texts.getText("infopanel.title"));
        if (quest == null) {
            questNameLabel.setText(texts.getText("infopanel.noquestloaded"));
            questNameLabel.setEnabled(false);
            questDescriptionLabel.setVisible(false);
        }
        else {
            questNameLabel.setText(texts.getText("infopanel.name") + ": " + quest.getName());
            questNameLabel.setEnabled(true);
            questDescriptionLabel.setText(texts.getText("infopanel.description") + ": " + getQuestDescription(texts));
            questDescriptionLabel.setVisible(true);
        }
    }

    private String getQuestDescription(I18nTexts texts) {
        // TODO: Use the currently selected quest language
        for (String desc : quest.getDescriptions().values()) {
            if (desc != null && !desc.equals("")) {
                return desc;
            }
        }
        return texts.getText("infopanel.description.none");
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}
