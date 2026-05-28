package com.github.jiten.demisemaces.mace;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    // Map of Player UUID -> (Map of Ability Name -> Expiration Time in millis)
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public boolean isOnCooldown(Player player, String abilityName) {
        if (!cooldowns.containsKey(player.getUniqueId())) return false;
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (!playerCooldowns.containsKey(abilityName)) return false;
        
        if (playerCooldowns.get(abilityName) > System.currentTimeMillis()) {
            return true;
        } else {
            playerCooldowns.remove(abilityName);
            if (playerCooldowns.isEmpty()) {
                cooldowns.remove(player.getUniqueId());
            }
            return false;
        }
    }

    public void setCooldown(Player player, String abilityName, long seconds) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                 .put(abilityName, System.currentTimeMillis() + (seconds * 1000));
    }
    
    public long getRemainingSeconds(Player player, String abilityName) {
        if (!isOnCooldown(player, abilityName)) return 0;
        long expiry = cooldowns.get(player.getUniqueId()).get(abilityName);
        return (expiry - System.currentTimeMillis()) / 1000 + 1;
    }
}
