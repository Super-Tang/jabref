package org.jabref.gui.preftabs;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.jabref.gui.customjfx.CustomJFXPanel;
import org.jabref.gui.groups.GroupViewMode;
import org.jabref.logic.l10n.Localization;
import org.jabref.preferences.JabRefPreferences;

class GroupsPrefsTab extends JPanel implements PrefsTab {

    private final CheckBox hideNonHits = new CheckBox(Localization.lang("Hide non-hits"));
    private final CheckBox grayOut = new CheckBox(Localization.lang("Gray out non-hits"));
    private final CheckBox autoAssignGroup = new CheckBox(Localization.lang("Automatically assign new entry to selected groups"));
    private final RadioButton multiSelectionModeIntersection = new RadioButton(Localization.lang("Intersection"));
    private final RadioButton multiSelectionModeUnion = new RadioButton(Localization.lang("Union"));

    private final TextField groupingField = new TextField();
    private final TextField keywordSeparator = new TextField();

    private final JabRefPreferences prefs;

    public GroupsPrefsTab(JabRefPreferences prefs) {
        this.prefs = prefs;

        keywordSeparator.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                keywordSeparator.selectAll();
            }
        });


        multiSelectionModeIntersection.setText(Localization.lang("Display only entries belonging to all selected groups."));
        multiSelectionModeUnion.setText(Localization.lang("Display all entries belonging to one or more of the selected groups."));

        GridPane builder = new GridPane();

        builder.add(new Label(Localization.lang("View")),1,1);
        builder.add(hideNonHits,2,2);
        builder.add(grayOut,2,3);
        builder.add(multiSelectionModeIntersection,2,4);
        builder.add(multiSelectionModeUnion,2,5);
        builder.add(autoAssignGroup,2,6);
        builder.add(new Label(Localization.lang("Dynamic groups")),1,7);

        builder.add(new Label(Localization.lang("Default grouping field") + ":"),1,8);
        builder.add(groupingField,2,8);
        builder.add(new Label(Localization.lang("When adding/removing keywords, separate them by") + ":"),1,9);
        builder.add(keywordSeparator,2,9);

        JFXPanel panel = CustomJFXPanel.wrap(new Scene(builder));
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void setValues() {
        grayOut.setSelected(prefs.getBoolean(JabRefPreferences.GRAY_OUT_NON_HITS));
        groupingField.setText(prefs.get(JabRefPreferences.GROUPS_DEFAULT_FIELD));
        keywordSeparator.setText(prefs.get(JabRefPreferences.KEYWORD_SEPARATOR));
        autoAssignGroup.setSelected(prefs.getBoolean(JabRefPreferences.AUTO_ASSIGN_GROUP));

        GroupViewMode mode = prefs.getGroupViewMode();
        if (mode == GroupViewMode.INTERSECTION) {
            multiSelectionModeIntersection.setSelected(true);
        }
        if (mode == GroupViewMode.UNION) {
            multiSelectionModeUnion.setSelected(true);
        }
    }

    @Override
    public void storeSettings() {
        prefs.putBoolean(JabRefPreferences.GRAY_OUT_NON_HITS, grayOut.isSelected());
        prefs.put(JabRefPreferences.GROUPS_DEFAULT_FIELD, groupingField.getText().trim());
        prefs.putBoolean(JabRefPreferences.AUTO_ASSIGN_GROUP, autoAssignGroup.isSelected());
        prefs.put(JabRefPreferences.KEYWORD_SEPARATOR, keywordSeparator.getText());

        if (multiSelectionModeIntersection.isSelected()) {
            prefs.setGroupViewMode(GroupViewMode.INTERSECTION);
        }
        if (multiSelectionModeUnion.isSelected()) {
            prefs.setGroupViewMode(GroupViewMode.UNION);
        }
    }

    @Override
    public boolean validateSettings() {
        return true;
    }

    @Override
    public String getTabName() {
        return Localization.lang("Groups");
    }

}
