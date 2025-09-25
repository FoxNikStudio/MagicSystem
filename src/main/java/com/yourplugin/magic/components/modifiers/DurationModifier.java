package com.yourplugin.magic.components.modifiers;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DurationModifier implements SpellComponent {

    @Override
    public String getName() {
        return "Модификатор длительности";
    }

    @Override
    public String getDescription() {
        return "Увеличивает длительность эффектов заклинания";
    }

    @Override
    public double getManaCost(Map<String, Object> parameters) {
        double multiplier = (double) parameters.getOrDefault("multiplier", 1.5);
        return multiplier * 3;
    }

    @Override
    public void execute(Player caster, Map<String, Object> parameters,
                        Vector direction, List<org.bukkit.entity.Entity> targets) {
        // Модификаторы не выполняются напрямую, они изменяют параметры других компонентов
        // Этот метод пуст, так как модификаторы обрабатываются отдельно
    }

    public Map<String, Object> modifyParameters(Map<String, Object> originalParams) {
        Map<String, Object> modified = new HashMap<>(originalParams);
        double multiplier = (double) modified.getOrDefault("multiplier", 1.5);

        // Увеличиваем длительность эффектов, если они есть
        if (modified.containsKey("duration")) {
            int duration = (int) modified.get("duration");
            modified.put("duration", (int) (duration * multiplier));
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
        data.put("type", "duration_modifier");
        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Не нужно сохранять состояние для этого компонента
    }
}