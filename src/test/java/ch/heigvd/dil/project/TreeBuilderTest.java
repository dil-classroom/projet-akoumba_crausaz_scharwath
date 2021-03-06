package ch.heigvd.dil.project;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.heigvd.dil.project.core.App;
import ch.heigvd.dil.project.core.FilesManager.TreeBuilder;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test class for the TreeBuilder class.
 *
 * @author Akoumba Ludivine
 * @author Crausaz Nicolas
 * @author Scharwath Maxime
 */
public class TreeBuilderTest {
    /** Delete the temporary folder after and before the tests. */
    @BeforeAll
    @AfterAll
    static void init() {
        File buildFolder = new File("./data/site/build");
        try {
            FileUtils.deleteDirectory(buildFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Should build some files */
    @Test
    public void shouldBuildSomeFiles() throws IOException {
        var src = new File("data/site/");
        var dest = new File("data/site/build/");
        var treeBuilder = new TreeBuilder(src, dest);
        App.getInstance().setRootPath("data/site");
        treeBuilder.build();
        assertTrue(dest.exists());
        assertTrue(
                FileUtils.listFilesAndDirs(
                                        dest, FileFileFilter.INSTANCE, DirectoryFileFilter.INSTANCE)
                                .size()
                        > 0);
    }
}
