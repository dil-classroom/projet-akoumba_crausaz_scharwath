package ch.heigvd.dil.project.commands;

import picocli.CommandLine.Command;

/** This class represents the command line interface for the clean command. */
@Command(name = "clean", description = "Clean sub-command", version = "1.0")
public class CleanCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Clean command is not implemented yet.");
    }
}
