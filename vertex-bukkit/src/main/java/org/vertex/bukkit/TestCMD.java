package org.vertex.bukkit;

import org.bukkit.command.CommandSender;
import org.vertex.core.command.CmdParam;
import org.vertex.core.command.Command;

import java.util.Optional;

public class TestCMD {

    @Command(
            playerOnly = true,
            usage = "fodase <oi> <ta>",
            params = {
                    @Command.CommandParam(key = "oi", type = String.class, conversor = "string-to-string"),
                    @Command.CommandParam(key = "ta", type = String.class, conversor = "string-to-string")
            }
    )
    public static void test(CommandSender sender, @CmdParam("oi") String oi, @CmdParam("ta") String ta) {
        sender.sendMessage(oi + ta);
    }

}
