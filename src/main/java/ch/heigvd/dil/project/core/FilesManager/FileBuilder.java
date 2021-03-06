package ch.heigvd.dil.project.core.FilesManager;

import ch.heigvd.dil.project.core.App;
import ch.heigvd.dil.project.core.PageConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.*;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Class used to parse and build files
 *
 * @author Akoumba Ludivine
 * @author Crausaz Nicolas
 * @author Scharwath Maxime
 */
public class FileBuilder {
    private final File fileSource;
    private final File fileDestination;
    private PageConfiguration pageConfig;
    private String bodyContent = "";
    private boolean isCompiled = false;

    /**
     * Create a new file builder
     *
     * @param fileSource source file
     * @param fileDestination destination file after build
     */
    public FileBuilder(File fileSource, File fileDestination) {
        this.fileSource = fileSource;
        this.fileDestination = fileDestination;
    }

    /**
     * Parse yaml to a page configuration
     *
     * @param yaml yaml to parse
     * @throws JsonProcessingException if error while parsing
     */
    private void parseYaml(String yaml) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        pageConfig = mapper.readValue(yaml, PageConfiguration.class);
    }

    /**
     * Parse markdown content as HTML
     *
     * @param markdown markdown content to parse
     */
    private void parseMarkdown(String markdown) {
        markdown = markdown.replaceAll(".md", ".html");
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        bodyContent = htmlRenderer.render(document);
    }

    /**
     * Compile a page file to a buildable state
     *
     * @throws IOException if file not found or is not well formatted
     */
    public void compile() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(fileSource));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        br.close();
        String[] parts = sb.toString().split("---", 2);
        if (parts.length != 2) {
            throw new IOException("File is not well formatted");
        }

        parseYaml(parts[0]);
        parseMarkdown(parts[1]);
        isCompiled = true;
    }

    /**
     * Compile and write html file to fileDestination
     *
     * @throws IOException if the file cannot be written
     */
    public void build() throws IOException {
        if (!isCompiled) {
            compile();
        }

        var build =
                Injector.injectLayout(
                        Path.of(App.getInstance().getRootPath() + "/layouts/layout"),
                        App.getInstance().getRootConfig(),
                        pageConfig,
                        bodyContent);

        FileUtils.createParentDirectories(fileDestination);
        var writer = new FileWriter(fileDestination);
        writer.write(build);
        writer.close();
    }
}
