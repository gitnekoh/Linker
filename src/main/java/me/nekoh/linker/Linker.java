package me.nekoh.linker;

import lombok.Getter;
import me.nekoh.core.util.ConsoleUtil;
import me.nekoh.linker.command.LinkerCommand;
import me.nekoh.linker.config.LinkerConfig;
import me.nekoh.linker.jedis.LinkerJedis;
import me.nekoh.linker.jedis.UpdateTask;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Linker extends JavaPlugin {

    @Getter
    private static Linker instance;
    private LinkerJedis linkerJedis;
    private LinkerConfig linkerConfig;

    @Override
    public void onEnable() {
        instance = this;
        instance.saveDefaultConfig();
        instance.getCommand("sync").setExecutor(new LinkerCommand());

        this.linkerJedis = new LinkerJedis();
        this.linkerConfig = new LinkerConfig();
        this.linkerJedis = new LinkerJedis();
        new UpdateTask().runTaskTimerAsynchronously(this, 20 * 1, 0);
        ConsoleUtil.printMessage("================================================================");
        ConsoleUtil.printMessage("&aLoaded Linker.");
        ConsoleUtil.printMessage("================================================================");
    }

    @Override
    public void onDisable() {
        this.linkerJedis.getJedisPool().destroy();
        ConsoleUtil.printMessage("================================================================");
        ConsoleUtil.printMessage("&aUnloaded Linker.");
        ConsoleUtil.printMessage("================================================================");
    }
}
