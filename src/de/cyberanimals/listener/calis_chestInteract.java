package de.cyberanimals.listener;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cyberanimals.caachievements;

public class calis_chestInteract
  implements Listener
{
	
  private caachievements plugin;
  public calis_chestInteract(caachievements plugin)
  {
    this.plugin = plugin;
  }
  
  public String createAchievementSkull(String world, int x, int y, int z)
  {
    String qry = "INSERT INTO `coinsigns`(`world`,`x`, `y`, `z`) VALUES ('"+world+"','" + x + "','" + y + "','" + z + "')";
    this.plugin.CASQL.update(qry);
    
    String id = "";
    try
    {
      ResultSet rs = this.plugin.CASQL.getResult("SELECT `id` FROM `coinsigns` WHERE x='" + x + "' AND y='" + y + "' AND z='" + z + "'");
      while (rs.next()) {
        id = rs.getString("id");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    String url = "https://mgmt.cyberanimals.de/achievement/create.php?id=" + id;
    return url;
  }
  
  @EventHandler
  public void onBuild(BlockPlaceEvent e)
  {
    if (((e.getBlock().getState() instanceof Skull)) && (e.getPlayer().hasPermission("blumenetwork.build")))
    {
      Skull sk = (Skull)e.getBlock().getState();
      if (sk.getOwner().equalsIgnoreCase("MHF_Chest"))
      {
        if (!e.getPlayer().hasPermission("blumenetwork.buildachievement"))
        {
          e.setCancelled(true);
          e.getPlayer().sendMessage(this.plugin.msgperm);
          return;
        }
        int x = sk.getLocation().getBlockX();
        int y = sk.getLocation().getBlockY();
        int z = sk.getLocation().getBlockZ();
        String world = sk.getLocation().getWorld().getName();
        
        String link = createAchievementSkull(world, x, y, z);
        
        e.getPlayer().sendMessage(this.plugin.pfx + "Um das Achievement einrichten zu können, klicke auf diesen Link: §c" + link);
      }
    }
  }
  
  @EventHandler
  public void onInteract(PlayerInteractEvent e)
  {
    if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && 
      ((e.getClickedBlock().getState() instanceof Skull)))
    {
      Skull sk = (Skull)e.getClickedBlock().getState();
      if (sk.getOwner().equals("MHF_Chest"))
      {
        Location loc = sk.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        String world = loc.getWorld().getName();
        
        String qry = "SELECT `coins`, `achievementid`, `permanent` FROM `coinsigns` WHERE world='"+world+"' AND x='" + x + "' AND y='" + y + "' AND z='" + z + "'";
        
        int coins = 0;
        int achievementid = 0;
        try
        {
          ResultSet rs = this.plugin.CASQL.getResult(qry);
          while (rs.next())
          {
            coins = rs.getInt("coins");
            achievementid = rs.getInt("achievementid");
          }
        }
        catch (SQLException err)
        {
          err.printStackTrace();
        }
        
        if(achievementid < 1) {
        	e.getPlayer().sendMessage(this.plugin.epfx+"Fehler. Das Achievement wurde noch nicht eingerichtet!");
        	return;
        }
        
        if (!this.plugin.functions.achievementCheck(achievementid))
        {
          e.getPlayer().sendMessage(this.plugin.epfx + "Fehler. Dieses Achievement existiert nicht.");
          return;
        }
        if (this.plugin.functions.checkAchievement(e.getPlayer().getName(), achievementid))
        {
          e.getPlayer().sendMessage(this.plugin.pfx + "Du hast dieses Achievement bereits.");
          return;
        }
        String desc = this.plugin.functions.getAchievementDesc(achievementid);
        
        this.plugin.functions.setAchievementShip(e.getPlayer().getName(), achievementid);
        this.plugin.functions.plusCoins(e.getPlayer().getName(), coins);
        
        e.getPlayer().sendMessage(this.plugin.pfx + "Du hast das Achievement §b" + desc + " §6erhalten!");
        e.getPlayer().sendMessage(this.plugin.pfx + "Für das Achievement bekommst du §c" + coins + " §6Coins!");
      }
    }
  }
}
