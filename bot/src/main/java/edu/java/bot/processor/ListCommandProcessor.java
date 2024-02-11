package edu.java.bot.processor;

public class ListCommandProcessor implements CommandProcessor {

    private CommandProcessor nextProcessor;

    @Override
    public void process(String command) {
        if (command.equals("/list")) {
            System.out.println("Processing /list");
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
