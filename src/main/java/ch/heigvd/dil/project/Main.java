package ch.heigvd.dil.project;

import ch.heigvd.dil.project.commands.NewCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

/**
 * Main for picocli
 *
 */
@Command(
        mixinStandardHelpOptions = true,
        name = "dil",
        description = "DIL project",
        version = "0.1",
        subcommands = {
                NewCommand.class
        }
)
public class Main implements Callable<Integer> {

    @Override
    public Integer call() {
        return 0;
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }
}