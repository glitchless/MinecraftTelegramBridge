package ru.glitchless.telegrambridge.core.config;

import java.net.Proxy;
import java.util.Collections;
import java.util.List;


public class TelegramBridgeConfig {
    public static TelegramConfig telegram_config = new TelegramConfig();
    public static RelayLevel relay_level = new RelayLevel();
    public static Text text = new Text();
    public static ProxyConfig proxy = new ProxyConfig();

    @Comment("List of telegram chats that messages will be relayed to")
    public static List<String> chat_ids = Collections.singletonList("-1");

    @Comment("Verbose logging for telegram")
    public static boolean verbose_logging = false;

    public static RelayMode relay_mode = RelayMode.TWO_SIDE;

    public enum RelayMode {
        NONE,
        TO_MINECRAFT,
        TO_TELEGRAM,
        TWO_SIDE
    }

    public static class TelegramConfig {
        @Comment("the bot api token")
        public String api_token = "";

        @Comment("timeout in seconds for long pooling update")
        public int telegram_long_pooling_timeout = 100;
    }

    public static class RelayLevel {
        public boolean user_join = true;
        public boolean user_leave = true;
        public boolean user_kill_by_user = true;
        public boolean user_kill_by_other = true;
        public boolean server_start = true;
        public boolean server_stop = true;
    }

    public static class Text {
        public String death_message = "\\[ ${deathmessage} ]";
        public String server_start = "\\[ Server has started ]";
        public String server_stop = "\\[ Server has stopped ]";
        public String player_join = "\\[ *${nickname}* has connected ]";
        public String player_leave = "\\[ *${nickname}* has disconnected ]";
        public String chatmessage_to_telegram = "*${nickname}:* ${message}";
        public String chatmessage_to_minecraft = "§3TelegramBridge§f / <§b${nickname}§f> ${message}";
        public String notfoundchat = "Chat `${chatid}` is not found in allowed chats. You can add it in `config/telegrambridge.cfg`";
        public String player_empty = "No one is online. Maybe it's time to fix it? :)";
        public String player_list = "*Players online*:${endline}${endline}${playerlist}${endline}Total: *${playercount}*";
    }

    public static class ProxyConfig {
        public Proxy.Type proxy = Proxy.Type.DIRECT;
        public String addr = "";
        public int port = 0;
    }
}
