package xyz.nkomarn.Harbor;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nkomarn.Harbor.listener.BedListener;
import xyz.nkomarn.Harbor.task.Checker;

public class HarborLegacy extends JavaPlugin {
    private static HarborLegacy harbor;

    @Override
    public void onEnable() {
        harbor = this;

        final PluginManager pluginManager = getServer().getPluginManager();

        final BedListener bedListener = new BedListener();
        pluginManager.registerEvent(Event.Type.PLAYER_BED_ENTER, bedListener, Event.Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_BED_LEAVE, bedListener, Event.Priority.Normal, this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this,
                new Checker(), 0L, 2 * 20);
    }

    @Override
    public void onDisable() { }

    public static HarborLegacy getHarbor() {
        return harbor;
    }
}
