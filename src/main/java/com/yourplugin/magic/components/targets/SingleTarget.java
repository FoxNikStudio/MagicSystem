package com.yourplugin.magic.components.targets;

import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SingleTarget implements SpellComponent {

    @Override
    public String getName() {
        return "Одиночная цель";
    }

    @Override
    public String getDescription() {
        return "Воздействует на одну цель перед кастером";
    }

    @Override
    public double getManaCost(Map<String, Object> parameters) {
        double range = (double) parameters.getOrDefault("range", 20.0);
        return range * 0.5;
    }

    @Override
    public void execute(Player caster, Map<String, Object> parameters,
                        Vector direction, List<org.bukkit.entity.Entity> targets) {
        // Этот компонент только фильтрует цели, поэтому здесь пусто
        // Реальная логика применения будет в SpellCaster
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("range", 20.0);
        return defaults;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "single_target");
        return data;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Не нужно сохранять состояние для этого компонента
    }

    // Специальный метод для получения цели
    public org.bukkit.entity.Entity getTarget(Player caster, Map<String, Object> parameters) {
        double range = (double) parameters.getOrDefault("range", 20.0);

        // Получаем цель, на которую смотрит игрок
        org.bukkit.util.RayTraceResult rayTrace = caster.getWorld().rayTraceEntities(
                caster.getEyeLocation(),
                caster.getEyeLocation().getDirection(),
                range,
                entity -> entity != caster && entity instanceof org.bukkit.entity.LivingEntity
        );

        return rayTrace != null ? rayTrace.getHitEntity() : null;
    }
}