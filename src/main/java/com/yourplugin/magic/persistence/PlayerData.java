package com.yourplugin.magic.persistence;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final Map<UUID, Integer> playerLevels = new HashMap<>();
    private final Map<UUID, Float> playerExp = new HashMap<>();

    public int getMagicLevel(Player player) {
        return playerLevels.getOrDefault(player.getUniqueId(), 1);
    }

    public void setMagicLevel(Player player, int level) {
        playerLevels.put(player.getUniqueId(), level);
    }

    public float getMagicExp(Player player) {
        return playerExp.getOrDefault(player.getUniqueId(), 0f);
    }

    public void setMagicExp(Player player, float exp) {
        playerExp.put(player.getUniqueId(), exp);
    }

    public void addMagicExp(Player player, float amount) {
        float currentExp = getMagicExp(player);
        int currentLevel = getMagicLevel(player);

        float newExp = currentExp + amount;
        float expToNextLevel = getExpToNextLevel(currentLevel);

        if (newExp >= expToNextLevel) {
            // Повышение уровня
            setMagicLevel(player, currentLevel + 1);
            setMagicExp(player, newExp - expToNextLevel);
            player.sendMessage("§6Ваш уровень магии повысился! Теперь у вас " + (currentLevel + 1) + " уровень");
        } else {
            setMagicExp(player, newExp);
        }
    }

    private float getExpToNextLevel(int currentLevel) {
        return currentLevel * 100f;
    }

    public float getExpProgress(Player player) {
        int level = getMagicLevel(player);
        float exp = getMagicExp(player);
        float expNeeded = getExpToNextLevel(level);
        return exp / expNeeded;
    }
}