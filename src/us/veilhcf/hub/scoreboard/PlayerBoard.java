package us.veilhcf.hub.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.veilhcf.hub.Main;

import java.util.Collection;
import java.util.Collections;

public class PlayerBoard
{
    public static boolean NAMES_ENABLED;
    public static boolean INVISIBILITYFIX;
    public final BufferedObjective bufferedObjective;
    private final Team members;
    private final Scoreboard scoreboard;
    private final Player player;
    private final Main plugin;
    private boolean sidebarVisible;
    private boolean removed;
    private SidebarProvider defaultProvider;

    public PlayerBoard(final Main plugin, final Player player) {
        this.sidebarVisible = false;
        this.removed = false;
        this.plugin = plugin;
        this.player = player;
        this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.bufferedObjective = new BufferedObjective(this.scoreboard);
        (this.members = this.scoreboard.registerNewTeam("members")).setPrefix(ChatColor.YELLOW.toString());
        player.setScoreboard(this.scoreboard);
    }

    public void remove() {
        this.removed = true;
        if (this.scoreboard != null) {
            synchronized (this.scoreboard) {
                for (final Team team : this.scoreboard.getTeams()) {
                    team.unregister();
                }
                for (final Objective objective : this.scoreboard.getObjectives()) {
                    objective.unregister();
                }
            }
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public boolean isSidebarVisible() {
        return this.sidebarVisible;
    }

    public void setSidebarVisible(final boolean visible) {
        this.sidebarVisible = visible;
        this.bufferedObjective.setDisplaySlot(visible ? DisplaySlot.SIDEBAR : null);
    }

    public void setDefaultSidebar(final SidebarProvider provider) {
        if (provider != null && provider.equals(this.defaultProvider)) {
            return;
        }
        if ((this.defaultProvider = provider) == null) {
            synchronized (this.scoreboard) {
                this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            }
        }
    }

    protected void updateObjective(final long now) {
        synchronized (this.scoreboard) {
            final SidebarProvider provider = this.defaultProvider;
            if (provider == null) {
                this.bufferedObjective.setVisible(false);
            }
            else {
                this.bufferedObjective.setTitle(provider.getTitle());
                this.bufferedObjective.setAllLines(provider.getLines(this.player));
                this.bufferedObjective.flip();
            }
        }
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public SidebarProvider getDefaultProvider() {
        return this.defaultProvider;
    }

    public void setMembers(final Collection<Player> players) {
        if (!PlayerBoard.NAMES_ENABLED || this.isRemoved()) {
            return;
        }
        synchronized (this.scoreboard) {
            for (final Player player : players) {
                this.members.addPlayer(player);
            }
        }
    }

    public void wipe(final String entry) {
        synchronized (this.scoreboard) {
            this.members.removeEntry(entry);
        }
    }

    public void init(final Player player) {
        this.init(Collections.singleton(player));
    }

    public void init(final Collection<? extends Player> players) {
        if (!PlayerBoard.NAMES_ENABLED || this.isRemoved()) {
            return;
        }
        for (final Player player : players) {
            this.wipe(player.getName());
            if (player == this.player) {
                this.setMembers(Collections.singleton(player));
            }
        }
    }

    static {
        PlayerBoard.NAMES_ENABLED = true;
        PlayerBoard.INVISIBILITYFIX = true;
    }
}