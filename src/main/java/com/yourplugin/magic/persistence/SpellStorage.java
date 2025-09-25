package com.yourplugin.magic.persistence;

import com.yourplugin.magic.core.MagicPlugin;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellStorage {
    private final MagicPlugin plugin;

    public SpellStorage(MagicPlugin plugin) {
        this.plugin = plugin;
    }

    public void savePlayerSpells(Player player) {
        try {
            File playerFile = getPlayerFile(player);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

            // Получаем данные о заклинаниях игрока
            Map<String, List<Map<String, Object>>> spells = plugin.getSpellManager()
                    .getPlayerSpellsData()
                    .get(player.getUniqueId());

            if (spells == null) {
                return;
            }

            // Сохраняем каждое заклинание
            for (Map.Entry<String, List<Map<String, Object>>> entry : spells.entrySet()) {
                String spellName = entry.getKey();
                List<Map<String, Object>> components = entry.getValue();

                // Сохраняем компоненты заклинания
                config.set("spells." + spellName + ".components", components);
            }

            config.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить заклинания игрока " + player.getName());
            e.printStackTrace();
        }
    }

    public void loadPlayerSpells(Player player) {
        try {
            File playerFile = getPlayerFile(player);
            if (!playerFile.exists()) {
                return;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

            if (!config.contains("spells")) {
                return;
            }

            Map<String, List<Map<String, Object>>> playerSpells = new HashMap<>();

            // Загружаем каждое заклинание
            for (String spellName : config.getConfigurationSection("spells").getKeys(false)) {
                List<Map<String, Object>> components = (List<Map<String, Object>>)
                        config.getList("spells." + spellName + ".components");
                playerSpells.put(spellName, components);
            }

            // Сохраняем загруженные заклинания
            Map<UUID, Map<String, List<Map<String, Object>>>> allData = new HashMap<>();
            allData.put(player.getUniqueId(), playerSpells);
            plugin.getSpellManager().setPlayerSpellsData(allData);
        } catch (Exception e) {
            plugin.getLogger().severe("Не удалось загрузить заклинания игрока " + player.getName());
            e.printStackTrace();
        }
    }

    private File getPlayerFile(Player player) {
        File playersDir = new File(plugin.getDataFolder(), "players");
        if (!playersDir.exists()) {
            playersDir.mkdirs();
        }
        return new File(playersDir, player.getUniqueId() + ".yml");
    }
}