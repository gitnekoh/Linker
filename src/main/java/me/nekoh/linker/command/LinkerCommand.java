package me.nekoh.linker.command;

import me.nekoh.core.util.CC;
import me.nekoh.linker.info.ServerInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class LinkerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("sync")) {
            return false;
        }
        int x = 0;
        sender.sendMessage(CC.translate("&7Linked servers list:"));
        System.out.println(ServerInfo.getServerList().values());
        for (ServerInfo serverInfo : ServerInfo.getServerList().values()) {
            boolean offline = false;
            if (System.currentTimeMillis() - serverInfo.getLastUpdate() > 5000) {
                offline = true;
            }
            if (offline) {
                sender.sendMessage(CC.translate(
                        "&9&lID: &7" + x++ +
                                " &9&l&lName: &7" + serverInfo.getName() + " &7(&cOFFLINE&7)&9&l."
                ));
            }
            sender.sendMessage(CC.translate(
                    "&9&lID: &7" + x++ +
                            " &9&lName: &7" + serverInfo.getName() +
                            " &9&lPlayers: &7" + serverInfo.getPlayers().size() +
                            " &9&lTPS: &7" + (serverInfo.getTps() > 18 ? "&a" : "&c") + serverInfo.getTps() +
                    " &9&lCPU core amount: &7" + serverInfo.getCoreAmount() +
                    " &9&lUsed RAM (MB): &7" + ((serverInfo.getTotalRam() - serverInfo.getFreeRam()) / serverInfo.getTotalRam() < 0.6 ? "&a" : "&c") + (serverInfo.getTotalRam() - serverInfo.getFreeRam()) / serverInfo.getTotalRam() / 1024
            ));
        }
        return false;
    }
}
