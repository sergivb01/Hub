package us.veilhcf.hub.selector;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import us.veilhcf.hub.Main;
import us.veilhcf.hub.utils.ColorUtils;
import us.veilhcf.hub.utils.ItemStackBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PlayerEvents implements Listener{
    private Main main;

    public PlayerEvents(Main plugin) {
        this.main = plugin;
    }

    private ArrayList<String> usingClock = new ArrayList<>();
    static List<String> phiderLore = new ArrayList<>();
    static List<String> enderpearlLore = new ArrayList<>();

  static {
	  phiderLore.add(ChatColor.translateAlternateColorCodes('&', "&7Right click to toggle visable players on and off."));
	  enderpearlLore.add(ChatColor.translateAlternateColorCodes('&', "&7Right click to surf around the lobby."));
  }

  private static ItemStack perll = ItemStackBuilder.get(Material.ENDER_PEARL, 1, (short)0, "&9Enderbutt", enderpearlLore);
    private static ItemStack phider = ItemStackBuilder.get(Material.WATCH, 1, (short)0, "&bPlayer hider", phiderLore);


    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.hasPermission("base.sign.colour")) {
            String[] lines = event.getLines();
            for (int i = 0; i < lines.length; ++i) {
                if (!player.hasPermission("base.sign.admin") && (event.getLine(i).contains(ChatColor.translateAlternateColorCodes('&', "Sell")) || event.getLine(i).contains("Buy")) || event.getLine(i).contains("[Class]")) {
                    if(player.isOp()){return;}
                    player.sendMessage("§cError: §rThis sign contains text that only an §cADMINISTRATOR §ror higher can use. §7(Sell/Buy/Class)");
                    event.setCancelled(true);
                }
                event.setLine(i, ChatColor.translateAlternateColorCodes('&', lines[i]));
            }
        }
    }


    @EventHandler(priority=EventPriority.HIGHEST)
      public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        for (Item item : this.epItems) {
          if (item.getPassenger().equals(p)) {
            item.eject();
          }
        }

        p.getInventory().clear();
            Location loc = new Location(p.getWorld(), p.getWorld().getSpawnLocation().getX(), p.getWorld().getSpawnLocation().getY(), p.getWorld().getSpawnLocation().getZ(), -90, 0);
        p.teleport(loc);
        p.setWalkSpeed(0.50F);

        p.setHealth(20);
        p.setFoodLevel(20);
        p.setGameMode(GameMode.ADVENTURE);

        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);

          p.sendMessage(new ColorUtils().translateFromString("&8&m------------------------------------"));
          p.sendMessage(new ColorUtils().translateFromString(""));
          p.sendMessage(new ColorUtils().translateFromString("&aHey, &e" + p.getName() + "&a. Welcome to the &eVeilMC Network 3.0."));
          p.sendMessage(new ColorUtils().translateFromString(""));
          p.sendMessage(new ColorUtils().translateFromString("&aServer IP: &eveilmc.net"));
          p.sendMessage(new ColorUtils().translateFromString("&aTwitter: &e@VeilHCF"));
          p.sendMessage(new ColorUtils().translateFromString("&aWebsite: &eveilmc.net"));
          p.sendMessage(new ColorUtils().translateFromString("&aTeamspeak: &ets.veilmc.net"));
          p.sendMessage(new ColorUtils().translateFromString(""));
          p.sendMessage(new ColorUtils().translateFromString("&aTo get started right click the your &eserver selector &ato join a server."));
          p.sendMessage(new ColorUtils().translateFromString(""));
          p.sendMessage(new ColorUtils().translateFromString("&8&m------------------------------------"));


        p.getInventory().setItem(0, perll);
        p.getInventory().setItem(4, ServerSelector.selector);
        //p.getInventory().setItem(7, ServerSelector.hub);
        p.getInventory().setItem(8, phider);
        p.getInventory().setHeldItemSlot(4);
    }

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event){
    event.setCancelled(true);
  }

  @EventHandler
  public void time(ThunderChangeEvent event){
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerDamage(EntityDamageEvent event){
    if ((event.getEntity() instanceof Player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event){
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event){
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void bucketFill(PlayerBucketEmptyEvent event){
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void bucketEmpty(PlayerBucketFillEvent event){
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent e){
    e.setCancelled(true);
  }

  @EventHandler
  public void onPlayerPickItem(PlayerPickupItemEvent e){
    e.setCancelled(true);
    e.getItem().remove();
  }

    @EventHandler
    public void onPlayerToggleFlight(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        event.setCancelled(true);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
        player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.45F, 1.45F);
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE && player.getLocation().subtract(0.0, 1.25, 0.0).getBlock().getType() != Material.AIR && !player.isFlying()) {
            player.setAllowFlight(true);
        }
    }

  @EventHandler
  public void chat(final AsyncPlayerChatEvent e) {
  	  	Player player = e.getPlayer();
  		e.setFormat(ChatColor.translateAlternateColorCodes('&', "&e" + player.getDisplayName() + " &6» &7") + (player.isOp() ? ChatColor.translateAlternateColorCodes('&', e.getMessage()) : e.getMessage()));
  		//p.sendMessage(ChatColor.GOLD + "In order to chat you must purchase a rank at " + ChatColor.RESET + ChatColor.YELLOW + "store.veilhcf.us");
        //e.setCancelled(true);

  }

  @EventHandler
  public void onLeave(PlayerQuitEvent e) {
	  Player p = e.getPlayer();
	//  if(p.hasPermission("lobby.joinmsg")){
	//	  e.setQuitMessage(new ColorUtils().translateFromString("&c&l> &e" + e.getPlayer().getName() + " &ahas disconnected."));
	  //}else{
		  e.setQuitMessage(null);
	  //}
  }


    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                Player p = (Player) e.getEntity();
                Location loc = new Location(p.getWorld(), p.getWorld().getSpawnLocation().getX(), p.getWorld().getSpawnLocation().getY(), p.getWorld().getSpawnLocation().getZ(), -90, 0);
                p.teleport(loc);
                p.sendMessage(ChatColor.GREEN + "You were saved from the void!");
            }
        }
        e.setCancelled(true);
    }


    private void setupEnderpearlRunnable(final Item item) {
        new BukkitRunnable() {
            public void run() {
                if (item.isDead()) {
                    this.cancel();
                }
                if (item.getVelocity().getX() == 0.0 || item.getVelocity().getY() == 0.0 || item.getVelocity().getZ() == 0.0) {
                    final Player player = (Player)item.getPassenger();
                    item.remove();
                    if (player != null) {
                        player.teleport(player.getLocation().add(0.0, 0.6, 0.0));
                    }
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.main, 2L, 2L);
    }

    @EventHandler
    public void onEnderpearl(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            final Player player = event.getPlayer();
            final ItemStack itemStack = player.getItemInHand();
            if (itemStack.getType() == Material.ENDER_PEARL) {
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
                event.setUseInteractedBlock(Event.Result.DENY);
                if ((player.getGameMode() == GameMode.SURVIVAL )|| (player.getGameMode() == GameMode.ADVENTURE)) {
                    final Item item = player.getWorld().dropItem(player.getLocation().add(0.0, 0.5, 0.0), new ItemStack(Material.ENDER_PEARL, 1));
                    item.setPickupDelay(10000);
                    item.setVelocity(player.getLocation().getDirection().normalize().multiply(1.6f));
                    item.setPassenger(player);
                    player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.15f, 1.15f);
                    this.setupEnderpearlRunnable(item);
                    player.updateInventory();
                }
            }
        }
    }


