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
                meta.displayName(miniMessage.deserialize("<aqua><bold>Wind Mace</bold></aqua>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<white>A mace forged from the howling winds.</white>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Right Click:</yellow> <aqua>Dash Forward</aqua></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Press F:</yellow> <aqua>Launch Upwards</aqua></gray>").decoration(TextDecoration.ITALIC, false));
                break;
            case VOID:
                meta.displayName(miniMessage.deserialize("<dark_purple><bold>Void Mace</bold></dark_purple>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<white>A mace that controls gravity itself.</white>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Right Click:</yellow> <dark_purple>Push Entities</dark_purple></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Press F:</yellow> <dark_purple>Pull Entities</dark_purple></gray>").decoration(TextDecoration.ITALIC, false));
                break;
            case FROST:
                meta.displayName(miniMessage.deserialize("<blue><bold>Frost Mace</bold></blue>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<white>A freezing relic from the tundra.</white>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Right Click:</yellow> <blue>Freeze Nearby</blue></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Press F:</yellow> <blue>Throw Mace</blue></gray>").decoration(TextDecoration.ITALIC, false));
                break;
            case EXPLOSIVE:
                meta.displayName(miniMessage.deserialize("<red><bold>Detonate Mace</bold></red>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<white>A volatile weapon that loves destruction.</white>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.empty());
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Passive:</yellow> <red>Explosive Hits</red></gray>").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage.deserialize("<gray>▶ <yellow>Press F:</yellow> <red>AOE Detonation</red></gray>").decoration(TextDecoration.ITALIC, false));
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
