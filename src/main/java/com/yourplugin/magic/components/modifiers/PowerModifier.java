package com.yourplugin.magic.components.modifiers;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PowerModifier implements SpellComponent {

    @Override
    public String getName() {
        return "Модификатор силы";
    }

    @Override
    public String getDescription() {
        return "Увеличивает силу эффектов заклинания";
    }

    @Override
    public double getManaCost(Map<String, Object> parameters) {
        double multiplier = (double) parameters.getOrDefault("multiplier", 1.5);
        return multiplier * 4;
    }

    @Override
    public void execute(Player caster, Map<String, Object> parameters,
                        Vector direction, List<org.bukkit.entity.Entity> targets) {
        // Модификаторы не выполняются напрямую
    }

    public Map<String, Object> modifyParameters(Map<String, Object> originalParams) {
        Map<String, Object> modified = new HashMap<>(originalParams);
        double multiplier = (double) modified.getOrDefault("multiplier", 1.5);

        // Увеличиваем силу эффектов, если они есть
        if (modified.containsKey("damage")) {
            double damage = (double) modified.get("damage");
            modified.put("damage", damage * multiplier);
        }

        if (modified.containsKey("heal")) {
            double heal = (double) modified.get("heal");
            modified.put("heal", heal * multiplier);
        }

        if (modified.containsKey("radius")) {
            double radius = (double) modified.get("radius");
            modified.put("radius", radius * multiplier);
        }

        return modified;
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("multiplier", 1.5);
        return defaults;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "power_modifier");
        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Не нужно сохранять состояние для этого компонента
    }
}