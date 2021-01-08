package de.cyberanimals;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.cyberanimals.core.CACore;
import de.cyberanimals.core.CAMySQL;
import de.cyberanimals.listener.calis_chestInteract;

public class caachievements extends JavaPlugin{
	
	public String pfx;
	public String cpfx;
	public String epfx;
	public String msgperm;
	public String msgplayer;
	public CAMySQL CASQL;
	public cafunctions functions = new cafunctions(this);
	
	public void onEnable() {
		System.out.println("[CyberAnimals] loading Plugin...");
		
	    this.CASQL = de.cyberanimals.core.CACore.getMySQL();
	    if(CASQL == null) {
	    	System.out.println("Sorry. Plugin [CYBERCORE] not available. Shutting down.");
	    	Bukkit.getPluginManager().disablePlugin(this);
	    } else {
	    	System.out.println("Hooked into CyberCore. Plugin loaded. MySQL available.");
	    }    
	    
		this.pfx = CACore.getPFX();
		this.cpfx = CACore.getCPFX();
		this.epfx = CACore.getEPFX();
		this.msgperm = CACore.getMSGPERM();
		this.msgplayer = CACore.getMSGPLAYER();
	   
	    
	    Bukkit.getPluginManager().registerEvents(new calis_chestInteract(this), this);
	    
	    
		System.out.println(cpfx+getDescription().getName()+" Plugin loaded.");
	}
	
	public void onDisable() {
		System.out.println(cpfx+"Plugin is shutting down...");
		
		System.out.println(cpfx+"Plugin is deactivated now!");
	}

}
