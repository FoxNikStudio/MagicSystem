package com.yourplugin.magic.components.targets;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AreaTarget implements SpellComponent {

    @Override
    public String getName() {
        return "Область воздействия";
    }

    @Override
    public String getDescription() {
        return "Воздействует на все цели в области";
    }

    @Override
    public double getManaCost(Map<String, Object> parameters) {
        double radius = (double) parameters.getOrDefault("radius", 5.0);
        return radius * 2;
    }

    @Override
    public void execute(Player caster, Map<String, Object> parameters,
                        Vector direction, List<org.bukkit.entity.Entity> targets) {
        // Этот компонент только фильтрует цели, поэтому здесь пусто
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("radius", 5.0);
        return defaults;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "area_target");
        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Не нужно сохранять состояние для этого компонента
    }

    // Специальный метод для получения целей в области
    public List<org.bukkit.entity.Entity> getTargets(Player caster, Map<String, Object> parameters) {
        double radius = (double) parameters.getOrDefault("radius", 5.0);

        // Получаем все entities в радиусе
        return caster.getNearbyEntities(radius, radius, radius);
    }
}