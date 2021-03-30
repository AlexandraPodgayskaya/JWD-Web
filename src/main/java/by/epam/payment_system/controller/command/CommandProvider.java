package by.epam.payment_system.controller.command;

import java.util.HashMap;
import java.util.Map;

import by.epam.payment_system.controller.command.impl.BlockCardCommandImpl;
import by.epam.payment_system.controller.command.impl.ChangeLocaleCommandImpl;
import by.epam.payment_system.controller.command.impl.FindClientCommandImpl;
import by.epam.payment_system.controller.command.impl.CloseCardCommandImpl;
import by.epam.payment_system.controller.command.impl.DefaultCommandImpl;
import by.epam.payment_system.controller.command.impl.GoToMainPageCommandImpl;
import by.epam.payment_system.controller.command.impl.GoToPaymentPageCommandImpl;
import by.epam.payment_system.controller.command.impl.GoToTopUpCardPageCommandImpl;
import by.epam.payment_system.controller.command.impl.LoginCommandImpl;
import by.epam.payment_system.controller.command.impl.LogoutCommandImpl;
import by.epam.payment_system.controller.command.impl.MakePaymentCommandImpl;
import by.epam.payment_system.controller.command.impl.RegisterCommandImpl;
import by.epam.payment_system.controller.command.impl.SaveAdditionalClientDataCommandImpl;
import by.epam.payment_system.controller.command.impl.ShowTransactionLogCommandImpl;
import by.epam.payment_system.controller.command.impl.TopUpCardCommandImpl;


public class CommandProvider {
	
	private Map<CommandName, Command> commands = new HashMap<>();

	public CommandProvider() {
		
		commands.put(CommandName.LOGIN, new LoginCommandImpl());
		commands.put(CommandName.GO_TO_MAIN_PAGE, new GoToMainPageCommandImpl());
		commands.put(CommandName.LOGOUT, new LogoutCommandImpl());
		commands.put(CommandName.REGISTRATION, new RegisterCommandImpl());
		commands.put(CommandName.SAVE_ADDITIONAL_CLIENT_DATA, new SaveAdditionalClientDataCommandImpl());
		commands.put(CommandName.EN, new ChangeLocaleCommandImpl());
		commands.put(CommandName.RU, new ChangeLocaleCommandImpl());
		commands.put(CommandName.BLOCKING, new BlockCardCommandImpl());
		commands.put(CommandName.GO_TO_TOP_UP_CARD_PAGE, new GoToTopUpCardPageCommandImpl());
		commands.put(CommandName.TOP_UP_CARD, new TopUpCardCommandImpl());
		commands.put(CommandName.GO_TO_PAYMENT_PAGE, new GoToPaymentPageCommandImpl());
		commands.put(CommandName.CLOSE_CARD, new CloseCardCommandImpl());
		commands.put(CommandName.PAY, new MakePaymentCommandImpl());
		commands.put(CommandName.SHOW_ACCOUNT_LOG, new ShowTransactionLogCommandImpl());
		commands.put(CommandName.SHOW_CARD_LOG, new ShowTransactionLogCommandImpl());
		commands.put(CommandName.DEFAULT_COMMAND, new DefaultCommandImpl());
		commands.put(CommandName.CLIENT_SEARCH, new FindClientCommandImpl());
	
	}

	public Command takeCommand(String name) {
		CommandName commandName;

		try {
			commandName = CommandName.valueOf(name.toUpperCase());
		} catch (IllegalArgumentException e){
			commandName = CommandName.DEFAULT_COMMAND;
		}
		
		return commands.get(commandName);
	}

}
