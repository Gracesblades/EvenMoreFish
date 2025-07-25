package com.oheers.fish.commands;

import com.oheers.fish.EvenMoreFish;
import com.oheers.fish.database.DatabaseUtil;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminDatabaseCommand extends CommandAPICommand {
    public AdminDatabaseCommand() {
        super("database");

        withPermission("emf.admin.debug.database");
        setSubcommands(List.of(
                dropFlywayCommand(),
                repairFlywayCommand(),
                cleanFlywayCommand(),
                migrateToLatest()
        ));
    }

    public CommandAPICommand dropFlywayCommand() {
        return new CommandAPICommand("drop-flyway")
                .withPermission("emf.admin.debug.database.flyway")
                .withShortDescription("Drops the flyway schema history, useful for when the database breaks")
                .executes((commandSender, commandArguments) -> {
                            if (isLogDbError(commandSender)) {
                                return;
                            }
                            EvenMoreFish.getInstance().getPluginDataManager().getDatabase().getMigrationManager().dropFlywaySchemaHistory();
                            commandSender.sendMessage("Dropped flyway schema history.");
                        }
                );
    }

    public CommandAPICommand repairFlywayCommand() {
        return new CommandAPICommand("repair-flyway")
                .withPermission("emf.admin.debug.database.flyway")
                .withShortDescription("Attempt to repair the database")
                .executes((commandSender, commandArguments) -> {
                    if (isLogDbError(commandSender)) {
                        return;
                    }
                    commandSender.sendMessage("Attempting to repair the migrations, check the logs.");
                    EvenMoreFish.getInstance().getPluginDataManager().getDatabase().getMigrationManager().repairFlyway();
                });
    }

    public CommandAPICommand cleanFlywayCommand() {
        return new CommandAPICommand("clean-flyway")
                .withShortDescription("Attempt to clean the database")
                .withPermission("emf.admin.debug.database.clean")
                .executes((commandSender, commandArguments) -> {
                    if (isLogDbError(commandSender)) {
                        return;
                    }
                    commandSender.sendMessage("Attempting to clean flyway, check the logs.");
                    EvenMoreFish.getInstance().getPluginDataManager().getDatabase().getMigrationManager().cleanFlyway();
                });
    }

    public CommandAPICommand migrateToLatest() {
        return new CommandAPICommand("migrate-to-latest")
                .withShortDescription("Migrate to the latest DB version.")
                .withPermission("emf.admin.debug.database.migrate")
                .executes((commandSender, commandArguments) -> {
                    if (isLogDbError(commandSender)) {
                        return;
                    }
                    EvenMoreFish.getInstance().getPluginDataManager().getDatabase().migrateFromDatabaseVersionToLatest();
                });
    }

    private boolean isLogDbError(final CommandSender sender) {
        if (!DatabaseUtil.isDatabaseOnline()) {
            sender.sendMessage("Database is offline.");
            return true;
        }
        return false;
    }

}
