package net.sf.jabref.gui;

import net.sf.jabref.JabRefException;
import net.sf.jabref.JabRefMain;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.gui.preftabs.PreferencesDialog;
import net.sf.jabref.logic.l10n.Localization;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.dependency.jsr305.Nonnull;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.swing.finder.WindowFinder.findDialog;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

public class GUITest extends AssertJSwingJUnitTestCase {

    private AWTExceptionHandler awtExceptionHandler;

    @BeforeClass
    public static void savePreferences() throws JabRefException, InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            try {
                JabRefPreferences.getInstance().exportPreferences("jabref.prefs.tmp");
            } catch (JabRefException e) {
                throw new RuntimeException(e);
            }
            JabRefPreferences.getInstance().defaults.keySet().forEach(key -> JabRefPreferences.getInstance().remove(key));
            JabRefPreferences.getInstance().put(JabRefPreferences.LANGUAGE, "en");
        });
    }

    @AfterClass
    public static void restorePreferences() throws JabRefException, InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            try {
                JabRefPreferences.getInstance().importPreferences("jabref.prefs.tmp");
                Files.delete(Paths.get("jabref.prefs.tmp"));
            } catch (JabRefException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void onSetUp() {
        awtExceptionHandler = new AWTExceptionHandler();
        awtExceptionHandler.installExceptionDetectionInEDT();

        application(JabRefMain.class).start();
    }

    @Test
    public void testExit() {
        FrameFixture mainFrame = findFrame(JabRefFrame.class).using(robot());
        exitJabRef(mainFrame);
    }

    private void exitJabRef(FrameFixture mainFrame) {
        mainFrame.menuItemWithPath("File", "Quit").click();
        awtExceptionHandler.assertNoExceptions();
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

            @Override
            protected boolean isMatching(@Nonnull JButton jButton) {
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

            @Override
            protected boolean isMatching(@Nonnull JButton jButton) {
                return "OK".equals(jButton.getText());
            }
        }).click();

        exitJabRef(mainFrame);
    }

    @Test
    public void testViewChanges() {
        FrameFixture mainFrame = findFrame(JabRefFrame.class).using(robot());

        newDatabase(mainFrame);

        mainFrame.menuItemWithPath("View", "Increase table font size").click();
        mainFrame.menuItemWithPath("View", "Decrease table font size").click();
        mainFrame.menuItemWithPath("View", "Web search").click();
        mainFrame.menuItemWithPath("View", "Toggle groups interface").click();
        mainFrame.menuItemWithPath("View", "Toggle entry preview").click();
        mainFrame.menuItemWithPath("View", "Switch preview layout").click();
        mainFrame.menuItemWithPath("View", "Hide/show toolbar").click();
        mainFrame.menuItemWithPath("View", "Focus entry table").click();

        exitJabRef(mainFrame);
    }

}
