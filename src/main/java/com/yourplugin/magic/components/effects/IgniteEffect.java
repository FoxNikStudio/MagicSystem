package com.yourplugin.magic.components.effects;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class IgniteEffect implements SpellComponent {

    @Override
    public String getName() {
        return "Поджог";
    }

    @Override
    public String getDescription() {
        return "Поджигает цели на время";
    }

    @Override
    public double getManaCost(Map<String, Object> parameters) {
        int duration = (int) parameters.getOrDefault("duration", 3);
        return duration * 2;
    }

    @Override
    public void execute(Player caster, Map<String, Object> parameters,
                        Vector direction, List<org.bukkit.entity.Entity> targets) {
        int duration = (int) parameters.getOrDefault("duration", 3);

        for (org.bukkit.entity.Entity target : targets) {
            if (target instanceof org.bukkit.entity.LivingEntity) {
                target.setFireTicks(duration * 20); // Конвертируем секунды в тики
            }
        }
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("duration", 3);
        return defaults;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "ignite");
        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Не нужно сохранять состояние для этого компонента
    }
}