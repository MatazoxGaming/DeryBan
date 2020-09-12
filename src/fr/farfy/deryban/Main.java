package fr.farfy.deryban;

import fr.farfy.deryban.ban.BanManager;
import fr.farfy.deryban.cmd.BanCommands;
import fr.farfy.deryban.cmd.CheckCommand;
import fr.farfy.deryban.cmd.UnbanCommand;
import fr.farfy.deryban.event.PlayerInfos;
import fr.farfy.deryban.event.PlayerJoin;
import fr.farfy.deryban.event.PlayerListener;
import fr.farfy.deryban.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

 
public class Main extends Plugin {
	 
    private static Main instance;
    public MySQL mysql = new MySQL();
    public PlayerInfos playerInfos = new PlayerInfos();
    public BanManager banManager = new BanManager();
 
    @Override
    public void onEnable() {
        instance = this;
        mysql.connect("", 3306, "", "", "");
 
        getProxy().getPluginManager().registerListener(this, new PlayerJoin());
        //getProxy().getPluginManager().registerListener(this, new PlayerListener());

 
        getProxy().getPluginManager().registerCommand(this, new BanCommands());
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand());
        getProxy().getPluginManager().registerCommand(this, new CheckCommand());
    }
 
    @Override
    public void onDisable() {
        mysql.disconnect();
    }
 
    /**
     * Récupérer cette class dans les autres
     * @return Main
     */
    public static Main getInstance() {
        return instance;
    }
}