/*
    @EventHandler
    public void onTrhowEnder(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if ((!event.hasItem()) ||
                (!event.getItem().getType().equals(Material.ENDER_PEARL)) || (
                (!action.equals(Action.RIGHT_CLICK_AIR)) && (!action.equals(Action.RIGHT_CLICK_BLOCK)))) {
            return;
        }
        event.setCancelled(true);
        event.setUseItemInHand(Event.Result.DENY);
        event.setUseInteractedBlock(Event.Result.DENY);
        Item ep = player.getWorld().dropItem(player.getLocation().add(0.0D, 0.5D, 0.0D), ItemStackBuilder.get(Material.ENDER_PEARL, 1, (short)0, UUID.randomUUID().toString(), null));
        ep.setPickupDelay(10000);
        ep.setVelocity(player.getLocation().getDirection().normalize().multiply(2.0F));
        ep.setPassenger(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        deleteItemWhenNeeded(ep);
        player.updateInventory();
    }


    private void deleteItemWhenNeeded(final Item item){
        new BukkitRunnable(){
            public void run(){
                if (item.isDead()) {
                    cancel();
                }
                if ((item.getVelocity().getX() == 0.0D) || (item.getVelocity().getY() == 0.0D) || (item.getVelocity().getZ() == 0.0D)){
                    Player p = (Player)item.getPassenger();
                    PlayerEvents.this.epItems.remove(item);
                    item.remove();
                    if (p != null) {
                        p.teleport(p.getLocation().add(0.0D, 0.5D, 0.0D));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(main, 2L, 1L);
    }*/

/*@SuppressWarnings({ "deprecation" })
@EventHandler
  public void onPlayerJoin2(PlayerJoinEvent e)
  {
    ItemStack i = new ItemStack(Material.CLAY_BALL);

    //Removed to fix bug
    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
      if (this.usingClock.contains(p.getName())) {
        p.hidePlayer(e.getPlayer());
      } else {
        p.showPlayer(e.getPlayer());
      }
    }
  }*/

  @SuppressWarnings("deprecation")
@EventHandler
  public void onPlayerInteract(PlayerInteractEvent e){
    Action a = e.getAction();

    ItemStack is = e.getItem();
    if ((a == Action.PHYSICAL) || (is == null) || (is.getType() == Material.AIR)) return;

    if(a.equals(Action.LEFT_CLICK_AIR) || a.equals(Action.LEFT_CLICK_BLOCK)) return;


    if (e.getItem().isSimilar(phider)) {
      if (this.usingClock.contains(e.getPlayer().getName())){
        this.usingClock.remove(e.getPlayer().getName());
        e.getPlayer().sendMessage(new ColorUtils().translateFromString("&8(&eVeil Hub&8) &6Players in lobby have been &eenabled."));
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
          e.getPlayer().showPlayer(p);
        }
      }else{
        this.usingClock.add(e.getPlayer().getName());
        e.getPlayer().sendMessage(new ColorUtils().translateFromString("&8(&eVeil Hub&8) &6Players in lobby have been &edisabled."));
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
          e.getPlayer().hidePlayer(p);
        }
      }
    }

  }

    private Set<Item> epItems = new HashSet<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.KICK_FULL)) {
            event.allow();
        }

    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
	 Player p = e.getPlayer();
        if ((e.getResult() == PlayerLoginEvent.Result.KICK_FULL) && (p.hasPermission("rank.staff"))) {
 	        e.allow();
            p.sendMessage(ChatColor.GOLD + "In this moment server is full but you joined since you have enough permissions to bypass the maxmimum slots.");
        }else if((e.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST )&& (p.hasPermission("rank.staff"))){
            e.allow();
            p.sendMessage(ChatColor.RED + "Bypassed whitelist.");
        }
    }


}
