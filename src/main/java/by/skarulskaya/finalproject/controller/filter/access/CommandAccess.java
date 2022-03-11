package by.skarulskaya.finalproject.controller.filter.access;

import by.skarulskaya.finalproject.controller.command.CommandType;

import java.util.Set;

public enum CommandAccess {
    ADMIN(Set.of(CommandType.SIGN_IN.name(),
            CommandType.REGISTRATION.name(),
            CommandType.ADD_ITEM_TO_CART.name(),
            CommandType.REMOVE_ITEM_FROM_CART.name(),
            CommandType.CHANGE_ITEM_AMOUNT_IN_CART.name(),
            CommandType.UPLOAD_CART.name(),
            CommandType.ADD_MONEY_TO_ACCOUNT.name(),
            CommandType.CHECK_OUT_ORDER.name())),
    CUSTOMER(Set.of(CommandType.SIGN_IN.name(),
            CommandType.REGISTRATION.name())),
    GUEST(Set.of(CommandType.SIGN_OUT.name(),
            CommandType.ADD_ITEM_TO_CART.name(),
            CommandType.REMOVE_ITEM_FROM_CART.name(),
            CommandType.CHANGE_ITEM_AMOUNT_IN_CART.name(),
            CommandType.UPLOAD_CART.name(),
            CommandType.ADD_MONEY_TO_ACCOUNT.name(),
            CommandType.CHECK_OUT_ORDER.name()));
    private final Set<String> commands;

    CommandAccess(Set<String> commands){
        this.commands = commands;
    }

    public Set<String> getCommands(){
        return commands;
    }
}
