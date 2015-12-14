package net.sf.jabref.gui;

import net.sf.jabref.JabRefMain;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.finder.WindowFinder.findFrame;

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
        mainFrame.menuItemWithPath("File", "New database").click();
        mainFrame.menuItemWithPath("File", "Close database").click();
        exitJabRef(mainFrame);
    }

}
