package com.yourplugin.magic.editor;

import com.yourplugin.magic.core.ManaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.yourplugin.magic.core.SpellManager;
import com.yourplugin.magic.components.SpellComponent;
import java.util.List;
import java.util.Arrays;

public class SpellEditor {
    private final SpellManager spellManager;

    public SpellEditor(SpellManager spellManager, ManaSystem manaSystem) {
        this.spellManager = spellManager;
    }

    public void openEditor(Player player) {
        Inventory editor = Bukkit.createInventory(null, 54, "§5Редактор Заклинаний");

        // Слоты для компонентов заклинания
        for (int i = 0; i < 9; i++) {
            editor.setItem(i + 9, createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
        }

        // Кнопки действий
        editor.setItem(45, createGuiItem(Material.EMERALD, "§aСохранить заклинание"));
        editor.setItem(46, createGuiItem(Material.BARRIER, "§cОтмена"));
        editor.setItem(47, createGuiItem(Material.BOOK, "§eПредпросмотр маны"));

        // Список доступных компонентов
        List<SpellComponent> components = spellManager.getAvailableComponents();
        for (int i = 0; i < components.size() && i < 28; i++) {
            SpellComponent component = components.get(i);
            editor.setItem(i + 18, createComponentItem(component));
        }

        player.openInventory(editor);
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createComponentItem(SpellComponent component) {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§b" + component.getName());
        meta.setLore(Arrays.asList(
                "§7" + component.getDescription(),
                "§6Стоимость: §e" + component.getManaCost(component.getDefaultParameters()),
                "§aЛКМ: §7Добавить компонент",
                "§6ПКМ: §7Настроить параметры"
        ));
        item.setItemMeta(meta);
        return item;
    }
}