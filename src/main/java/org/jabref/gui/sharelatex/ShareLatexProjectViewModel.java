package org.jabref.gui.sharelatex;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Data class
 * @author CS
 *
 */
public class ShareLatexProjectViewModel {

    private final String projectId;
    private final StringProperty projectTitle;
    private final StringProperty owner;
    private final StringProperty lastUpdated;

    public ShareLatexProjectViewModel(String projectId, String projectTitle, String owner, String lastUpdated) {

        this.projectId = projectId;
        this.projectTitle = new SimpleStringProperty(projectTitle);
        this.owner = new SimpleStringProperty(owner);
        this.lastUpdated = new SimpleStringProperty(lastUpdated);

    }

    public String getProjectId() {
        return projectId;
    }

    public StringProperty getProjectTitle() {
        return projectTitle;
    }

    public StringProperty getOwner() {
        return owner;
    }

    public StringProperty getLastUpdated() {
        return lastUpdated;
    }
}
