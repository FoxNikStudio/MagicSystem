package com.yourplugin.magic.editor;

import com.yourplugin.magic.core.ManaSystem;
import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.Map;

public class SpellPreview {
    private final ManaSystem manaSystem;

    public SpellPreview(ManaSystem manaSystem) {
        this.manaSystem = manaSystem;
    }

    public void showManaCost(Player player, List<Map<String, Object>> components) {
        double totalCost = calculateManaCost(components);
        double playerMana = manaSystem.getMana(player);

        String message;
        if (playerMana >= totalCost) {
            message = ChatColor.GREEN + "Стоимость маны: " + ChatColor.YELLOW +
                    totalCost + ChatColor.GREEN + "/" + ChatColor.YELLOW + playerMana;
        } else {
            message = ChatColor.RED + "Стоимость маны: " + ChatColor.YELLOW +
                    totalCost + ChatColor.RED + "/" + ChatColor.YELLOW + playerMana +
                    ChatColor.RED + " (недостаточно маны)";
        }

        // Используем совместимый метод для отправки в action bar
        try {
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(message)
            );
        } catch (Exception e) {
            // Fallback: отправляем в чат, если action bar не работает
            player.sendMessage(message);
        }
    }

    public void updatePreviewInventory(Inventory inventory, List<Map<String, Object>> components) {
        // Очищаем слоты предпросмотра
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i + 9, null);
        }

        // Заполняем предпросмотр компонентами
        for (int i = 0; i < components.size() && i < 9; i++) {
            Map<String, Object> componentData = components.get(i);
            SpellComponent component = parseComponentFromData(componentData);

            if (component != null) {
                Map<String, Object> parameters = (Map<String, Object>) componentData.get("parameters");
                ItemStack item = createComponentPreviewItem(component, parameters);
                inventory.setItem(i + 9, item);
            }
        }
    }

    private double calculateManaCost(List<Map<String, Object>> components) {
        double totalCost = 0;

        for (Map<String, Object> componentData : components) {
            SpellComponent component = parseComponentFromData(componentData);
            if (component != null) {
                Map<String, Object> parameters = (Map<String, Object>) componentData.get("parameters");
                totalCost += component.getManaCost(parameters);
            }
        }

        return totalCost;
    }

    private SpellComponent parseComponentFromData(Map<String, Object> data) {
        // Здесь должна быть логика получения компонента по данным
        // Это упрощенная версия
        return null;
    }

    private ItemStack createComponentPreviewItem(SpellComponent component, Map<String, Object> parameters) {
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + component.getName());

        // Создаем описание с параметрами
        java.util.List<String> lore = new java.util.ArrayList<>();
        lore.add(ChatColor.GRAY + "Параметры:");

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            lore.add(ChatColor.DARK_GRAY + "- " + ChatColor.GRAY +
                    entry.getKey() + ": " + ChatColor.WHITE + entry.getValue());
        }

        lore.add(ChatColor.GOLD + "Стоимость маны: " + ChatColor.YELLOW +
                component.getManaCost(parameters));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}