package com.github.jiten.demisemaces.command;

import com.github.jiten.demisemaces.mace.ItemManager;
import com.github.jiten.demisemaces.mace.MaceType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DemiseCommand extends Command {

    private final ItemManager itemManager;

    public DemiseCommand(ItemManager itemManager) {
        super("demisemaces");
        this.description = "Main command for DemiseMaces.";
        this.usageMessage = "/demisemaces give <type>";
        this.setPermission("demisemaces.admin");
        this.itemManager = itemManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.").color(NamedTextColor.RED));
            return true;
        }

        if (!player.hasPermission("demisemaces.admin")) {
            player.sendMessage(Component.text("You don't have permission to use this command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase("give")) {
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

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("give");
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
