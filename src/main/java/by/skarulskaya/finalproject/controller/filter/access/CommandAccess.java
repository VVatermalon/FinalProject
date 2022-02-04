package by.skarulskaya.finalproject.controller.filter.access;

import by.skarulskaya.finalproject.controller.command.CommandType;

import java.util.Set;

public enum CommandAccess {
    ADMIN(Set.of(CommandType.SIGN_IN.name(),
            CommandType.REGISTRATION.name())),
    CUSTOMER(Set.of(CommandType.SIGN_IN.name(),
            CommandType.REGISTRATION.name())),
    GUEST(Set.of(CommandType.SIGN_OUT.name()));
    private Set<String> commands;

    CommandAccess(Set<String> commands){
        this.commands = commands;
    }

    public Set<String> getCommands(){
        return commands;
    }
}
