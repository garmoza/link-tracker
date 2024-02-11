package edu.java.bot.processor;

public class HelpCommandProcessor implements CommandProcessor {

    private CommandProcessor nextProcessor;

    @Override
    public void process(String command) {
        if (command.equals("/help")) {
            System.out.println("Processing /help");
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
