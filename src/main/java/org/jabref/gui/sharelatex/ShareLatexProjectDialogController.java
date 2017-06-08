package org.jabref.gui.sharelatex;

import org.jabref.gui.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ShareLatexProjectDialogController extends AbstractController<ShareLatexProjectDialogViewModel> {

    @FXML TableColumn<ShareLatexProjectViewModel, String> colTitle;
    @FXML TableColumn<ShareLatexProjectViewModel, String> colOwner;
    @FXML TableColumn<ShareLatexProjectViewModel, String> colLastModified;
    @FXML TableView<ShareLatexProjectViewModel> tblProjects;



    @FXML
    private void initialize() {

        viewModel = new ShareLatexProjectDialogViewModel();
        colTitle.setCellValueFactory(cellData -> cellData.getValue().getProjectTitle());
        colOwner.setCellValueFactory(cellData -> cellData.getValue().getOwner());
        colLastModified.setCellValueFactory(cellData -> cellData.getValue().getLastUpdated());
        setBindings();


    }

    private void setBindings() {
        tblProjects.itemsProperty().bindBidirectional(viewModel.projectsProperty());
    }



}
