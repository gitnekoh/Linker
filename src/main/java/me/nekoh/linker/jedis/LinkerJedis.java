package me.nekoh.linker.jedis;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.nekoh.core.util.CC;
import me.nekoh.core.util.ConsoleUtil;
import me.nekoh.linker.Linker;
import me.nekoh.linker.info.ServerInfo;
import me.nekoh.linker.util.UUIDUtil;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Getter
@RequiredArgsConstructor
public class LinkerJedis {

    private final JedisPool jedisPool;
    private Jedis jedis;
    private final Gson gson = new Gson();

    public LinkerJedis() {
        this.jedisPool = new JedisPool();
        new BukkitRunnable() {
            @Override
            public void run() {
                jedis = jedisPool.getResource();
                jedis.subscribe(new LinkerSubscriber(), "linker");
                jedis.close();
            }
        }.runTaskAsynchronously(Linker.getInstance());
    }

    private class LinkerSubscriber extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (message.equalsIgnoreCase("post-update")) {
                        ServerInfo serverInfo = ServerInfo.getServerList().computeIfAbsent(Linker.getInstance().getLinkerConfig().getServerName(), s ->
                                new ServerInfo(s,
                                        UUIDUtil.convertToList(Bukkit.getOnlinePlayers()),
                                        System.currentTimeMillis(),
                                        (MinecraftServer.getServer().recentTps[0] * 10000) / 10000,
                                        Runtime.getRuntime().availableProcessors(),
                                        Runtime.getRuntime().freeMemory() / 1024,
                                        Runtime.getRuntime().totalMemory() / 1024)
                        );
                        serverInfo.update();
                        try (Jedis jedis = jedisPool.getResource()) {
                            jedis.publish("linker", gson.toJson(serverInfo));
                        } catch (Exception e) {
                            ConsoleUtil.printMessage(CC.translate("&cSomething fucked up. Please contact the owner or developer."));
                        }

                    } else {
                        ServerInfo serverInfo = gson.fromJson(message, ServerInfo.class);
                        ServerInfo.getServerList().put(serverInfo.getName(), serverInfo);
                    }
                }
            }.runTaskAsynchronously(Linker.getInstance());
        }
    }
}
