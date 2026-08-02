package dev.unnm3d.redischat.chat;

import dev.unnm3d.redischat.Permissions;
import dev.unnm3d.redischat.RedisChat;
import org.bukkit.ChatColor;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;

public final class ChatColorGUI {
    private ChatColorGUI() {
    }

    public static Gui create(RedisChat plugin) {
        Gui gui = Gui.of(plugin.guiSettings.getChatColorGUIStructure());
        StringBuilder sb = new StringBuilder();
        plugin.guiSettings.chatColorGUIStructure.forEach(row -> sb.append(row.replace(" ", "")));

        for (int slot = 0; slot < sb.length(); slot++) {
            char slotChar = sb.charAt(slot);
            ChatColor color = ChatColor.getByChar(slotChar);
            if (color == null) continue;
            final int finalSlot = slot;
            gui.setItem(finalSlot, Item.builder()
                    .setItemProvider(player -> new ItemBuilder(gui.getItem(finalSlot).getItemProvider(player).get()))
                    .addClickHandler((item, click) -> {
                        click.player().closeInventory();
                        applyColor(plugin, click.player().getName(), color, click.player().hasPermission(Permissions.CHAT_COLOR.getPermission() + "." + color.name().toLowerCase()), click.player());
                    })
                    .build());
        }
        return gui;
    }

    private static void applyColor(RedisChat plugin, String playerName, ChatColor color, boolean hasPermission, org.bukkit.entity.Player player) {
        if (color == ChatColor.RESET) {
            plugin.getPlaceholderManager().removePlayerPlaceholder(playerName, "chat_color");
        } else if (hasPermission) {
            plugin.getPlaceholderManager().addPlayerPlaceholder(playerName, "chat_color", "<" + color.name().toLowerCase() + ">");
        } else {
            plugin.messages.sendMessage(player, plugin.messages.noPermission);
            return;
        }
        plugin.messages.sendMessage(player, plugin.messages.color_set);
    }
}
