package by.skarulskaya.finalproject.controller.command;

import by.skarulskaya.finalproject.controller.command.impl.*;
import by.skarulskaya.finalproject.controller.command.impl.admin.*;
import by.skarulskaya.finalproject.controller.command.impl.common.*;
import by.skarulskaya.finalproject.controller.command.impl.customer.*;
import by.skarulskaya.finalproject.exception.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum CommandType {
    SIGN_IN(new SignIn()),
    SIGN_OUT(new SignOut()),
    REGISTRATION(new Registration()),
    FIND_ALL_ITEMS(new FindAllItems()),
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
    CANCEL_ORDER(new CancelOrder()),
    UPLOAD_CUSTOMERS(new UploadCustomers()),
    UPLOAD_ADMINS(new UploadAdmins()),
    DELETE_ADMIN(new DeleteAdmin()),
    REGISTER_ADMIN(new RegisterAdmin()),
    FIND_USER_BY_ID(new FindUserById()),
    CHANGE_USER_STATUS(new ChangeUserStatus()),
    OPEN_UPDATE_ITEM_PAGE(new OpenUpdateItemPage()),
    UPDATE_ITEM(new UpdateItem()),
    DELETE_ITEM(new DeleteItem()),
    CREATE_CATEGORY(new CreateCategory()),
    CHANGE_CATEGORY(new ChangeCategory()),
    DELETE_CATEGORY(new DeleteCategory()),
    CREATE_SIZE(new CreateSize()),
    CHANGE_SIZE(new ChangeSize()),
    DELETE_SIZE(new DeleteSize());

    private static final Logger logger = LogManager.getLogger();
    private final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public static Command provideCommand(String command) throws CommandException {
        if(command == null || command.isBlank()) {
            logger.error("Command Type was provided with empty command");
            throw new CommandException("null pointer");
        }
        try {
            CommandType commandType = CommandType.valueOf(command.toUpperCase());
            return commandType.getCommand();
        }
        catch (IllegalArgumentException e) {
            logger.error("Command Type was provided with nonexistent command");
            throw new CommandException("Can't find such command: " + command);
        }
    }
}
