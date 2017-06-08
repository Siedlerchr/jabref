package org.jabref.gui.sharelatex;

import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import org.jabref.gui.AbstractViewModel;

public class ShareLatexProjectDialogViewModel extends AbstractViewModel {

    private final SimpleListProperty<ShareLatexProjectViewModel> projects = new SimpleListProperty<>(
            FXCollections.observableArrayList());

    public ShareLatexProjectDialogViewModel(List<ShareLatexProjectViewModel> projects) {
        this.projects.addAll(projects);
    }

    public SimpleListProperty<ShareLatexProjectViewModel> projectsProperty() {
        return this.projects;
    }

    public ShareLatexProjectDialogViewModel() {
        // TODO Auto-generated constructor stub
    }

}
