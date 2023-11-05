package tim.quest.simulator.gui;

import tim.quest.model.Quest;
import tim.quest.simulator.I18nTexts;
import tim.quest.simulator.LanguageAware;

import javax.swing.*;

public class InfoPanel extends JPanel implements LanguageAware {
    private Quest quest = null;
    private final JLabel topLabel;
    private final JLabel nameLabel;
    private final JLabel descriptionLabel;
    private final JLabel languagesLabel;

    public InfoPanel(I18nTexts texts) {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        topLabel = new JLabel(texts.getText("infopanel.title"));
        GuiTricks.makeFontBigger(topLabel);
        nameLabel = new JLabel(texts.getText("infopanel.name"));
        descriptionLabel = new JLabel(texts.getText("infopanel.description"));
        languagesLabel = new JLabel(texts.getText("infopanel.languages"));
        for (JLabel label : new JLabel[] {topLabel, nameLabel, languagesLabel, descriptionLabel}) {
            add(label);
        }
        setLanguage(texts);
    }

    public void setLanguage(I18nTexts texts) {
        topLabel.setText(texts.getText("infopanel.title"));
        if (quest == null) {
            nameLabel.setText(texts.getText("infopanel.noquestloaded"));
            nameLabel.setEnabled(false);
            languagesLabel.setVisible(false);
            descriptionLabel.setVisible(false);
        }
        else {
            nameLabel.setText(texts.getText("infopanel.name") + ": " + quest.getName());
            nameLabel.setEnabled(true);
            languagesLabel.setText(texts.getText("infopanel.languages") + ": " + getQuestLanguages(texts));
            languagesLabel.setVisible(true);
            descriptionLabel.setText(texts.getText("infopanel.description") + ": " + getQuestDescription(texts));
            descriptionLabel.setVisible(true);
        }
    }

    private String getQuestLanguages(I18nTexts texts) {
        StringBuilder result = new StringBuilder();
        for (String desc : quest.getLanguages()) {
            if (desc != null && !desc.equals("")) {
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append(desc);
            }
        }
        return result.length() == 0 ? texts.getText("infopanel.languages.none") : result.toString();
    }

    private String getQuestDescription(I18nTexts texts) {
        String desc = quest.getDescription();
        if (desc != null && !desc.equals("")) {
            return desc;
        }
        return texts.getText("infopanel.description.none");
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}
