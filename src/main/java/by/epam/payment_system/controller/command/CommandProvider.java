package by.epam.payment_system.controller.command;

import java.util.HashMap;
import java.util.Map;

import by.epam.payment_system.controller.command.impl.BlockingCommandImpl;
import by.epam.payment_system.controller.command.impl.ChangeLocaleCommandImpl;
import by.epam.payment_system.controller.command.impl.MainPageCommandImpl;
import by.epam.payment_system.controller.command.impl.TopUpCardPageCommandImpl;
import by.epam.payment_system.controller.command.impl.LoginCommandImpl;
import by.epam.payment_system.controller.command.impl.LogoutCommandImpl;
import by.epam.payment_system.controller.command.impl.RegistrationCommandImpl;
import by.epam.payment_system.controller.command.impl.SaveAdditionalClientDataCommandImpl;
import by.epam.payment_system.controller.command.impl.TopUpCardCommandImpl;


public class CommandProvider {
	private Map<CommandName, Command> commands = new HashMap<>();

	public CommandProvider() {
		
		commands.put(CommandName.LOGIN, new LoginCommandImpl());
		commands.put(CommandName.GO_TO_MAIN_PAGE, new MainPageCommandImpl());
		commands.put(CommandName.LOGOUT, new LogoutCommandImpl());
		commands.put(CommandName.REGISTRATION, new RegistrationCommandImpl());
		commands.put(CommandName.SAVE_ADDITIONAL_CLIENT_DATA, new SaveAdditionalClientDataCommandImpl());
		commands.put(CommandName.EN, new ChangeLocaleCommandImpl());
		commands.put(CommandName.RU, new ChangeLocaleCommandImpl());
		commands.put(CommandName.BLOCKING, new BlockingCommandImpl());
		commands.put(CommandName.GO_TO_TOP_UP_CARD_PAGE, new TopUpCardPageCommandImpl());
		commands.put(CommandName.TOP_UP_CARD, new TopUpCardCommandImpl());
	}

	public Command takeCommand(String name) {
		CommandName commandName;

		commandName = CommandName.valueOf(name.toUpperCase());

		return commands.get(commandName);
	}

}
