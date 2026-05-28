package com.github.jiten.demisemaces.listener;

import com.github.jiten.demisemaces.mace.CooldownManager;
import com.github.jiten.demisemaces.mace.ItemManager;
import com.github.jiten.demisemaces.mace.MaceType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collection;

public class MaceListener implements Listener {

    private final JavaPlugin plugin;
    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final NamespacedKey snowballKey;

    public MaceListener(JavaPlugin plugin, ItemManager itemManager, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
        this.snowballKey = new NamespacedKey(plugin, "frost_mace_throw");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        MaceType type = itemManager.getMaceType(item);

        if (type == null) return;

        event.setCancelled(true); // Prevent normal interaction if any

        switch (type) {
            case WIND:
                handleWindDash(player);
                break;
            case VOID:
                handleVoidPush(player);
                break;
            case FROST:
                handleFrostFreeze(player);
                break;
            case EXPLOSIVE:
                // Passive on hit, maybe secondary on right click?
                // The plan said "Press F: AOE Detonation". We don't have right-click for Explosive yet.
                break;
        }
    }

    @EventHandler
    public void onPlayerSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        
        // Wait, if they have the mace in main hand and press F, the mace goes to offhand.
        // We probably want to cancel the event so it stays in the main hand, but trigger the ability.
        
        // Check what item they are holding before the swap
        ItemStack mainHandBefore = player.getInventory().getItemInMainHand();
        MaceType type = itemManager.getMaceType(mainHandBefore);

        if (type == null) return;

        event.setCancelled(true); // Prevent moving to offhand

