package by.skarulskaya.finalproject.controller.command;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    Router execute(HttpServletRequest request) throws CommandException;
}
