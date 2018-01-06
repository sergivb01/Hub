package us.veilhcf.hub.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.veilhcf.hub.Main;
import us.veilhcf.hub.scoreboard.provider.TimerSidebarProvider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ScoreboardHandler implements Listener, Runnable
{
    private final Map<UUID, PlayerBoard> playerBoards;
    private final TimerSidebarProvider timerSidebarProvider;
    private final Main plugin;
    private boolean running;
    private Thread scoreboardThread;
    private final long TICK_IN_NANOS;
    private final long NANO;

    public ScoreboardHandler(final Main plugin) {
        this.running = true;
        this.TICK_IN_NANOS = TimeUnit.MILLISECONDS.toNanos(100L);
        this.NANO = TimeUnit.MILLISECONDS.toNanos(1L);
        this.playerBoards = new HashMap<>();
        this.plugin = plugin;
        this.timerSidebarProvider = new TimerSidebarProvider(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final PlayerBoard playerBoard = new PlayerBoard(plugin, player);
            playerBoard.init(Bukkit.getOnlinePlayers());
            this.setPlayerBoard(player.getUniqueId(), playerBoard);
        }
        (this.scoreboardThread = new Thread(this)).start();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        synchronized (this.playerBoards) {
            for (final PlayerBoard playerBoard : this.playerBoards.values()) {
                playerBoard.init(player);
            }
        }
        final PlayerBoard playerBoard2 = new PlayerBoard(this.plugin, player);
        playerBoard2.init(Bukkit.getOnlinePlayers());
        this.setPlayerBoard(uuid, playerBoard2);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        synchronized (this.playerBoards) {
            this.playerBoards.remove(event.getPlayer().getUniqueId()).remove();
        }
    }

    public PlayerBoard getPlayerBoard(final UUID uuid) {
        synchronized (this.playerBoards) {
            return this.playerBoards.get(uuid);
        }
    }

    public void setPlayerBoard(final UUID uuid, final PlayerBoard board) {
        synchronized (this.playerBoards) {
            this.playerBoards.put(uuid, board);
        }
        board.setSidebarVisible(true);
        board.setDefaultSidebar(this.timerSidebarProvider);
    }

    public void tick() {
        final long now = System.currentTimeMillis();
        synchronized (this.playerBoards) {
            for (final PlayerBoard board : this.playerBoards.values()) {
                if (board.getPlayer().isOnline() && !board.isRemoved()) {
                    try {
                        board.updateObjective(now);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }

    public void disable() {
        this.running = false;
        try {
            this.scoreboardThread.join();
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        this.clearBoards();
    }

    public void run() {
        long start = System.nanoTime();
        while (this.plugin.isEnabled() && this.running) {
            this.tick();
            final long finish = System.nanoTime();
            final long diff = finish - start;
            if (diff < this.TICK_IN_NANOS) {
                final long sleep = (this.TICK_IN_NANOS - diff) / this.NANO;
                if (sleep > 0L) {
                    try {
                        Thread.sleep(sleep);
                    }
                    catch (InterruptedException exception) {
                        exception.printStackTrace();
                        break;
                    }
                }
            }
            start = System.nanoTime();
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public void clearBoards() {
        synchronized (this.playerBoards) {
            final Iterator<PlayerBoard> iterator = this.playerBoards.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().remove();
                iterator.remove();
            }
        }
    }

    public Map<UUID, PlayerBoard> getPlayerBoards() {
        return this.playerBoards;
    }
}