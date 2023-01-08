package net.threadly.core.testplugin;

import net.threadly.core.command.CmdParam;
import net.threadly.core.command.Command;
import net.threadly.core.text.Text;
import net.threadly.core.text.TextBuilder;
import net.threadly.core.text.spigot.SpigotText;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {

    @Command(
            usage = "testcmd hewoo <player>",
            params = @Command.CommandParam(key = "player", type = Player.class, conversor = "name-to-player"),
            playerOnly = true
    )
    public static void testCommand(CommandSender sender, @CmdParam("player") Player player) {
        sender.sendMessage("This command worked. Wow");
        sender.sendMessage("The player name: " + player.getName());
        new TextBuilder().fromConfig("test", Main.getInstance()).build(Text.class).send(player);
        new TextBuilder().fromConfig("test", Main.getInstance()).build(SpigotText.class).send(player);
    }

}
