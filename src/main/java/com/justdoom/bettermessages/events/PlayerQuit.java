package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.manager.PlayerManager;
import com.justdoom.bettermessages.message.Message;
import com.justdoom.bettermessages.util.MessageUtil;
import com.justdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void QuitEvent(org.bukkit.event.player.PlayerQuitEvent event) {

        Player player = event.getPlayer();

        PlayerManager.removePlayer(player.getUniqueId());

        for (Message msg : Config.MESSAGES) {

            if (!msg.getActivation().contains("quit") || !msg.isEnabled()) break;

            event.setQuitMessage(null);

            if (VanishUtil.isVanished(player)
                    || player.hasPermission("bettermessages.silent-quit")) break;

            if (!msg.getPermission().equals("none") && !player.hasPermission(msg.getPermission())) break;

            if (!msg.getCount().contains(BetterMessages.getInstance().getSqlite().getCount(player.getUniqueId())) && !msg.getCount().contains(-1)) {
                break;
            }

            String message = MessageUtil.translate(msg.getMessage(), player);

            switch (msg.getAudience()) {
                case "server":
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(message);
                    }
                    break;
                case "world":
                    for (Player p : player.getWorld().getPlayers()) {
                        p.sendMessage(message);
                    }
                    break;
                case "user":
                    player.sendMessage(message);
                    break;
                default:
                    if (!msg.getAudience().startsWith("world/")) {
                        break;
                    }

                    for (Player p : Bukkit.getWorld(msg.getAudience().replace("world/", "")).getPlayers()) {
                        p.sendMessage(message);
                    }
            }
        }
    }
}

