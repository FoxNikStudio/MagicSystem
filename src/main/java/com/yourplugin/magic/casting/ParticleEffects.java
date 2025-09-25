package com.yourplugin.magic.casting;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.bukkit.Color;
import com.yourplugin.magic.components.SpellComponent;
import java.util.Map;

public class ParticleEffects {

    public void createCastEffect(Location location) {
        // Сфера из частиц вокруг кастующего
        for (int i = 0; i < 50; i++) {
            double angle = 2 * Math.PI * i / 50;
            double x = Math.cos(angle) * 1.5;
            double z = Math.sin(angle) * 1.5;

            Location particleLoc = location.clone().add(x, 1, z);
            location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,
                    particleLoc, 1, 0, 0, 0, 0);
        }
    }

    public void createComponentEffect(Location location, SpellComponent component, Map<String, Object> parameters) {
        String componentType = component.getClass().getSimpleName();

        if (componentType.contains("Damage")) {
            // Эффект для урона - красные частицы
            createExplosionEffect(location);
        } else if (componentType.contains("Heal")) {
            // Эффект для лечения - зеленые частицы
            createSphereEffect(location);
        } else if (componentType.contains("Ignite")) {
            // Эффект для поджога - оранжевые частицы
            createFireEffect(location);
        }
    }

    private void createExplosionEffect(Location center) {
        for (int i = 0; i < 30; i++) {
            Vector direction = new Vector(
                    Math.random() - 0.5,
                    Math.random() - 0.5,
                    Math.random() - 0.5
            ).normalize().multiply(Math.random() * 2);

            Location particleLoc = center.clone().add(direction);

            // Используем частицы лавы для красного эффекта
            center.getWorld().spawnParticle(
                    Particle.LAVA,
                    particleLoc,
                    1, // Количество частиц
                    0, 0, 0, // Смещение
                    0 // Дополнительные параметры
            );

            // Добавляем немного дыма для большего эффекта
            center.getWorld().spawnParticle(
                    Particle.CLOUD,
                    particleLoc,
                    1,
                    0, 0, 0,
                    0
            );
        }
    }

    private void createSphereEffect(Location center) {
        // Создаем DustOptions с зеленым цветом
        Particle.DustOptions greenDust = new Particle.DustOptions(Color.GREEN, 1.0f);

        for (int i = 0; i < 50; i++) {
            double u = Math.random();
            double v = Math.random();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);

            double x = 2.0 * Math.sin(phi) * Math.cos(theta);
            double y = 2.0 * Math.sin(phi) * Math.sin(theta);
            double z = 2.0 * Math.cos(phi);

            Location particleLoc = center.clone().add(x, y, z);

            // Используем частицы эффекта для зеленого цвета (замена VILLAGER_HAPPY)
            center.getWorld().spawnParticle(
                    Particle.DUST,
                    particleLoc,
                    1,
                    0, 0, 0,
                    0,
                    greenDust // Зеленый цвет
            );

            // Добавляем дополнительные частицы для большего эффекта
            center.getWorld().spawnParticle(
                    Particle.END_ROD,
                    particleLoc,
                    1,
                    0, 0, 0,
                    0.1
            );
        }
    }

    private void createFireEffect(Location center) {
        for (int i = 0; i < 20; i++) {
            Vector direction = new Vector(
                    Math.random() - 0.5,
                    Math.random() * 0.5,
                    Math.random() - 0.5
            ).normalize().multiply(Math.random() * 1.5);

            Location particleLoc = center.clone().add(direction);

            // Используем огненные частицы
            center.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1, 0, 0, 0, 0);

            // Добавляем дым для большего эффекта
            center.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 1, 0, 0, 0, 0);
        }
    }
}