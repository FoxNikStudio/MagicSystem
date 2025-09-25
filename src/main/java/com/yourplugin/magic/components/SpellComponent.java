package com.yourplugin.magic.components;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;

public interface SpellComponent {
    String getName();
    String getDescription();
    double getManaCost(Map<String, Object> parameters);
    void execute(Player caster, Map<String, Object> parameters,
                 Vector direction, List<org.bukkit.entity.Entity> targets);
    Map<String, Object> getDefaultParameters();
    // Для сериализации/десериализации
    Map<String, Object> serialize();
    void deserialize(Map<String, Object> data);
}