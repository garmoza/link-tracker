package edu.java.bot.processor;

public interface CommandProcessor {

    void process(String command);

    void nextCommandProcessor(CommandProcessor nextProcessor);
}
