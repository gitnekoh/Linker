package me.nekoh.linker.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UUIDUtil {

    public static List<UUID> convertToList(Collection<? extends Player> collection) {
        List<UUID> toReturn = new ArrayList<>();
        collection.forEach(player -> toReturn.add(player.getUniqueId()));
        return toReturn;
    }
}