        switch (type) {
            case WIND:
                handleWindLaunch(player);
                break;
            case VOID:
                handleVoidPull(player);
                break;
            case FROST:
                handleFrostThrow(player, mainHandBefore);
                break;
            case EXPLOSIVE:
                handleExplosiveDetonate(player);
                break;
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        MaceType type = itemManager.getMaceType(item);

        if (type == MaceType.EXPLOSIVE) {
            // Explosive Mace Passive: Small explosion on hit
            Location loc = event.getEntity().getLocation();
            loc.getWorld().createExplosion(loc, 0.0F, false, false); // Visual explosion, 0 damage blocks
            loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.5f);
            
            // Add some extra damage or just the effect
            double bonusDamage = plugin.getConfig().getDouble("maces.explosive.passive.bonus_damage", 5.0);
            event.setDamage(event.getDamage() + bonusDamage);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball snowball) {
            if (snowball.getPersistentDataContainer().has(snowballKey, PersistentDataType.BYTE)) {
                if (event.getHitEntity() instanceof LivingEntity target) {
                    double damage = plugin.getConfig().getDouble("maces.frost.throw.damage", 20.0);
                    target.damage(damage, snowball.getShooter() instanceof Entity ? (Entity) snowball.getShooter() : null);
                    target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);
                    target.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
    }

    // --- Abilities Implementation ---

    private void handleWindDash(Player player) {
        int cooldown = plugin.getConfig().getInt("maces.wind.dash.cooldown", 20);
        if (checkCooldown(player, "wind_dash", cooldown)) return;
        
        double velocity = plugin.getConfig().getDouble("maces.wind.dash.velocity", 2.5);
        Vector direction = player.getLocation().getDirection().normalize().multiply(velocity);
        player.setVelocity(direction);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
    }

    private void handleWindLaunch(Player player) {
        int cooldown = plugin.getConfig().getInt("maces.wind.launch.cooldown", 20);
        if (checkCooldown(player, "wind_launch", cooldown)) return;

        double velocity = plugin.getConfig().getDouble("maces.wind.launch.velocity", 2.5);
        player.setVelocity(new Vector(0, velocity, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.GUST, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
    }

    private void handleVoidPush(Player player) {
        int cooldown = plugin.getConfig().getInt("maces.void.push.cooldown", 20);
        if (checkCooldown(player, "void_push", cooldown)) return;

        double radius = plugin.getConfig().getDouble("maces.void.push.radius", 15.0);
        double velocity = plugin.getConfig().getDouble("maces.void.push.velocity", 2.5);

        Location center = player.getLocation();
        Collection<Entity> nearby = center.getWorld().getNearbyEntities(center, radius, radius, radius);
        for (Entity entity : nearby) {
            if (entity.equals(player)) continue;
            Vector push = entity.getLocation().toVector().subtract(center.toVector()).normalize().multiply(velocity);
            entity.setVelocity(push);
        }
        player.getWorld().playSound(center, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 0.5f);
        player.getWorld().spawnParticle(Particle.PORTAL, center, 50, 1, 1, 1, 0.5);
    }

    private void handleVoidPull(Player player) {
        int cooldown = plugin.getConfig().getInt("maces.void.pull.cooldown", 20);
        if (checkCooldown(player, "void_pull", cooldown)) return;

        double radius = plugin.getConfig().getDouble("maces.void.pull.radius", 15.0);
        double velocity = plugin.getConfig().getDouble("maces.void.pull.velocity", 1.5);

        Location center = player.getLocation();
        Collection<Entity> nearby = center.getWorld().getNearbyEntities(center, radius, radius, radius);
        for (Entity entity : nearby) {
            if (entity.equals(player)) continue;
            Vector pull = center.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(velocity);
            entity.setVelocity(pull);
        }
        player.getWorld().playSound(center, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, center, 50, 3, 3, 3, 0.1);
    }

    private void handleFrostFreeze(Player player) {
        int cooldown = plugin.getConfig().getInt("maces.frost.freeze.cooldown", 20);
        if (checkCooldown(player, "frost_freeze", cooldown)) return;

        double radius = plugin.getConfig().getDouble("maces.frost.freeze.radius", 15.0);
        int durationTicks = plugin.getConfig().getInt("maces.frost.freeze.duration_ticks", 300);

        Location center = player.getLocation();
        Collection<Entity> nearby = center.getWorld().getNearbyEntities(center, radius, radius, radius);
        for (Entity entity : nearby) {
            if (entity.equals(player)) continue;
            if (entity instanceof LivingEntity living) {
                living.setFreezeTicks(durationTicks);
                living.getWorld().spawnParticle(Particle.SNOWFLAKE, living.getLocation(), 20, 0.5, 1, 0.5, 0.05);
            }
        }
        player.getWorld().playSound(center, Sound.ENTITY_PLAYER_HURT_FREEZE, 1.0f, 1.0f);
    }

    private void handleFrostThrow(Player player, ItemStack maceItem) {
        int cooldown = plugin.getConfig().getInt("maces.frost.throw.cooldown", 20);
        if (checkCooldown(player, "frost_throw", cooldown)) return;

        double velocity = plugin.getConfig().getDouble("maces.frost.throw.velocity", 2.5);

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setItem(maceItem); // Disguise as mace
        snowball.getPersistentDataContainer().set(snowballKey, PersistentDataType.BYTE, (byte) 1);
        snowball.setVelocity(player.getLocation().getDirection().multiply(velocity));
        
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0f, 0.5f);
    }

    private void handleExplosiveDetonate(Player player) {
        int cooldown = plugin.getConfig().getInt("maces.explosive.detonate.cooldown", 20);
        if (checkCooldown(player, "explosive_detonate", cooldown)) return;

        float explosionSize = (float) plugin.getConfig().getDouble("maces.explosive.detonate.explosion_size", 5.0);
        double radius = plugin.getConfig().getDouble("maces.explosive.detonate.radius", 15.0);
        double pushVelocity = plugin.getConfig().getDouble("maces.explosive.detonate.push_velocity", 3.0);

        Location center = player.getLocation();
        center.getWorld().createExplosion(center, explosionSize, false, false);
        
        Collection<Entity> nearby = center.getWorld().getNearbyEntities(center, radius, radius, radius);
        for (Entity entity : nearby) {
            if (entity.equals(player)) continue;
            Vector push = entity.getLocation().toVector().subtract(center.toVector()).normalize().multiply(pushVelocity);
            entity.setVelocity(push);
        }
    }

    private boolean checkCooldown(Player player, String ability, int seconds) {
        if (cooldownManager.isOnCooldown(player, ability)) {
            long remaining = cooldownManager.getRemainingSeconds(player, ability);
            player.sendMessage(Component.text("Ability is on cooldown for " + remaining + "s.").color(NamedTextColor.RED));
            return true;
        }
        cooldownManager.setCooldown(player, ability, seconds);
        return false;
    }
}
