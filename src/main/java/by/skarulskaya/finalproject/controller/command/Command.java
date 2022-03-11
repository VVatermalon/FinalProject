package by.skarulskaya.finalproject.controller.command;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;
}
