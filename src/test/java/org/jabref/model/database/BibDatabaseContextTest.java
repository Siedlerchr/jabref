package org.jabref.model.database;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jabref.model.metadata.FileDirectoryPreferences;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

public class BibDatabaseContextTest {

    private String currentWorkingDir;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    FileDirectoryPreferences preferences;

    @Before
    public void setUp() {
        Map<String, String> mapFieldDirs = new HashMap<>();
        mapFieldDirs.put("pdf", "/home/saulius/jabref");
        preferences = new FileDirectoryPreferences("saulius", mapFieldDirs, true);
        currentWorkingDir = Paths.get(System.getProperty("user.dir")).toString();
    }

    @Test
    public void getFileDirectoriesWithEmptyDbParent() {
        BibDatabaseContext dbContext = new BibDatabaseContext();
        dbContext.setDatabaseFile(new File("biblio.bib"));
        List<String> fileDirectories = dbContext.getFileDirectories( "file", preferences );
        assertTrue(fileDirectories.get(0).equals(currentWorkingDir));
    }

    @Test
    public void getFileDirectoriesWithRelativeDbParent() {
        String dbDirectory = "relative/subdir";
        BibDatabaseContext dbContext = new BibDatabaseContext();
        dbContext.setDatabaseFile(new File(dbDirectory + "/" + "biblio.bib"));
        List<String> fileDirectories = dbContext.getFileDirectories("file", preferences);
        assertTrue(fileDirectories.get(0).equals(currentWorkingDir + "/" + dbDirectory));
    }

    @Test
    public void getFileDirectoriesWithRelativeDottedDbParent() {
        String dbDirectory = "./relative/subdir";
        BibDatabaseContext dbContext = new BibDatabaseContext();
        dbContext.setDatabaseFile(new File(dbDirectory + "/" + "biblio.bib"));
        List<String> fileDirectories = dbContext.getFileDirectories("file", preferences);
        assertTrue(fileDirectories.get(0).equals(currentWorkingDir + "/" + dbDirectory));
    }

    @Test
    public void getFileDirectoriesWithAbsoluteDbParent() {
        String dbDirectory = "/absolute/subdir";
        BibDatabaseContext dbContext = new BibDatabaseContext();
        dbContext.setDatabaseFile(new File(dbDirectory + "/" + "biblio.bib"));
        List<String> fileDirectories = dbContext.getFileDirectories("file", preferences);
        assertTrue(fileDirectories.get(0).equals(dbDirectory));
    }
}
