package xyz.nkomarn.Harbor.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import xyz.nkomarn.Harbor.HarborLegacy;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Checker implements Runnable {
    public static final List<World> skippingWorlds = new ArrayList<>();
    private static final double SKIP_PERCENTAGE = 60;

    @Override
    public void run() {
        Bukkit.getWorlds().stream().filter(this::validateWorld).forEach(this::checkWorld);
    }

    private void checkWorld(final World world) {
        final int sleeping = getSleeping(world).size();
        final int needed = getNeeded(world);

        if (needed == 0 && sleeping > 0) {
            skippingWorlds.add(world);

            final AccelerateNightTask nightTask = new AccelerateNightTask(world);
            nightTask.setTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(HarborLegacy.getHarbor(), nightTask,1, 1));
        }
    }

    private boolean validateWorld(final World world) {
        return !skippingWorlds.contains(world)
            && isNight(world);
    }

    private boolean isNight(final World world) {
        return world.getTime() > 12950 || world.getTime() < 23950;
    }

    public static List<Player> getSleeping(final World world) {
        return world.getPlayers().stream().filter(Player::isSleeping).collect(toList());
    }

    public static int getSkipAmount(final World world) {
        return (int) Math.ceil(getPlayers(world) * (SKIP_PERCENTAGE / 100));
    }

    public static int getPlayers(final World world) {
        return Math.max(0, world.getPlayers().size() - getExcluded(world).size());
    }

    public static int getNeeded(final World world) {
        return Math.max(0, (int) Math.ceil((getPlayers(world))
                * (SKIP_PERCENTAGE / 100)
                - getSleeping(world).size()));
    }

    private static List<Player> getExcluded(final World world) {
        return world.getPlayers().stream().filter(player -> player.hasPermission("harbor.ignored")).collect(toList());
    }
}
