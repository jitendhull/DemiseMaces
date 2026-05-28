package com.github.jiten.demisemaces;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DemiseMaces extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register event listener
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("DemiseMaces has been enabled!");
        getLogger().info("Ready to deal some demise with Maces!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DemiseMaces has been disabled.");
    }

    @EventHandler
    public void onMaceHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        // Check if the player is holding a Mace in their main hand
        if (player.getInventory().getItemInMainHand().getType() != Material.MACE) {
            return;
        }

        // Play an epic lightning strike sound to represent the demise power
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.5f);
        player.sendMessage("§c§lDEMISE! §7You struck with the mighty Mace!");
    }
}
