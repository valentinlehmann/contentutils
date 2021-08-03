package de.valentinlehmann.contentutils.utils;

import co.aikar.commands.BukkitLocales;
import co.aikar.commands.MessageKeys;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

@RequiredArgsConstructor
public class LocalizeUtils {
    private final HashMap<String, String> messages = new HashMap<>();
    private final JsonParser jsonParser = new JsonParser();
    private final ContentUtilsPlugin plugin;

    public void loadMessages() throws IOException {
        InputStream inputStream = this.plugin.getResource("messages.json");

        if (inputStream == null) {
            this.plugin.getLogger().severe("Konnte die Message-File nicht laden!");
            return;
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        JsonElement jsonElement = this.jsonParser.parse(inputStreamReader);

        inputStreamReader.close();
        inputStream.close();

        if (!jsonElement.isJsonObject()) {
            return;
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jsonObject.entrySet().forEach(entry -> {
            JsonElement messageElement = entry.getValue();

            if (!messageElement.isJsonPrimitive()) {
                return;
            }

            JsonPrimitive messagePrimitive = messageElement.getAsJsonPrimitive();

            if (!messagePrimitive.isString()) {
                return;
            }

            this.messages.put(entry.getKey(), messagePrimitive.getAsString());
        });

        searchForReplacements();
        overwriteAikarMessages();
    }

    private void searchForReplacements() {
        // Copying HashMap to avoid a ConcurrentModificationException
        HashMap<String, String> messagesCopy = new HashMap<>(this.messages);
        StrSubstitutor strSubstitutor = new StrSubstitutor(this.messages, "%(", ")");

        messagesCopy.forEach((key, message) -> this.messages.replace(key, strSubstitutor.replace(message)));
    }

    private void overwriteAikarMessages() {
        BukkitLocales locales = this.plugin.getCommandManager().getLocales();
        Locale locale = this.plugin.getCommandManager().getLocales().getDefaultLocale();

        locales.addMessage(locale, MessageKeys.PERMISSION_DENIED, getMessage("contentutils.no-perms"));
        locales.addMessage(locale, MessageKeys.PERMISSION_DENIED_PARAMETER, getMessage("contentutils.no-perms"));
        locales.addMessage(locale, MessageKeys.UNKNOWN_COMMAND, getMessage("contentutils.command.not-found"));
        locales.addMessage(locale, MessageKeys.NOT_ALLOWED_ON_CONSOLE, getMessage("contentutils.command.only-players"));
        locales.addMessage(locale, MessageKeys.COULD_NOT_FIND_PLAYER, getMessage("contentutils.command.player-not-found"));
        locales.addMessage(locale, MessageKeys.INVALID_SYNTAX, getMessage("contentutils.command.invalid-syntax"));
        locales.addMessage(locale, MessageKeys.ERROR_PREFIX, getMessage("contentutils.command.error-prefix"));
        locales.addMessage(locale, MessageKeys.ERROR_GENERIC_LOGGED, getMessage("contentutils.command.error"));
        locales.addMessage(locale, MessageKeys.PLEASE_SPECIFY_ONE_OF, getMessage("contentutils.command.please-specify"));
    }

    public String getMessage(String key, Object... replacements) {
        String message;

        if (this.messages.containsKey(key)) {
            message = this.messages.get(key);
        } else {
            message = "Localize: " + key;
        }

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
        }

        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }
}
