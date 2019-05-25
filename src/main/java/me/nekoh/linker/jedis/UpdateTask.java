package me.nekoh.linker.jedis;

import me.nekoh.core.util.CC;
import me.nekoh.core.util.ConsoleUtil;
import me.nekoh.linker.Linker;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;

public class UpdateTask extends BukkitRunnable {
    @Override
    public void run() {
        try (Jedis jedis = Linker.getInstance().getLinkerJedis().getJedisPool().getResource()) {
            jedis.publish("linker", "post-update");
        } catch (Exception e) {
            ConsoleUtil.printMessage(CC.translate("&cSomething fucked up. Please contact the owner or developer."));
        }
    }
}
