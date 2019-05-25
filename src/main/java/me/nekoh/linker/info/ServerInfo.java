package me.nekoh.linker.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.nekoh.linker.util.UUIDUtil;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ServerInfo {

    @Getter
    private static HashMap<String, ServerInfo> serverList = new HashMap<>();

    String name;
    List<UUID> players;
    long lastUpdate;
    double tps;
    int coreAmount;
    long freeRam;
    long totalRam;

    public void update() {
        this.players = UUIDUtil.convertToList(Bukkit.getOnlinePlayers());
        this.lastUpdate = System.currentTimeMillis();
        this.tps = (Math.round(MinecraftServer.getServer().recentTps[0] * 10000)) / 10000;
        this.coreAmount = Runtime.getRuntime().availableProcessors();
        this.freeRam = Runtime.getRuntime().freeMemory() / 1024;
        this.totalRam = Runtime.getRuntime().totalMemory() / 1024;
    }
}
