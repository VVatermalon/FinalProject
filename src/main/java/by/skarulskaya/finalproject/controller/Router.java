package by.skarulskaya.finalproject.controller;

public class Router {
    public enum Type {
        FORWARD, REDIRECT
    }

    private Type currentType = Type.FORWARD;
    private String currentPage;

    public Router() {
        currentPage = "/jsp/pages/signIn.jsp";
    }

    public Router(String currentPage) {
        this.currentPage = currentPage;
    }

    public Router(Type currentType, String currentPage) {
        this.currentType = currentType;
        this.currentPage = currentPage;
    }

    public Type getCurrentType() {
        return currentType;
    }

    public void setCurrentType(Type currentType) {
        this.currentType = currentType;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
}
