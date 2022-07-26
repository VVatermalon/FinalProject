package by.skarulskaya.finalproject.controller.filter.access;

import by.skarulskaya.finalproject.controller.command.CommandType;

import java.util.Set;

public enum CommandAccess {
    ADMIN(Set.of(CommandType.SIGN_IN.name(),
            CommandType.ADD_ITEM_TO_CART.name(),
            CommandType.REMOVE_ITEM_FROM_CART.name(),
            CommandType.UPLOAD_CART.name(),
            CommandType.ADD_MONEY_TO_ACCOUNT.name(),
            CommandType.ADD_SHIPPING_ADDRESS.name(),
            CommandType.PAY_ORDER.name(),
            CommandType.UPLOAD_ORDER_HISTORY.name())),
    CUSTOMER(Set.of(CommandType.SIGN_IN.name(),
            CommandType.REGISTRATION.name(),
            CommandType.UPLOAD_ORDERS.name(),
            CommandType.CONFIRM_ORDER.name(),
            CommandType.CANCEL_ORDER.name(),
            CommandType.UPLOAD_CUSTOMERS.name(),
            CommandType.UPLOAD_ADMINS.name(),
            CommandType.DELETE_ADMIN.name(),
            CommandType.REGISTER_ADMIN.name(),
            CommandType.CHANGE_USER_STATUS.name(),
            CommandType.FIND_USER_BY_ID.name(),
            CommandType.OPEN_UPDATE_ITEM_PAGE.name(),
            CommandType.UPDATE_ITEM.name(),
            CommandType.DELETE_ITEM.name())),
    GUEST(Set.of(CommandType.SIGN_OUT.name(),
            CommandType.ADD_ITEM_TO_CART.name(),
            CommandType.REMOVE_ITEM_FROM_CART.name(),
            CommandType.UPLOAD_CART.name(),
            CommandType.ADD_MONEY_TO_ACCOUNT.name(),
            CommandType.ADD_SHIPPING_ADDRESS.name(),
            CommandType.PAY_ORDER.name(),
            CommandType.CHANGE_SETTING.name(),
            CommandType.UPLOAD_ORDER_HISTORY.name(),
            CommandType.UPLOAD_ORDERS.name(),
            CommandType.CONFIRM_ORDER.name(),
            CommandType.CANCEL_ORDER.name(),
            CommandType.UPLOAD_CUSTOMERS.name(),
            CommandType.UPLOAD_ADMINS.name(),
            CommandType.DELETE_ADMIN.name(),
            CommandType.REGISTER_ADMIN.name(),
            CommandType.CHANGE_USER_STATUS.name(),
            CommandType.FIND_USER_BY_ID.name(),
            CommandType.OPEN_UPDATE_ITEM_PAGE.name(),
            CommandType.UPDATE_ITEM.name(),
            CommandType.DELETE_ITEM.name()));
    private final Set<String> commands;

    CommandAccess(Set<String> commands){
        this.commands = commands;
    }

    public Set<String> getCommands(){
        return commands;
    }
}
