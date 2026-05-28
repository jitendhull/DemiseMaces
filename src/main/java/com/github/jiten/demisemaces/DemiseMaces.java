package com.github.jiten.demisemaces;

import com.github.jiten.demisemaces.command.DemiseCommand;
import com.github.jiten.demisemaces.listener.MaceListener;
import com.github.jiten.demisemaces.mace.CooldownManager;
import com.github.jiten.demisemaces.mace.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DemiseMaces extends JavaPlugin {

    private ItemManager itemManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        // Initialize managers
        itemManager = new ItemManager(this);
        cooldownManager = new CooldownManager();

        // Register event listener
        getServer().getPluginManager().registerEvents(new MaceListener(this, itemManager, cooldownManager), this);

        // Register commands
        DemiseCommand command = new DemiseCommand(itemManager);
        getCommand("demisemaces").setExecutor(command);
        getCommand("demisemaces").setTabCompleter(command);

        printBanner();
        getLogger().info("DemiseMaces has been enabled!");
        getLogger().info("Ready to deal some demise with Maces!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DemiseMaces has been disabled.");
    }

    private void printBanner() {
        String[] banner = {
            "  _____                 _          __  __                     ",
            " |  __ \\               (_)        |  \\/  |                    ",
            " | |  | | ___ _ __ ___  _ ___  ___| \\  / | __ _  ___ ___  ___ ",
            " | |  | |/ _ \\ '_ ` _ \\| / __|/ _ \\ |\\/| |/ _` |/ __/ _ \\/ __|",
            " | |__| |  __/ | | | | | \\__ \\  __/ |  | | (_| | (_|  __/\\__ \\",
            " |_____/ \\___|_| |_| |_|_|___/\\___|_|  |_|\\__,_|\\___\\___||___/"
        };

        for (String line : banner) {
            getLogger().info(line);
        }
    }
}
