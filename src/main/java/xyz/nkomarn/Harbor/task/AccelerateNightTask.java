package xyz.nkomarn.Harbor.task;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import xyz.nkomarn.Harbor.HarborLegacy;
import xyz.nkomarn.Harbor.util.Messages;

public class AccelerateNightTask implements Runnable {
    private final World world;
    private int taskId = 0;

    public AccelerateNightTask(final World world) {
        this.world = world;
        Messages.sendWorldChatMessage(world, "§eRapidly approaching daytime.");

        Bukkit.getScheduler().scheduleSyncDelayedTask(HarborLegacy.getHarbor(), () -> {
            world.setStorm(false);
            world.setThundering(false);
        });
    }

    public void setTaskId(final int id) {
        this.taskId = id;
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int dayTime = 1200;
        double timeRate = 70;

        if (time >= (dayTime - timeRate * 1.5) && time <= dayTime) {
            Checker.skippingWorlds.remove(world);
            Messages.sendRandomSkippedMessage(world);
            Bukkit.getScheduler().cancelTask(this.taskId);
        } else {
            world.setTime(time + (int) timeRate);
        }
    }
}
