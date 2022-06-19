package by.skarulskaya.finalproject.controller.command;

import by.skarulskaya.finalproject.controller.command.impl.*;
import by.skarulskaya.finalproject.controller.command.impl.admin.CancelOrder;
import by.skarulskaya.finalproject.controller.command.impl.admin.ConfirmOrder;
import by.skarulskaya.finalproject.controller.command.impl.admin.UploadOrders;
import by.skarulskaya.finalproject.controller.command.impl.common.*;
import by.skarulskaya.finalproject.controller.command.impl.customer.*;
import by.skarulskaya.finalproject.exception.CommandException;

public enum CommandType {
    SIGN_IN(new SignIn()),
    SIGN_OUT(new SignOut()),
    REGISTRATION(new Registration()),
    FIND_ALL_ITEMS(new FindAllItems()),
    FIND_BY_CATEGORY_ITEMS(new FindByCategoryItems()),
    SORT_ITEMS(new SortItems()),
    OPEN_ITEM_PAGE(new OpenItemPage()),
    ADD_ITEM_TO_CART(new AddItemToCart()),
    REMOVE_ITEM_FROM_CART(new RemoveItemFromCart()),
    UPLOAD_CART(new UploadCart()),
    ADD_MONEY_TO_ACCOUNT(new AddMoneyToAccount()),
    PAY_ORDER(new PayOrder()),
    ADD_SHIPPING_ADDRESS(new AddShippingAddress()),
    CHANGE_LANGUAGE(new ChangeLanguage()),
    CHANGE_SETTING(new ChangeSetting()),
    UPLOAD_ORDER_HISTORY(new UploadOrderHistory()),
    UPLOAD_ORDERS(new UploadOrders()),
    CONFIRM_ORDER(new ConfirmOrder()),
    CANCEL_ORDER(new CancelOrder());

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
