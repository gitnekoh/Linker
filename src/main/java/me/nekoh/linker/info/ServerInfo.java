package me.nekoh.linker.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class ServerInfo {

    @Getter
    private static HashMap<String, ServerInfo> serverList = new HashMap<>();

    String name;
    int playerAmount;
}
