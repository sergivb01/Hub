package us.veilhcf.hub.scoreboard.provider;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.veilhcf.hub.Main;
import us.veilhcf.hub.scoreboard.SidebarEntry;
import us.veilhcf.hub.scoreboard.SidebarProvider;
import us.veilhcf.hub.utils.BukkitUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimerSidebarProvider implements SidebarProvider
{
    private final Main plugin;
    private static final String STRAIGHT_LINE;

	public TimerSidebarProvider(final Main plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public String getTitle() {
		return ChatColor.GREEN + "Veil Network " + ChatColor.YELLOW + "[Hub]";
	}

	@Override
	public List<SidebarEntry> getLines(final Player player) {
		List<SidebarEntry> lines = new ArrayList<>();
		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("EST"));

		lines.add(new SidebarEntry(ChatColor.GREEN + "" + ChatColor.BOLD + "Nickname:"));
		lines.add(new SidebarEntry(ChatColor.YELLOW, player.getDisplayName(), ""));
		lines.add(new SidebarEntry(""));
		lines.add(new SidebarEntry(ChatColor.GREEN + "" + ChatColor.BOLD + "Online:"));
		lines.add(new SidebarEntry(ChatColor.YELLOW + (plugin.getGlobalStatus().get("online").equals("true") ? plugin.getGlobalStatus().get("onlineplayers") : " ")));
		lines.add(new SidebarEntry("", "  ", ""));
		lines.add(new SidebarEntry(ChatColor.GREEN + "" + ChatColor.BOLD + "Server Time:"));
		lines.add(new SidebarEntry(ChatColor.YELLOW + formatter.format(date),ChatColor.GRAY + " [EST]", ""));
		lines.add(new SidebarEntry("", "     ", "  "));
		lines.add(new SidebarEntry(ChatColor.YELLOW + "veilmc.net"));

        if (!lines.isEmpty()) {
			lines.add(0, new SidebarEntry(ChatColor.GRAY, TimerSidebarProvider.STRAIGHT_LINE, TimerSidebarProvider.STRAIGHT_LINE));
			lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY,ChatColor.STRIKETHROUGH + TimerSidebarProvider.STRAIGHT_LINE, TimerSidebarProvider.STRAIGHT_LINE));
		}
		return lines;
	}

	static {
        ThreadLocal.withInitial(() -> new DecimalFormat("##.#"));
        new SidebarEntry(" ", " ", " ");
		STRAIGHT_LINE = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 14);
	}
}
