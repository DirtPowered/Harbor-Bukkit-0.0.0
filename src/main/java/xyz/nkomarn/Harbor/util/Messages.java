package xyz.nkomarn.Harbor.util;

import org.bukkit.World;
import xyz.nkomarn.Harbor.task.Checker;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Messages {
    public static void sendRandomSkippedMessage(final World world) {
        final List<String> messages = Arrays.asList(
            "§eThe night has been skipped.",
            "§eAhhh, finally morning.",
            "§eArghh, it's so bright outside.",
            "§eRise and shine."
        );
        final int index = new Random().nextInt(Math.max(0, messages.size()));
        sendWorldChatMessage(world, messages.get(index));
    }

    public static void sendWorldChatMessage(final World world, final String message) {
        world.getPlayers().forEach(player -> player.sendMessage(prepareMessage(world, message)));
    }

    private static String prepareMessage(final World world, final String message) {
        return message
                .replace("[sleeping]", String.valueOf(Checker.getSleeping(world).size()))
                .replace("[players]", String.valueOf(Checker.getPlayers(world)))
                .replace("[needed]", String.valueOf(Checker.getSkipAmount(world)))
                .replace("[more]", String.valueOf(Checker.getNeeded(world)));
    }
}
