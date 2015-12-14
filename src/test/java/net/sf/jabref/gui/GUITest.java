package net.sf.jabref.gui;

import net.sf.jabref.JabRefMain;
import net.sf.jabref.gui.preftabs.PreferencesDialog;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.dependency.jsr305.Nonnull;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import javax.swing.*;

import static org.assertj.swing.finder.WindowFinder.findDialog;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

public class GUITest extends AssertJSwingJUnitTestCase {

    @Override
    protected void onSetUp() {
        application(JabRefMain.class).start();
    }

    @Test
    public void testExit() {
        FrameFixture mainFrame = findFrame(JabRefFrame.class).using(robot());
        exitJabRef(mainFrame);
    }

    private void exitJabRef(FrameFixture mainFrame) {
        mainFrame.menuItemWithPath("File", "Quit").click();
    }

    @Test
    public void testNewFile() {
        FrameFixture mainFrame = findFrame(JabRefFrame.class).using(robot());
        newDatabase(mainFrame);
        mainFrame.menuItemWithPath("File", "Close database").click();
        exitJabRef(mainFrame);
    }

    private void newDatabase(FrameFixture mainFrame) {
        mainFrame.menuItemWithPath("File", "New database").click();
    }

    @Test
    public void testCreateBibtexEntry() {
        FrameFixture mainFrame = findFrame(JabRefFrame.class).using(robot());

        newDatabase(mainFrame);

        mainFrame.menuItemWithPath("BibTeX", "New entry").click();
        findDialog(EntryTypeDialog.class).using(robot()).button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override protected boolean isMatching(@Nonnull JButton jButton) {
                return "Book".equals(jButton.getText());
            }
        }).click();

        exitJabRef(mainFrame);
    }

    @Test
    public void testOpenAndSavePreferences() {
        FrameFixture mainFrame = findFrame(JabRefFrame.class).using(robot());

        mainFrame.menuItemWithPath("Options", "Preferences").click();
        findDialog(PreferencesDialog.class).using(robot()).button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override protected boolean isMatching(@Nonnull JButton jButton) {
                return "OK".equals(jButton.getText());
            }
        }).click();

        exitJabRef(mainFrame);
    }

}
