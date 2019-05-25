package me.nekoh.linker.command;

import me.nekoh.core.util.CC;
import me.nekoh.linker.Linker;
import me.nekoh.linker.info.ServerInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import redis.clients.jedis.JedisPool;


public class LinkerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("sync")) {
            return false;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Linker.getInstance(), () -> {
            JedisPool jedisPool = Linker.getInstance().getJedisPool();
            jedisPool.getResource().publish("server-info", "GET");
            Bukkit.getScheduler().runTask(Linker.getInstance(), () -> {
                sender.sendMessage(CC.translate("&aLinked servers list:"));
                int x = 0;
                System.out.println(ServerInfo.getServerList().size());
                for (ServerInfo serverInfo : ServerInfo.getServerList().values()) {
                    sender.sendMessage(CC.translate("&eid: " + x++ + ", name: " + serverInfo.getName() + ", players: " + serverInfo.getPlayerAmount()));
                }
            });
        });

        return false;
    }
}
