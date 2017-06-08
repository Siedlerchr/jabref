package org.jabref.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import javafx.application.Platform;

import org.jabref.gui.sharelatex.ShareLatexLoginDialogView;

public class ManageShareLatexAction extends AbstractAction {

    public ManageShareLatexAction() {
        super();
        putValue(Action.NAME, "manage sharelatex");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(() -> new ShareLatexLoginDialogView().show());

    }

}
