package com.github.jiten.demisemaces.mace;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final NamespacedKey maceKey;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ItemManager(JavaPlugin plugin) {
        this.maceKey = new NamespacedKey(plugin, "mace_type");
    }

    public NamespacedKey getMaceKey() {
        return maceKey;
    }

    public ItemStack createMace(MaceType type) {
        ItemStack item = new ItemStack(Material.MACE);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());

        switch (type) {
            case WIND:
                meta.displayName(miniMessage.deserialize("<gradient:aqua:white><bold>✧ Wind Mace ✧</bold></gradient>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray><i>A legendary mace forged from the howling winds.</i></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Right Click:</gray> <aqua>Dash Forward</aqua>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Press F:</gray> <aqua>Launch Upwards</aqua>").decoration(TextDecoration.ITALIC, false));
                break;
            case VOID:
                meta.displayName(miniMessage.deserialize("<gradient:#5500FF:#000000><bold>✧ Void Mace ✧</bold></gradient>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray><i>A dark relic that manipulates gravity itself.</i></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Right Click:</gray> <dark_purple>Push Entities</dark_purple>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Press F:</gray> <dark_purple>Pull Entities</dark_purple>").decoration(TextDecoration.ITALIC, false));
                break;
            case FROST:
                meta.displayName(miniMessage.deserialize("<gradient:#00BFFF:#FFFFFF><bold>✧ Frost Mace ✧</bold></gradient>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray><i>An ancient weapon that freezes the very air.</i></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Right Click:</gray> <blue>Absolute Freeze</blue>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Press F:</gray> <blue>Frost Projectile</blue>").decoration(TextDecoration.ITALIC, false));
                break;
            case EXPLOSIVE:
                meta.displayName(miniMessage.deserialize("<gradient:#FF4500:#FF8C00><bold>✧ Detonate Mace ✧</bold></gradient>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray><i>A highly volatile weapon that loves destruction.</i></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Passive:</gray> <red>Explosive Strikes</red>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<dark_gray>▪</dark_gray> <gray>Press F:</gray> <red>Cataclysmic Burst</red>").decoration(TextDecoration.ITALIC, false));
                break;
        }

        meta.lore(lore);
        
        // Tag the item with PDC so we can identify it later
        meta.getPersistentDataContainer().set(maceKey, PersistentDataType.STRING, type.name());
        
        item.setItemMeta(meta);
        return item;
    }

    public MaceType getMaceType(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        String typeStr = item.getItemMeta().getPersistentDataContainer().get(maceKey, PersistentDataType.STRING);
        if (typeStr == null) return null;
        try {
            return MaceType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
