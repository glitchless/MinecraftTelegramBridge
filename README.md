# Telegram Bridge

# Install
1. Get download from [curseforge](https://www.curseforge.com/minecraft/mc-mods/telegram-bridge) and put in mods folder
2. Create your telegram bot in Telegram with [BotFather](t.me/BotFather)
3. Turn off Privacy mode in `Bot Settings->Group Privacy`
4. Add bot in telegram group
5. Add telegram chat id in telegrambridge.cfg (you can use [GetChatBot](t.me/GetChatBot))
6. Change text block in config 
7. Check it!
8. You can use [ServerLocalization](https://www.curseforge.com/minecraft/mc-mods/server-localization) for better support

# Building
Most modules build from the repository root:
```
./gradlew build
```

`bridge-1.20` is a **standalone build** and is deliberately not part of the root build. Forge 1.20.1
requires ForgeGradle 6, Gradle 8 and Java 17, which the legacy modules (ForgeGradle 4.1, and the
1.7.10 fork) cannot run on. Build it from its own directory with a Java 17 toolchain available:
```
cd bridge-1.20 && ./gradlew build
```

# Example
## Telegram
![](https://habrastorage.org/webt/c8/1i/v-/c81iv-lwbnc-d5ar-mn3xrpl6r8.png)
## Minecraft
![](https://habrastorage.org/webt/0b/j2/hz/0bj2hz7dy92bb87yivmhpujiate.png)
