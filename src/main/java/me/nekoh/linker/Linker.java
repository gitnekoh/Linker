package me.nekoh.linker;

import lombok.Getter;
import me.nekoh.core.util.ConsoleUtil;
import me.nekoh.linker.command.LinkerCommand;
import me.nekoh.linker.config.LinkerConfig;
import me.nekoh.linker.jedis.PubSubListener;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public final class Linker extends JavaPlugin {

    @Getter
    private static Linker instance;
    private JedisPool jedisPool;
    private PubSubListener pubSubListener;
    private LinkerConfig linkerConfig;
    private Thread thread;

    @Override
    public void onEnable() {
        instance = this;
        instance.saveDefaultConfig();
        instance.getCommand("sync").setExecutor(new LinkerCommand());

        this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 1000);
        this.pubSubListener = new PubSubListener(jedisPool);
        this.linkerConfig = new LinkerConfig();

        (this.thread = new Thread(this.pubSubListener)).start();

        ConsoleUtil.printMessage("================================================================");
        ConsoleUtil.printMessage("&aLoaded Linker.");
        ConsoleUtil.printMessage("================================================================");
    }

    @Override
    public void onDisable() {
        this.jedisPool.close();
        this.thread.stop();

        ConsoleUtil.printMessage("================================================================");
        ConsoleUtil.printMessage("&aUnloaded Linker.");
        ConsoleUtil.printMessage("================================================================");
    }
}
