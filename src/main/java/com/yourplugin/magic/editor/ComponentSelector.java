package com.yourplugin.magic.editor;

import com.yourplugin.magic.core.SpellManager;
import com.yourplugin.magic.components.SpellComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class ComponentSelector {
    private final SpellManager spellManager;

    public ComponentSelector(SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    public void openCategorySelector(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Выбор категории компонентов");

        // Кнопки категорий
        inventory.setItem(10, createCategoryItem(Material.REDSTONE, ChatColor.RED + "Эффекты", "Компоненты воздействия"));
        inventory.setItem(12, createCategoryItem(Material.ENDER_EYE, ChatColor.AQUA + "Цели", "Определение целей заклинания"));
        inventory.setItem(14, createCategoryItem(Material.NETHER_STAR, ChatColor.GOLD + "Модификаторы", "Изменение свойств заклинания"));
        inventory.setItem(16, createCategoryItem(Material.BOOK, ChatColor.LIGHT_PURPLE + "Утилиты", "Вспомогательные компоненты"));

        player.openInventory(inventory);
    }

    public void openComponentSelector(Player player, String category) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "Выбор компонентов: " + category);

        // Получаем компоненты соответствующие категории
        List<SpellComponent> components = getComponentsByCategory(category);

        // Заполняем инвентарь компонентами
        for (int i = 0; i < components.size() && i < 45; i++) {
            SpellComponent component = components.get(i);
            inventory.setItem(i, createComponentItem(component));
        }

        // Кнопка назад
        inventory.setItem(49, createBackItem());

        player.openInventory(inventory);
    }

    public void handleComponentSelection(InventoryClickEvent event, String category) {
        if (event.getCurrentItem() == null) return;

        ItemStack item = event.getCurrentItem();
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String displayName = item.getItemMeta().getDisplayName();
        if (displayName.equals(ChatColor.GREEN + "Назад")) {
            openCategorySelector((Player) event.getWhoClicked());
            return;
        }

        // Извлекаем название компонента из displayName (убираем цветовые коды)
        String componentName = displayName.replace(ChatColor.AQUA.toString(), "");
        SpellComponent component = spellManager.getComponentByName(componentName);

        if (component != null) {
            // Здесь должна быть логика добавления компонента в текущее заклинание
            event.getWhoClicked().sendMessage(ChatColor.GREEN + "Компонент \"" + componentName + "\" добавлен");
            event.getWhoClicked().closeInventory();
        }
    }

    private List<SpellComponent> getComponentsByCategory(String category) {
        // Здесь должна быть логика фильтрации компонентов по категории
        // Это упрощенная версия, возвращающая все компоненты
        return spellManager.getAvailableComponents();
    }

    private ItemStack createCategoryItem(Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + description,
                ChatColor.GREEN + "Нажмите для выбора"
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createComponentItem(SpellComponent component) {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + component.getName());
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + component.getDescription(),
                ChatColor.GOLD + "Стоимость: " + ChatColor.YELLOW + component.getManaCost(component.getDefaultParameters()),
                ChatColor.GREEN + "ЛКМ: " + ChatColor.GRAY + "Добавить компонент"
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createBackItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Назад");
        item.setItemMeta(meta);
        return item;
    }
}