package by.skarulskaya.finalproject.controller.command;

import by.skarulskaya.finalproject.controller.command.impl.Registration;
import by.skarulskaya.finalproject.controller.command.impl.SignIn;
import by.skarulskaya.finalproject.exception.CommandException;

import java.util.Locale;
import java.util.Optional;

public enum CommandType {
    SIGN_IN(new SignIn()),
    REGISTRATION(new Registration());

    private final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public static Command provideCommand(String command) throws CommandException {
        if(command == null || command.isBlank()) {
            throw new CommandException("null pointer");
        }
        try {
            CommandType commandType = CommandType.valueOf(command.toUpperCase());
            return commandType.getCommand();
        }
        catch (IllegalArgumentException e) {
            throw new CommandException("Can't find such command: " + command);
        }
    }
}
