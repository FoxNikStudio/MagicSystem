package com.yourplugin.magic.core;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManaSystem {
    private final MagicPlugin plugin;
    private final Map<UUID, Double> playerMana = new HashMap<>();
    private final Map<UUID, Double> playerMaxMana = new HashMap<>();

    public ManaSystem(MagicPlugin plugin) {
        this.plugin = plugin;
        startManaRegeneration();
    }

    public double getMana(Player player) {
        return playerMana.getOrDefault(player.getUniqueId(), 0.0);
    }

    public double getMaxMana(Player player) {
        return playerMaxMana.getOrDefault(player.getUniqueId(), 20.0);
    }

    public void setMana(Player player, double amount) {
        double maxMana = getMaxMana(player);
        playerMana.put(player.getUniqueId(), Math.min(amount, maxMana));
        updateManaBar(player);
    }

    public boolean consumeMana(Player player, double amount) {
        double currentMana = getMana(player);
        if (currentMana >= amount) {
            setMana(player, currentMana - amount);
            return true;
        }
        return false;
    }

    private void startManaRegeneration() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    double maxMana = getMaxMana(player);
                    double currentMana = getMana(player);
                    if (currentMana < maxMana) {
                        // Регенерация 1 маны каждые 5 секунд
                        setMana(player, Math.min(maxMana, currentMana + 1));
                    }
                }
            }
        }.runTaskTimer(plugin, 100L, 100L); // Каждые 5 секунд
    }

    private void updateManaBar(Player player) {
        double mana = getMana(player);
        double maxMana = getMaxMana(player);
        int manaPercent = (int) ((mana / maxMana) * 100);

        // Создаем текстовое представление полоски маны
        StringBuilder manaBar = new StringBuilder();
        manaBar.append(ChatColor.BLUE).append("Мана: ");
        manaBar.append(ChatColor.AQUA);

        // Добавляем заполненную часть
        int filledSegments = manaPercent / 5;
        for (int i = 0; i < filledSegments; i++) {
            manaBar.append("|");
        }

        // Добавляем пустую часть
        manaBar.append(ChatColor.GRAY);
        for (int i = filledSegments; i < 20; i++) {
            manaBar.append("|");
        }

        // Добавляем текстовое представление значений
        manaBar.append(" ").append(ChatColor.YELLOW)
                .append((int) mana).append("/").append((int) maxMana);

        // Отправляем сообщение в action bar
        try {
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(manaBar.toString())
            );
        } catch (Exception e) {
            // Fallback: отправляем в чат, если action bar не работает
            player.sendMessage(manaBar.toString());
        }
    }
}