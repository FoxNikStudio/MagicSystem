package com.yourplugin.magic.core;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import java.util.*;

public class SpellManager {
    private final MagicPlugin plugin;
    private final Map<UUID, Map<String, List<Map<String, Object>>>> playerSpells = new HashMap<>();
    private final List<SpellComponent> availableComponents = new ArrayList<>();

    public SpellManager(MagicPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerComponent(SpellComponent component) {
        availableComponents.add(component);
    }

    public List<SpellComponent> getAvailableComponents() {
        return new ArrayList<>(availableComponents);
    }

    public SpellComponent getComponentByName(String name) {
        for (SpellComponent component : availableComponents) {
            if (component.getName().equalsIgnoreCase(name)) {
                return component;
            }
        }
        return null;
    }

    public SpellComponent getComponentFromData(Map<String, Object> data) {
        String type = (String) data.get("type");
        for (SpellComponent component : availableComponents) {
            if (component.getClass().getSimpleName().toLowerCase().contains(type.toLowerCase())) {
                return component;
            }
        }
        return null;
    }

    public void savePlayerSpell(Player player, String spellName, List<Map<String, Object>> components) {
        UUID playerId = player.getUniqueId();
        if (!playerSpells.containsKey(playerId)) {
            playerSpells.put(playerId, new HashMap<>());
        }

        playerSpells.get(playerId).put(spellName.toLowerCase(), components);
        plugin.getSpellStorage().savePlayerSpells(player);
    }

    public Map<String, Object> getPlayerSpell(Player player, String spellName) {
        UUID playerId = player.getUniqueId();
        if (!playerSpells.containsKey(playerId)) {
            return null;
        }

        List<Map<String, Object>> components = playerSpells.get(playerId).get(spellName.toLowerCase());
        if (components == null) {
            return null;
        }

        Map<String, Object> spellData = new HashMap<>();
        spellData.put("name", spellName);
        spellData.put("components", components);
        return spellData;
    }

    public List<String> getPlayerSpellNames(Player player) {
        UUID playerId = player.getUniqueId();
        if (!playerSpells.containsKey(playerId)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(playerSpells.get(playerId).keySet());
    }

    public void loadPlayerData(Player player) {
        plugin.getSpellStorage().loadPlayerSpells(player);
    }

    // Для внутреннего использования persistence
    public Map<UUID, Map<String, List<Map<String, Object>>>> getPlayerSpellsData() {
        return playerSpells;
    }

    public void setPlayerSpellsData(Map<UUID, Map<String, List<Map<String, Object>>>> data) {
        this.playerSpells.putAll(data);
    }
}