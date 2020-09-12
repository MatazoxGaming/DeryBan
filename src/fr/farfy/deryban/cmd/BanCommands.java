package fr.farfy.deryban.cmd;

import java.util.UUID;

import fr.farfy.deryban.Main;
import fr.farfy.deryban.time.TimeUnit;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
 
public class BanCommands extends Command {
 
    public BanCommands(){
        super("ban");
    }
 
    @SuppressWarnings("deprecation")
	@Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3){
            helpMessage(sender);
            return;
        }
 
        String targetName = args[0];
 
        if(!Main.getInstance().playerInfos.exist(targetName)){
            sender.sendMessage("§cDeryBan§8>> §cCe joueur ne s'est jamais connecté au serveur !");
            return;
        }
 
        UUID targetUUID = Main.getInstance().playerInfos.getUUID(targetName);
 
        if(Main.getInstance().banManager.isBanned(targetUUID)){
            sender.sendMessage("§cDeryBan§8>> §cCe joueur est déjà banni !");
            return;
        }
 
        String reason = "";
        for(int i = 2; i < args.length; i++){
            reason += args[i] + " ";
        }
 
        if(args[1].equalsIgnoreCase("perm")){
            Main.getInstance().banManager.ban(targetUUID, -1, reason);
            sender.sendMessage("§cDeryBan§8>> §aVous avez banni §6" + targetName + " §c(Permanent) §apour : §e" + reason);
            if(sender.hasPermission("MOD")) {
            ProxyServer.getInstance().broadcast("§cDeryCore§8>> "+targetName+" §4A été banni du serveur "+ProxyServer.getInstance().getName()+"  §4(Permanant) §apour : §e"+reason);
            return;
            }
        }
 
        if(!args[1].contains(":")){
            helpMessage(sender);
            return;
        }
 
        int duration = 0;
        try {
            duration = Integer.parseInt(args[1].split(":")[0]);
        } catch(NumberFormatException e){
            sender.sendMessage("§cDeryBan§8>> §cLa valeur 'durée' doit être un nombre !");
            return;
        }
 
        if(!TimeUnit.existFromShortcut(args[1].split(":")[1])){
            sender.sendMessage("§cDeryBan§8>> §cCette unité de temps n'existe pas !");
            for(TimeUnit units : TimeUnit.values()){
                sender.sendMessage("§b" + units.getName() + " §f: §e" + units.getShortcut());
            }
            return;
        }
 
        TimeUnit unit = TimeUnit.getFromShortcut(args[1].split(":")[1]);
        long banTime = unit.getToSecond() * duration;
 
        Main.getInstance().banManager.ban(targetUUID, banTime, reason);
        sender.sendMessage("§cDeryBan§8>> §aVous avez banni §6" + targetName + " §b(" + duration + " " + unit.getName() + ") §apour : §e" + reason);
        if(sender.hasPermission("MOD")) {
        ProxyServer.getInstance().broadcast("§cDeryCore§8>> "+targetName+" §4A été banni du serveur "+ProxyServer.getInstance().getName()+" §b("+ duration + " "+ unit.getName() + ") §apour : §e" + reason);
        return;
        }
    }
 
    @SuppressWarnings("deprecation")
	public void helpMessage(CommandSender sender){
        sender.sendMessage("§cDeryBan§8>> §c/ban <joueur> perm <raison>");
        sender.sendMessage("§cDeryBan§8>> §c/ban <joueur> <durée>:<unité> <raison>");
    }
}