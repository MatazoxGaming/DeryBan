package fr.farfy.deryban.event;

import fr.farfy.deryban.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
 
public class PlayerJoin implements Listener {
 
    @EventHandler
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();
        Main.getInstance().playerInfos.update(player);
    }
 
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onLogin(LoginEvent e){
        Main.getInstance().banManager.checkDuration(e.getConnection().getUniqueId());
 
        if(Main.getInstance().banManager.isBanned(e.getConnection().getUniqueId())){
            e.setCancelled(true);
            e.setCancelReason("§c⚠ §6Connexion perdue sur le serveur Faction §c⚠ \n\n §cVous avez été banni du serveur\n\n §cRaison: "+Main.getInstance().banManager.getReason(e.getConnection().getUniqueId())+"\n\n §6Temps restant: "+Main.getInstance().banManager.getTimeLeft(e.getConnection().getUniqueId())+"\n\n §c⬣ §6Pour toute réclamation venez ici https://discord.gg/tXEq3tC §c⬣");
        }
    }
 
}