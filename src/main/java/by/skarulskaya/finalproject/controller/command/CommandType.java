package by.skarulskaya.finalproject.controller.command;

import by.skarulskaya.finalproject.controller.command.impl.*;
import by.skarulskaya.finalproject.exception.CommandException;

import java.util.Locale;
import java.util.Optional;

public enum CommandType {
    SIGN_IN(new SignIn()),
    SIGN_OUT(new SignOut()),
    REGISTRATION(new Registration()),
    FIND_ALL_ITEMS(new FindAllItems()),
    FIND_BY_CATEGORY_ITEMS(new FindByCategoryItems()),
    SORT_ITEMS(new SortItems()),
    OPEN_ITEM_PAGE(new OpenItemPage()),
    CHANGE_LANGUAGE(new ChangeLanguage());

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
