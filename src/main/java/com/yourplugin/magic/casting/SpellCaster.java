package com.yourplugin.magic.casting;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import com.yourplugin.magic.core.SpellManager;
import com.yourplugin.magic.core.ManaSystem;
import com.yourplugin.magic.components.SpellComponent;
import com.yourplugin.magic.components.targets.SingleTarget;
import com.yourplugin.magic.components.targets.AreaTarget;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class SpellCaster {
    private final SpellManager spellManager;
    private final ManaSystem manaSystem;
    private final ParticleEffects particleEffects;

    public SpellCaster(SpellManager spellManager, ManaSystem manaSystem) {
        this.spellManager = spellManager;
        this.manaSystem = manaSystem;
        this.particleEffects = new ParticleEffects();
    }

    public boolean castSpell(Player player, String spellName) {
        Map<String, Object> spellData = spellManager.getPlayerSpell(player, spellName);
        if (spellData == null) {
            player.sendMessage("§cЗаклинание не найдено!");
            return false;
        }

        List<Map<String, Object>> componentsData = (List<Map<String, Object>>) spellData.get("components");
        double totalManaCost = calculateTotalManaCost(componentsData);

        if (!manaSystem.consumeMana(player, totalManaCost)) {
            player.sendMessage("§cНедостаточно маны!");
            return false;
        }

        // Визуальные эффекты начала каста
        particleEffects.createCastEffect(player.getLocation());

        // Получение направления взгляда игрока
        Vector direction = player.getEyeLocation().getDirection();

        // Применение всех компонентов заклинания
        for (Map<String, Object> componentData : componentsData) {
            SpellComponent component = spellManager.getComponentFromData(componentData);
            if (component != null) {
                Map<String, Object> parameters = (Map<String, Object>) componentData.get("parameters");

                // Получаем цели в зависимости от типа компонента
                List<org.bukkit.entity.Entity> targets = new ArrayList<>();

                if (component instanceof SingleTarget) {
                    SingleTarget singleTarget = (SingleTarget) component;
                    org.bukkit.entity.Entity target = singleTarget.getTarget(player, parameters);
                    if (target != null) {
                        targets.add(target);
                    }
                } else if (component instanceof AreaTarget) {
                    AreaTarget areaTarget = (AreaTarget) component;
                    targets = areaTarget.getTargets(player, parameters);
                }

                // Применяем эффект к целям
                component.execute(player, parameters, direction, targets);

                // Визуальные эффекты для компонента
                particleEffects.createComponentEffect(player.getLocation(), component, parameters);
            }
        }

        return true;
    }

    private double calculateTotalManaCost(List<Map<String, Object>> componentsData) {
        double totalCost = 0;
        for (Map<String, Object> componentData : componentsData) {
            SpellComponent component = spellManager.getComponentFromData(componentData);
            if (component != null) {
                Map<String, Object> parameters = (Map<String, Object>) componentData.get("parameters");
                totalCost += component.getManaCost(parameters);
            }
        }
        return totalCost;
    }
}