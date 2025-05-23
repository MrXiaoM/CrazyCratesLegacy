package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.FileManager.Files;
import com.badbones69.crazycrates.api.enums.settings.Messages;
import com.badbones69.crazycrates.api.objects.CrateLocation;
import com.badbones69.crazycrates.commands.subs.CrateBaseCommand;
import com.badbones69.crazycrates.commands.subs.player.BaseKeyCommand;
import com.badbones69.crazycrates.cratetypes.*;
import com.badbones69.crazycrates.library.dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import com.badbones69.crazycrates.library.dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import com.badbones69.crazycrates.library.dev.triumphteam.cmd.core.message.MessageKey;
import com.badbones69.crazycrates.library.dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import com.badbones69.crazycrates.listeners.*;
import com.badbones69.crazycrates.support.AdventureUtil;
import com.badbones69.crazycrates.support.libraries.PluginSupport;
import com.badbones69.crazycrates.support.placeholders.PlaceholderAPISupport;
import com.google.common.collect.Lists;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrazyCrates extends JavaPlugin implements Listener {

    private static CrazyCrates plugin;

    private Starter starter;

    BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);

    @Override
    public void onLoad() {
        MinecraftVersion.replaceLogger(getLogger());
        MinecraftVersion.disableUpdateCheck();
        MinecraftVersion.disableBStats();
        MinecraftVersion.getVersion();
    }

    @Override
    public void onEnable() {
        AdventureUtil.init();
        plugin = this;

        starter = new Starter();

        starter.run();

        starter.getFileManager().setLog(true)
                .registerDefaultGenerateFiles("CrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("CosmicCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuickCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("classic.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("nether.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("outdoors.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("sea.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("soul.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("wooden.nbt", "/schematics", "/schematics")
                .registerCustomFilesFolder("/crates")
                .registerCustomFilesFolder("/schematics")
                .setup();

        // Clean files if we have to.
        cleanFiles();

        // Add extra messages.
        Messages.addMissingMessages();

        FileConfiguration config = Files.CONFIG.getFile();

        boolean metricsEnabled = config.getBoolean("Settings.Toggle-Metrics");

        String updater = config.getString("Settings.Update-Checker");
        String version = config.getString("Settings.Config-Version");

        String menu = config.getString("Settings.Enable-Crate-Menu");

        String full = config.getString("Settings.Give-Virtual-Keys-When-Inventory-Full-Message");

        String phys = config.getString("Settings.Physical-Accepts-Physical-Keys");

        if (phys == null) {
            config.set("Settings.Physical-Accepts-Physical-Keys", true);

            Files.CONFIG.saveFile();
        }

        if (full == null) {
            config.set("Settings.Give-Virtual-Keys-When-Inventory-Full-Message", false);

            Files.CONFIG.saveFile();
        }

        if (version == null) {
            config.set("Settings.Config-Version", 1);

            Files.CONFIG.saveFile();
        }

        if (menu == null) {
            String oldBoolean = config.getString("Settings.Disable-Crate-Menu");
            boolean switchBoolean = config.getBoolean("Settings.Disable-Crate-Menu");

            if (oldBoolean != null) {
                config.set("Settings.Enable-Crate-Menu", switchBoolean);
                config.set("Settings.Disable-Crate-Menu", null);
            } else {
                config.set("Settings.Enable-Crate-Menu", true);
            }

            Files.CONFIG.saveFile();
        }

        if (updater == null) {
            config.set("Settings.Update-Checker", true);

            Files.CONFIG.saveFile();
        }

        int configVersion = 1;
        if (configVersion != config.getInt("Settings.Config-Version") && version != null) {
            plugin.getLogger().warning("========================================================================");
            plugin.getLogger().warning("You have an outdated config, Please run the command /crates update!");
            plugin.getLogger().warning("This will take a backup of your entire folder & update your configs.");
            plugin.getLogger().warning("Default values will be used in place of missing options!");
            plugin.getLogger().warning("If you have any issues, Please contact Discord Support.");
            plugin.getLogger().warning("https://discord.gg/crazycrew");
            plugin.getLogger().warning("========================================================================");
        }

        enable();
    }

    @Override
    public void onDisable() {
        QuickCrate.removeAllRewards();

        if (starter.getCrazyManager().getHologramController() != null)
            starter.getCrazyManager().getHologramController().removeAllHolograms();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        starter.getCrazyManager().setNewPlayerKeys(e.getPlayer());
        starter.getCrazyManager().loadOfflinePlayersKeys(e.getPlayer());
    }

    public void cleanFiles() {
        if (!Files.LOCATIONS.getFile().contains("Locations")) {
            Files.LOCATIONS.getFile().set("Locations.Clear", null);
            Files.LOCATIONS.saveFile();
        }

        if (!Files.DATA.getFile().contains("Players")) {
            Files.DATA.getFile().set("Players.Clear", null);
            Files.DATA.saveFile();
        }
    }

    private void enable() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new MenuListener(), this);
        pluginManager.registerEvents(new PreviewListener(), this);
        pluginManager.registerEvents(new FireworkDamageListener(), this);
        pluginManager.registerEvents(new CrateControlListener(), this);
        pluginManager.registerEvents(new MiscListener(), this);

        pluginManager.registerEvents(new War(), this);
        pluginManager.registerEvents(new CSGO(), this);
        pluginManager.registerEvents(new Wheel(), this);
        pluginManager.registerEvents(new Wonder(), this);
        pluginManager.registerEvents(new Cosmic(), this);
        pluginManager.registerEvents(new Roulette(), this);
        pluginManager.registerEvents(new QuickCrate(), this);
        pluginManager.registerEvents(new CrateOnTheGo(), this);

        pluginManager.registerEvents(this, this);

        if (PluginSupport.ITEMS_ADDER.isPluginEnabled()) {
            getServer().getPluginManager().registerEvents(new ItemsAdderListener(), this);
        } else {
            starter.getCrazyManager().loadCrates();
        }

        if (!starter.getCrazyManager().getBrokeCrateLocations().isEmpty())
            pluginManager.registerEvents(new BrokeLocationsListener(), this);

        if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) new PlaceholderAPISupport().register();

        manager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> sender.sendMessage(Messages.UNKNOWN_COMMAND.getMessage()));

        manager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, context) -> {
            String command = context.getCommand();
            String subCommand = context.getSubCommand();

            String commandOrder = "/" + command + " " + subCommand + " ";

            String correctUsage = null;

            switch (command) {
                case "crates":
                    correctUsage = getString(subCommand, commandOrder);
                    break;
                case "keys":
                    if (subCommand.equals("view")) correctUsage = "/keys " + subCommand;
                    break;
            }

            if (correctUsage != null)
                sender.sendMessage(Messages.CORRECT_USAGE.getMessage().replace("%usage%", correctUsage));
        });

        manager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> {
            String command = context.getCommand();
            String subCommand = context.getSubCommand();

            String commandOrder = "/" + command + " " + subCommand + " ";

            String correctUsage = null;

            switch (command) {
                case "crates":
                    correctUsage = getString(subCommand, commandOrder);
                    break;
                case "keys":
                    if (subCommand.equals("view")) correctUsage = "/keys " + subCommand + " <player-name>";
                    break;
            }

            if (correctUsage != null)
                sender.sendMessage(Messages.CORRECT_USAGE.getMessage().replace("%usage%", correctUsage));
        });

        manager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> sender.sendMessage(Messages.NOT_ONLINE.getMessage().replace("%player%", context.getTypedArgument())));

        manager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) -> sender.sendMessage(Messages.NO_PERMISSION.getMessage()));

        manager.registerMessage(BukkitMessageKey.PLAYER_ONLY, (sender, context) -> sender.sendMessage(Messages.MUST_BE_A_PLAYER.getMessage()));

        manager.registerMessage(BukkitMessageKey.CONSOLE_ONLY, (sender, context) -> sender.sendMessage(Messages.MUST_BE_A_CONSOLE_SENDER.getMessage()));

        manager.registerSuggestion(SuggestionKey.of("crates"), (sender, context) -> starter.getFileManager().getAllCratesNames(plugin).stream().collect(Collectors.toList()));

        manager.registerSuggestion(SuggestionKey.of("key-types"), (sender, context) -> KEYS);

        manager.registerSuggestion(SuggestionKey.of("online-players"), (sender, context) -> getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));

        manager.registerSuggestion(SuggestionKey.of("locations"), (sender, context) -> starter.getCrazyManager().getCrateLocations().stream().map(CrateLocation::getID).collect(Collectors.toList()));

        manager.registerSuggestion(SuggestionKey.of("prizes"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            starter.getCrazyManager().getCrateFromName(context.getArgs().get(0)).getPrizes().forEach(prize -> numbers.add(prize.getName()));

            return numbers;
        });

        manager.registerSuggestion(SuggestionKey.of("numbers"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            for (int i = 1; i <= 250; i++) numbers.add(i + "");

            return numbers;
        });

        manager.registerArgument(CrateBaseCommand.CustomPlayer.class, (sender, context) -> {
            return new CrateBaseCommand.CustomPlayer(context);
        });

        manager.registerCommand(new BaseKeyCommand());
        manager.registerCommand(new CrateBaseCommand());

        printHooks();
    }

    private String getString(String subCommand, String commandOrder) {
        String correctUsage = null;

        switch (subCommand) {
            case "transfer":
                correctUsage = commandOrder + "<crate-name> " + "<player-name> " + "<amount>";
                break;
            case "debug":
            case "open":
            case "set":
                correctUsage = commandOrder + "<crate-name>";
                break;
            case "tp":
                correctUsage = commandOrder + "<id>";
            case "additem":
                correctUsage = commandOrder + "<crate-name> " + "<prize-number>";
                break;
            case "preview":
            case "open-others":
            case "forceopen":
                correctUsage = commandOrder + "<crate-name> " + "<player-name>";
                break;
            case "mass-open":
                correctUsage = commandOrder + "<crate-name> " + "<amount>";
                break;
            case "give-random":
                correctUsage = commandOrder + "<key-type> " + "<amount> " + "<player-name>";
                break;
            case "give":
            case "take":
                correctUsage = commandOrder + "<key-type> " + "<crate-name> " + "<amount> " + "<player-name>";
                break;
            case "giveall":
                correctUsage = commandOrder + "<key-type> " + "<crate-name> " + "<amount>";
                break;
        }

        return correctUsage;
    }

    private final List<String> KEYS = Lists.newArrayList("virtual", "v", "physical", "p");

    public static CrazyCrates getPlugin() {
        return plugin;
    }

    public void printHooks() {
        for (PluginSupport value : PluginSupport.values()) {
            if (value.isPluginEnabled()) {
                plugin.getLogger().info(Methods.color("&6&l" + value.name() + " &a&lFOUND"));
            } else {
                plugin.getLogger().info(Methods.color("&6&l" + value.name() + " &c&lNOT FOUND"));
            }
        }
    }

    public Starter getStarter() {
        return starter;
    }
}