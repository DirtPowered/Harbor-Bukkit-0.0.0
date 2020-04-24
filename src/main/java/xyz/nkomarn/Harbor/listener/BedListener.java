package xyz.nkomarn.Harbor.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerListener;
import xyz.nkomarn.Harbor.HarborLegacy;
import xyz.nkomarn.Harbor.task.Checker;
import xyz.nkomarn.Harbor.util.Messages;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BedListener extends PlayerListener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
        if (Checker.skippingWorlds.contains(event.getPlayer().getWorld())) return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(HarborLegacy.getHarbor(), () -> {
            final UUID playerUuid = event.getPlayer().getUniqueId();
            if (!(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getCooldown(playerUuid)) > 5)) return;

            Messages.sendWorldChatMessage(event.getBed().getWorld(), String.format(
                    "§e%s is now sleeping ([sleeping]/[needed], [more] more needed to skip).",
                    event.getPlayer().getName())
            );
            cooldowns.put(playerUuid, System.currentTimeMillis());
        }, 1);
    }

    @Override
    public void onPlayerBedLeave(final PlayerBedLeaveEvent event) {
        if (Checker.skippingWorlds.contains(event.getPlayer().getWorld())) return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(HarborLegacy.getHarbor(), () -> {
            final UUID playerUuid = event.getPlayer().getUniqueId();
            if (!(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getCooldown(playerUuid)) > 5)) return;

            Messages.sendWorldChatMessage(event.getBed().getWorld(), String.format(
                    "§e%s got out of bed ([sleeping]/[needed], [more] more needed to skip).",
                    event.getPlayer().getName())
            );
            cooldowns.put(playerUuid, System.currentTimeMillis());
        }, 1);
    }

    private long getCooldown(final UUID playerUuid) {
        if (!cooldowns.containsKey(playerUuid)) return 0;
        return cooldowns.get(playerUuid);
    }
}
