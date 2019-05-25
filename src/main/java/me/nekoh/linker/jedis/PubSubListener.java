package me.nekoh.linker.jedis;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import me.nekoh.linker.Linker;
import me.nekoh.linker.info.ServerInfo;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@RequiredArgsConstructor
public class PubSubListener extends JedisPubSub implements Runnable {

    private final JedisPool jedisPool;
    private final Gson gson = new Gson();

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println(message);
        if (message.equalsIgnoreCase("GET")) {
            ServerInfo serverInfo = ServerInfo.getServerList().computeIfAbsent(Linker.getInstance().getLinkerConfig().getServerName(), s ->
                    new ServerInfo(s, Bukkit.getOnlinePlayers().size())
            );
            serverInfo.setPlayerAmount(Bukkit.getOnlinePlayers().size());
            jedisPool.getResource().publish("server-info", gson.toJson(serverInfo));
            jedisPool.getResource().close();
        } else {
            ServerInfo serverInfo = gson.fromJson(message, ServerInfo.class);
            ServerInfo.getServerList().putIfAbsent(serverInfo.getName(), serverInfo);
            ServerInfo.getServerList().get(serverInfo.getName()).setPlayerAmount(serverInfo.getPlayerAmount());
        }
    }

    @Override
    public void run() {
        jedisPool.getResource().psubscribe(this, "server-info");
        jedisPool.getResource().close();
    }
}
