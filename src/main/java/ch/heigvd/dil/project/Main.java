package ch.heigvd.dil.project;

import ch.heigvd.dil.project.commands.BuildCommand;
import ch.heigvd.dil.project.commands.CleanCommand;
import ch.heigvd.dil.project.commands.NewCommand;
import ch.heigvd.dil.project.commands.ServeCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.io.*;
import java.util.concurrent.Callable;

import static ch.heigvd.dil.project.FilesManager.FileManager.parseFile;
import static ch.heigvd.dil.project.FilesManager.FileManager.parserMarkdownToHtml;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        mixinStandardHelpOptions = true,
        name = "dil",
        description = "DIL project",
        versionProvider = ManifestVersionProvider.class,
        subcommands = {
                NewCommand.class,
                CleanCommand.class,
                BuildCommand.class,
                ServeCommand.class
        }
)
public class Main implements Callable<Integer> {

    public static void main(String[] args) throws IOException {
        //System.exit(new CommandLine(new Main()).execute(args));

        File file = new File(new File(".").getCanonicalPath() +
                File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator +
                "ch" + File.separator + "heigvd" + File.separator + "dil" +
                File.separator + "project" +  File.separator + "test.md");

        System.out.println(file);
        String yml = parseFile(file)[0];
        String markdown = parseFile(file)[1];
        System.out.println(yml);
        System.out.println(markdown);
        String s = parserMarkdownToHtml(markdown);
        System.out.println(s);


    }

    @Override
    public Integer call () {
        return 0;
    }
}
