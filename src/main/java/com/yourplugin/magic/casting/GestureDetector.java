package com.yourplugin.magic.casting;

import com.yourplugin.magic.core.MagicPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

public class GestureDetector implements Listener {
    private final MagicPlugin plugin;
    private final Map<UUID, List<Long>> playerClicks = new HashMap<>();
    private final Map<UUID, Long> lastSlotChange = new HashMap<>();

    public GestureDetector(MagicPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Проверяем, держит ли игрок волшебный предмет
        if (!isMagicWand(player.getInventory().getItemInMainHand())) {
            return;
        }

        // Регистрируем клики для жестов
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            registerClick(player, false);
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            registerClick(player, true);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());

        // Проверяем, является ли предмет волшебной палочкой
        if (isMagicWand(newItem)) {
            long currentTime = System.currentTimeMillis();
            lastSlotChange.put(player.getUniqueId(), currentTime);
        }
    }

    private void registerClick(Player player, boolean isRightClick) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Инициализируем список кликов, если нужно
        if (!playerClicks.containsKey(playerId)) {
            playerClicks.put(playerId, new ArrayList<>());
        }

        List<Long> clicks = playerClicks.get(playerId);

        // Удаляем старые клики (старше 3 секунд)
        clicks.removeIf(time -> currentTime - time > 3000);

        // Добавляем текущий клик
        clicks.add(currentTime);

        // Проверяем жесты
        checkForGestures(player, clicks, isRightClick);
    }

    private void checkForGestures(Player player, List<Long> clicks, boolean isRightClick) {
        int clickCount = clicks.size();

        // Быстрое троекратное нажатие
        if (clickCount >= 3) {
            long timeDiff = clicks.get(clickCount - 1) - clicks.get(clickCount - 3);
            if (timeDiff < 1000) { // 3 клика менее чем за секунду
                sendActionBar(player, ChatColor.GOLD + "Жест распознан: " + ChatColor.YELLOW + "Быстрое троекратное нажатие");
                // Открываем редактор заклинаний
                plugin.getSpellEditor().openEditor(player);
                playerClicks.get(player.getUniqueId()).clear();
                return;
            }
        }

        // Двойное нажатие
        if (clickCount >= 2) {
            long timeDiff = clicks.get(clickCount - 1) - clicks.get(clickCount - 2);
            if (timeDiff < 500) { // 2 клика менее чем за 0.5 секунды
                if (isRightClick) {
                    sendActionBar(player, ChatColor.GOLD + "Жест распознан: " + ChatColor.YELLOW + "Двойное правое нажатие");
                    // Произносим последнее заклинание
                    List<String> spellNames = plugin.getSpellManager().getPlayerSpellNames(player);
                    if (!spellNames.isEmpty()) {
                        String lastSpell = spellNames.get(spellNames.size() - 1);
                        plugin.getSpellCaster().castSpell(player, lastSpell);
                    }
                } else {
                    sendActionBar(player, ChatColor.GOLD + "Жест распознан: " + ChatColor.YELLOW + "Двойное левое нажатие");
                    // Открываем меню заклинаний
                    player.performCommand("magic");
                }
                playerClicks.get(player.getUniqueId()).clear();
            }
        }
    }

    private void sendActionBar(Player player, String message) {
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

    private boolean isMagicWand(ItemStack item) {
        return item != null && item.hasItemMeta() &&
                item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Волшебная палочка");
    }
}