package fr.farfy.deryban.ban;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import fr.farfy.deryban.Main;
import fr.farfy.deryban.time.TimeUnit;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
 
public class BanManager {
    @SuppressWarnings("deprecation")
	public void ban(UUID uuid, long endInSeconds, String reason){
        if(isBanned(uuid)) return;
 
        long endToMillis = endInSeconds * 1000;
        long end = endToMillis + System.currentTimeMillis();
 
        if(endInSeconds == -1){
            end = -1;
        }
 
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("INSERT INTO bans (player_uuid, end, reason) VALUES (?, ?, ?)");
            sts.setString(1, uuid.toString());
            sts.setLong(2, end);
            sts.setString(3, reason);
            sts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        if(BungeeCord.getInstance().getPlayer(uuid) != null){
            ProxiedPlayer target = BungeeCord.getInstance().getPlayer(uuid);
            target.disconnect("§c⚠ §6Connexion perdue sur le serveur "+ProxyServer.getInstance().getName()+"  §c⚠ \n\n §cVous avez été banni du serveur\n\n §cRaison: "+getReason(uuid)+"\n\n §6Temps restant: "+getTimeLeft(uuid)+"\n\n §c⬣ §6Pour toute réclamation venez ici https://discord.gg/tXEq3tC §c⬣");
            
            //
         }
    }
 
    public void unban(UUID uuid){
        if(!isBanned(uuid)) return;
 
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("DELETE FROM bans WHERE player_uuid=?");
            sts.setString(1, uuid.toString());
            sts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public boolean isBanned(UUID uuid){
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("SELECT * FROM bans WHERE player_uuid=?");
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 
    public void checkDuration(UUID uuid){
        if(!isBanned(uuid)) return;
 
        if(getEnd(uuid) == -1) return;
 
        if(getEnd(uuid) < System.currentTimeMillis()){
            unban(uuid);
        }
    }
 
    public long getEnd(UUID uuid){
        if(!isBanned(uuid)) return 0;
 
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("SELECT * FROM bans WHERE player_uuid=?");
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();
            if(rs.next()){
                return rs.getLong("end");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
 
    public String getTimeLeft(UUID uuid){
        if(!isBanned(uuid)) return "§cNon banni";
 
        if(getEnd(uuid) == -1){
            return "§cPermanent";
        }
 
        long tempsRestant = (getEnd(uuid) - System.currentTimeMillis()) / 1000;
        int mois = 0;
        int jours = 0;
        int heures = 0;
        int minutes = 0;
        int secondes = 0;
 
        while(tempsRestant >= TimeUnit.MOIS.getToSecond()){
            mois++;
            tempsRestant -= TimeUnit.MOIS.getToSecond();
        }
 
        while(tempsRestant >= TimeUnit.JOUR.getToSecond()){
            jours++;
            tempsRestant -= TimeUnit.JOUR.getToSecond();
        }
 
        while(tempsRestant >= TimeUnit.HEURE.getToSecond()){
            heures++;
            tempsRestant -= TimeUnit.HEURE.getToSecond();
        }
 
        while(tempsRestant >= TimeUnit.MINUTE.getToSecond()){
            minutes++;
            tempsRestant -= TimeUnit.MINUTE.getToSecond();
        }
 
        while(tempsRestant >= TimeUnit.SECONDE.getToSecond()){
            secondes++;
            tempsRestant -= TimeUnit.SECONDE.getToSecond();
        }
 
        // 1 Mois, 1 Jour(s), 12 Heure(s), 32 Minute(s), 12 Seconde(s)
        return mois + " " + TimeUnit.MOIS.getName() + ", " + jours + " " + TimeUnit.JOUR.getName() + ", " + heures + " " + TimeUnit.HEURE.getName() + ", " + minutes + " " + TimeUnit.MINUTE.getName() + ", " + secondes + " " + TimeUnit.SECONDE.getName();
    }
 
    public String getReason(UUID uuid){
        if(!isBanned(uuid)) return "§cNon banni";
 
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("SELECT * FROM bans WHERE player_uuid=?");
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();
            if(rs.next()){
                return rs.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "§cNon banni";
    }
 
}