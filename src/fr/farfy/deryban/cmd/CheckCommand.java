package fr.farfy.deryban.cmd;

import java.util.UUID;

import fr.farfy.deryban.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
 
public class CheckCommand extends Command {
 
    public CheckCommand(){
        super("check");
    }
 
    @SuppressWarnings("deprecation")
	@Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 1){
            sender.sendMessage("§cDeryBan§8>> §c/check <joueur>");
            return;
        }
 
        String targetName = args[0];
 
        if(!Main.getInstance().playerInfos.exist(targetName)){
            sender.sendMessage("§cDeryBan§8>> §cCe joueur ne s'est jamais connecté au serveur !");
            return;
        }
 
        UUID targetUUID = Main.getInstance().playerInfos.getUUID(targetName);
 
        Main.getInstance().banManager.checkDuration(targetUUID);
 
        sender.sendMessage("§7-----------------------------------------------------");
        sender.sendMessage("§ePseudo : §b" + args[0]);
        sender.sendMessage("§eUUID : §b" + targetUUID.toString());
        sender.sendMessage("§eBanni : " + (Main.getInstance().banManager.isBanned(targetUUID) ? "§a✔" : "§c✖"));
 
        if(Main.getInstance().banManager.isBanned(targetUUID)){
            sender.sendMessage("");
            sender.sendMessage("§6Raison : §c" + Main.getInstance().banManager.getReason(targetUUID));
            sender.sendMessage("§6Temps restant : §f" + Main.getInstance().banManager.getTimeLeft(targetUUID));
        }
 
        sender.sendMessage("§7-----------------------------------------------------");
        return;
    }
}