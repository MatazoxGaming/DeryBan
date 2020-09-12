package fr.farfy.deryban.cmd;

import java.util.UUID;

import fr.farfy.deryban.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {

    public UnbanCommand(){
        super("unban");
    }

    @SuppressWarnings("deprecation")
	@Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 1){
            sender.sendMessage("§cDeryBan§8>> §c/unban <joueur>");
            return;
        }

        String targetName = args[0];

        if(!Main.getInstance().playerInfos.exist(targetName)){
            sender.sendMessage("§cDeryBan§8>> §cCe joueur ne s'est jamais connecté au serveur !");
            return;
        }

        UUID targetUUID = Main.getInstance().playerInfos.getUUID(targetName);

        if(!Main.getInstance().banManager.isBanned(targetUUID)){
            sender.sendMessage("§cDeryBan§8>> §cCe joueur n'est pas banni !");
            return;
        }

        Main.getInstance().banManager.unban(targetUUID);
        sender.sendMessage("§cDeryBan§8>> §aVous avez débanni §6" + targetName);
        ProxyServer.getInstance().broadcast("§cDeryCore§8>> "+targetName+" &4A été débanni sur le serveur "+ProxyServer.getInstance().getName());

        return;
    }
}