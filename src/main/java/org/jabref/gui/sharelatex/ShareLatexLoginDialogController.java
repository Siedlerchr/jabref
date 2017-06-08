package org.jabref.gui.sharelatex;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.jabref.gui.AbstractController;
import org.jabref.logic.sharelatex.SharelatexConnector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ShareLatexLoginDialogController extends AbstractController<ShareLatexLoginDialogViewModel> {

    private final SharelatexConnector connector = new SharelatexConnector();
    private final List<ShareLatexProjectViewModel> projects = new ArrayList<>();

    @FXML private Button btnCancel;
    @FXML private Button btnLogin;
    @FXML private TextField tbAddress;
    @FXML private TextField tbUsername;
    @FXML private PasswordField pfPassword;

    @FXML
    private void initialize() {
        viewModel = new ShareLatexLoginDialogViewModel();
    }

    @FXML
    private void closeDialog() {
        getStage().close();
    }

    @FXML
    private void signIn() {
        System.out.println("sign in pressed");
        System.out.println(tbAddress.getText());
        System.out.println(tbUsername.getText());
        System.out.println(pfPassword.getText());

        JsonObject jsonResponse = connector.connectToServer(tbAddress.getText(), tbUsername.getText(),
                pfPassword.getText());

        if (jsonResponse != null) {
            if (jsonResponse.has("text")) {
                System.out.println(jsonResponse.get("text").getAsString());
            }

            if (jsonResponse.has("projects")) {

                JsonArray projectArray = jsonResponse.get("projects").getAsJsonArray();

                System.out.println(projectArray);

                for (JsonElement elem : projectArray) {

                    String id = elem.getAsJsonObject().get("id").getAsString();
                    String name = elem.getAsJsonObject().get("name").getAsString();
                    String lastUpdated = elem.getAsJsonObject().get("lastUpdated").getAsString();
                    String owner =  elem.getAsJsonObject().get("owner_ref").getAsString();
                    System.out.println("ID " + id);
                    System.out.println("Name " + name);
                    System.out.println("LastUpdated " + lastUpdated);
                    System.out.println("Owner" + owner);

                    ShareLatexProjectViewModel model = new ShareLatexProjectViewModel(id, name, owner, lastUpdated);
                    projects.add(model);
                    //TODO: How do I pass this projectLIst to the other view?
                }

            }

        }
    }
}
