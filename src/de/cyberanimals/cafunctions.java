package de.cyberanimals;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;

public class cafunctions
{
  private caachievements plugin;
  
  public cafunctions(caachievements plugin)
  {
    this.plugin = plugin;
  }
  
  public void plusCoins(String playername, int coins)
  {
    String qry = "UPDATE `statistics` SET `points`=points+" + coins + " WHERE playername='" + playername + "'";
    
    this.plugin.CASQL.update(qry);
  }
  
  public boolean checkAchievement(String playername, int achievementid)
  {
    String qry = "SELECT * FROM `achievementship` WHERE playername='" + playername + "' AND achievementid='" + achievementid + "'";
    try
    {
      ResultSet rs = this.plugin.CASQL.getResult(qry);
      return rs.next();
    }
    catch (SQLException e)
    {
      System.out.println("Fehler bei der Datenverarbeitung.");
      e.printStackTrace();
    }
    return false;
  }
  
  public String getAchievementDesc(int achievementid)
  {
    String qry = "SELECT `description` FROM `achievements` WHERE id='" + achievementid + "'";
    try
    {
      ResultSet rs = this.plugin.CASQL.getResult(qry);
      if (rs.next()) {
        return rs.getString("description");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return "Error.";
    }
    return "No desc";
  }
  
  public boolean achievementCheck(int achievementid)
  {
    String qry = "SELECT * FROM `achievements` WHERE id='" + achievementid + "'";
    try
    {
      ResultSet rs = this.plugin.CASQL.getResult(qry);
      return rs.next();
    }
    catch (SQLException e)
    {
      System.out.println("Fehler bei der Datenverarbeitung.");
      e.printStackTrace();
    }
    return false;
  }
  
  public void setAchievementShip(String playername, int achievementid)
  {
    String qry = "INSERT INTO `achievementship`(`playername`, `achievementid`) VALUES ('" + playername + "','" + achievementid + "')";
    if (achievementCheck(achievementid))
    {
      this.plugin.CASQL.update(qry);
    }
    else
    {
      System.out.println("Error. Achievement does not exist.");
      return;
    }
  }
  
  public int getCoins(String playername)
  {
    int coins = 0;
    try
    {
      ResultSet rs = this.plugin.CASQL.getResult("SELECT `points` FROM `statistics` WHERE playername='" + playername + "'");
      while (rs.next()) {
        coins = rs.getInt("points");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return -1;
    }
    return coins;
  }
  
}
