package edu.java.bot.processor;

public class TrackCommandProcessor implements CommandProcessor {

    private CommandProcessor nextProcessor;

    @Override
    public void process(String command) {
        if (command.equals("/track")) {
            System.out.println("Processing /track");
        }
        if (nextProcessor != null) {
            nextProcessor.process(command);
        }
    }

    @Override
    public void nextCommandProcessor(CommandProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }
}
