package com.github.jiten.demisemaces.command;

import com.github.jiten.demisemaces.mace.ItemManager;
import com.github.jiten.demisemaces.mace.MaceType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DemiseCommand extends Command {

    private final JavaPlugin plugin;
    private final ItemManager itemManager;

    public DemiseCommand(JavaPlugin plugin, ItemManager itemManager) {
        super("demisemaces");
        this.description = "Main command for DemiseMaces.";
        this.usageMessage = "/demisemaces <give|reload> [type]";
        this.setPermission("demisemaces.admin");
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!sender.hasPermission("demisemaces.admin")) {
            sender.sendMessage(Component.text("You don't have permission to use this command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /demisemaces <give|reload>").color(NamedTextColor.RED));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(Component.text("DemiseMaces configuration reloaded!").color(NamedTextColor.GREEN));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can receive maces.").color(NamedTextColor.RED));
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(Component.text("Usage: /demisemaces give <mace_type>").color(NamedTextColor.RED));
                return true;
            }

            String typeStr = args[1].toUpperCase();
            MaceType type;
            try {
                type = MaceType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text("Invalid mace type! Valid types: WIND, VOID, FROST, EXPLOSIVE").color(NamedTextColor.RED));
                return true;
            }

            ItemStack mace = itemManager.createMace(type);
            player.getInventory().addItem(mace);
            player.sendMessage(Component.text("Given " + type.name() + " mace!").color(NamedTextColor.GREEN));
            return true;
        }

        sender.sendMessage(Component.text("Unknown subcommand. Usage: /demisemaces <give|reload>").color(NamedTextColor.RED));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("give".startsWith(args[0].toLowerCase())) completions.add("give");
            if ("reload".startsWith(args[0].toLowerCase())) completions.add("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (MaceType type : MaceType.values()) {
                if (type.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(type.name());
                }
            }
        }
        return completions;
    }
}
