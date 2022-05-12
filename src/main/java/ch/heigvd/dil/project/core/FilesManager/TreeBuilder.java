package ch.heigvd.dil.project.core.FilesManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * Class used to make actions on file structures
 *
 * @author Akoumba Ludivine
 * @author Crausaz Nicolas
 * @author Scharwath Maxime
 */
public class TreeBuilder {
    private static final Logger LOG = Logger.getLogger(TreeBuilder.class.getName());
    private final File root;
    private final File dest;

    private final ArrayList<Path> ignoreFiles;

    /**
     * Create a new TreeBuilder
     *
     * @param root source structure
     * @param dest destination folder
     */
    public TreeBuilder(File root, File dest) {
        this.root = root;
        this.dest = dest;
        ignoreFiles = new ArrayList<>();
        addIgnoreFile(Path.of("build"));
        addIgnoreFile(Path.of("layouts"));
        addIgnoreFile(Path.of("config.yml"));
    }

    /**
     * Build files of structures and move them to destination
     *
     * @throws IOException if source / destination not found
     */
    public void build() throws IOException {
        Files.walkFileTree(
                root.toPath(),
                new SimpleFileVisitor<>() {
                    private void buildFile(Path path) throws IOException {
                        var relativePath = root.toPath().relativize(path);
                        var destFile = new File(dest, relativePath.toString());
                        var file = path.toFile();
                        if (file.getName().endsWith(".md")) {
                            var htmlFile =
                                    new File(
                                            destFile.getParentFile(),
                                            destFile.getName().replace(".md", ".html"));
                            new FileBuilder(file, htmlFile).build();
                        } else {
                            if (file.isDirectory()) {
                                destFile.mkdirs();
                            } else {
                                FileUtils.copyFile(file, destFile);
                            }
                        }
                        LOG.info(
                                "Copied "
                                        + file.getAbsolutePath()
                                        + " to "
                                        + destFile.getAbsolutePath());
                    }

                    private boolean visit(Path path, BasicFileAttributes attrs) {
                        if (ignoreFiles.contains(path)) return false;
                        try {
                            buildFile(path);
                        } catch (IOException e) {
                            LOG.severe(e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                            throws IOException {
                        return !visit(dir, attrs)
                                ? FileVisitResult.SKIP_SUBTREE
                                : super.preVisitDirectory(dir, attrs);
                    }

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                            throws IOException {
                        visit(path, attrs);
                        return FileVisitResult.CONTINUE;
                    }
                });
    }

    /**
     * Ignore path while building
     *
     * @param path path to ignore
     */
    private void addIgnoreFile(Path path) {
        ignoreFiles.add(root.toPath().resolve(path));
    }
}
