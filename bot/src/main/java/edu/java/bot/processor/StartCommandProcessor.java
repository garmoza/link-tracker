package edu.java.bot.processor;

public class StartCommandProcessor implements CommandProcessor {

    private CommandProcessor nextProcessor;

    @Override
    public void process(String command) {
        if (command.equals("/start")) {
            System.out.println("Processing /start");
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
