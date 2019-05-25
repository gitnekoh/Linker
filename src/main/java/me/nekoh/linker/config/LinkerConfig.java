package me.nekoh.linker.config;

import lombok.Getter;
import me.nekoh.linker.Linker;

@Getter
public class LinkerConfig {

    private final String serverName;

    public LinkerConfig() {
        serverName =  Linker.getInstance().getConfig().getString("server_name");
    }
}
