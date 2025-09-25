package com.yourplugin.magic.components.effects;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DamageEffect implements SpellComponent {

    @Override
    public String getName() {
        return "Урон";
    }

    @Override
    public String getDescription() {
        return "Наносит урон целям";
    }

    @Override
    public double getManaCost(Map<String, Object> parameters) {
        double damage = (double) parameters.getOrDefault("damage", 2.0);
        return damage * 5;
    }

    @Override
    public void execute(Player caster, Map<String, Object> parameters,
                        Vector direction, List<org.bukkit.entity.Entity> targets) {
        double damage = (double) parameters.getOrDefault("damage", 2.0);

        for (org.bukkit.entity.Entity target : targets) {
            if (target instanceof org.bukkit.entity.LivingEntity) {
                org.bukkit.entity.LivingEntity livingTarget = (org.bukkit.entity.LivingEntity) target;
                livingTarget.damage(damage, caster);
            }
        }
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("damage", 2.0);
        return defaults;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "damage");
        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Не нужно сохранять состояние для этого компонента
    }
}