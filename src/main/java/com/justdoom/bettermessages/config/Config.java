package com.justdoom.bettermessages.config;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.message.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

    public static boolean DISABLE_OUTDATED_CONFIG_WARNING;

    public static int CONFIG_VERSION;

    public static List<Message> MESSAGES = new ArrayList<>();

    public static void init() {
        BetterMessages.getInstance().reloadConfig();

        MESSAGES.clear();

        DISABLE_OUTDATED_CONFIG_WARNING = getConfig().getBoolean("disable-outdated-config-warning");
        CONFIG_VERSION = getConfig().getInt("config-version");

        for(String msg : getConfig().getConfigurationSection("messages").getKeys(false)) {
            Message message = new Message();
            message.setMessage(getConfig().getString("messages." + msg + ".message"));
            message.setEnabled(getConfig().getBoolean("messages." + msg + ".enabled"));
            message.setPermission(getConfig().getString("messages." + msg + ".permission"));
            message.setActivation(getConfig().getStringList("messages." + msg + ".activation"));
            message.setAudience(getConfig().getString("messages." + msg + ".audience"));
            message.setParent(msg);

            if (getConfig().isInt("messages." + msg + ".count")) {
                message.setCount(getConfig().getInt("messages." + msg + ".count") == -1
                        ? Arrays.asList(-1)
                        : Arrays.asList(getConfig().getInt("messages." + msg + ".count")));
            } else {
                message.setCount(getConfig().getIntegerList("messages." + msg + ".count"));
            }

            MESSAGES.add(message);
        }
    }

    private static FileConfiguration getConfig() {
        return BetterMessages.getInstance().getConfig();
    }
}
