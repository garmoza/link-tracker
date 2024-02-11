package edu.java.bot.processor;

public class UntrackCommandProcessor implements CommandProcessor {

    private CommandProcessor nextProcessor;

    @Override
    public void process(String command) {
        if (command.equals("/untrack")) {
            System.out.println("Processing /untrack");
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
