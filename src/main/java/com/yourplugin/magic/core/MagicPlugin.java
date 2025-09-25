package com.yourplugin.magic.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.yourplugin.magic.editor.SpellEditor;
import com.yourplugin.magic.casting.SpellCaster;
import com.yourplugin.magic.casting.GestureDetector;
import com.yourplugin.magic.persistence.SpellStorage;
import com.yourplugin.magic.persistence.PlayerData;

public class MagicPlugin extends JavaPlugin {
    private SpellManager spellManager;
    private ManaSystem manaSystem;
    private SpellEditor spellEditor;
    private SpellCaster spellCaster;
    private SpellStorage spellStorage;
    private PlayerData playerData;

    @Override
    public void onEnable() {
        // Инициализация систем
        this.spellManager = new SpellManager(this);
        this.manaSystem = new ManaSystem(this);
        this.spellStorage = new SpellStorage(this);
        this.playerData = new PlayerData();
        this.spellEditor = new SpellEditor(spellManager, manaSystem);
        this.spellCaster = new SpellCaster(spellManager, manaSystem);

        // Регистрация компонентов
        registerComponents();

        // Регистрация событий
        getServer().getPluginManager().registerEvents(new GestureDetector(this), this);

        // Загрузка данных игроков онлайн
        for (Player player : getServer().getOnlinePlayers()) {
            spellManager.loadPlayerData(player);
        }

        getLogger().info("MagicSystem включен!");
    }

    private void registerComponents() {
        // Эффекты
        spellManager.registerComponent(new com.yourplugin.magic.components.effects.DamageEffect());
        spellManager.registerComponent(new com.yourplugin.magic.components.effects.HealEffect());
        spellManager.registerComponent(new com.yourplugin.magic.components.effects.IgniteEffect());

        // Цели
        spellManager.registerComponent(new com.yourplugin.magic.components.targets.SingleTarget());
        spellManager.registerComponent(new com.yourplugin.magic.components.targets.AreaTarget());

        // Модификаторы
        spellManager.registerComponent(new com.yourplugin.magic.components.modifiers.DurationModifier());
        spellManager.registerComponent(new com.yourplugin.magic.components.modifiers.PowerModifier());
    }

    @Override
    public void onDisable() {
        // Сохранение данных всех онлайн-игроков
        for (Player player : getServer().getOnlinePlayers()) {
            spellStorage.savePlayerSpells(player);
        }

        getLogger().info("MagicSystem выключен!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("magic")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("edit")) {
                spellEditor.openEditor(player);
                return true;
            }

            player.sendMessage("§5=== Система Магии ===");
            player.sendMessage("§d/magic edit §7- Открыть редактор заклинаний");
            player.sendMessage("§d/magic list §7- Список ваших заклинаний");
            player.sendMessage("§d/cast <name> §7- Произнести заклинание");
            return true;
        }

        if (command.getName().equalsIgnoreCase("edited")) {
            spellEditor.openEditor(player);
            return true;
        }

        if (command.getName().equalsIgnoreCase("cast") && args.length > 0) {
            spellCaster.castSpell(player, args[0]);
            return true;
        }

        return false;
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }

    public ManaSystem getManaSystem() {
        return manaSystem;
    }

    public SpellStorage getSpellStorage() {
        return spellStorage;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public SpellEditor getSpellEditor() {
        return spellEditor;
    }

    public SpellCaster getSpellCaster() {
        return spellCaster;
    }
}