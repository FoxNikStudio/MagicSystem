package com.yourplugin.magic.components.effects;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class HealEffect implements SpellComponent {

    @Override
    public String getName() {
        return "Лечение";
    }

    @Override
    public String getDescription() {
        return "Восстанавливает здоровье";
    }

    @Override
    public double getManaCost(Map<String, Object> parameters) {
        double healAmount = (double) parameters.getOrDefault("heal", 4.0);
        return healAmount * 3;
    }

    @Override
    public void execute(Player caster, Map<String, Object> parameters,
                        Vector direction, List<org.bukkit.entity.Entity> targets) {
        double healAmount = (double) parameters.getOrDefault("heal", 4.0);

        for (org.bukkit.entity.Entity target : targets) {
            if (target instanceof org.bukkit.entity.LivingEntity) {
                org.bukkit.entity.LivingEntity livingTarget = (org.bukkit.entity.LivingEntity) target;
                double newHealth = Math.min(livingTarget.getHealth() + healAmount, livingTarget.getMaxHealth());
                livingTarget.setHealth(newHealth);
            }
        }
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("heal", 4.0);
        return defaults;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "heal");
        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Не нужно сохранять состояние для этого компонента
    }
}