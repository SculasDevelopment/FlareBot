package stream.flarebot.flarebot.commands.general;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.commands.Command;
import stream.flarebot.flarebot.commands.CommandType;
import stream.flarebot.flarebot.objects.GuildWrapper;
import stream.flarebot.flarebot.permissions.Permission;
import stream.flarebot.flarebot.util.MessageUtils;
import stream.flarebot.flarebot.util.general.GeneralUtils;

public class CommandUsageCommand implements Command {

    @Override
    public void onCommand(User sender, GuildWrapper guild, TextChannel channel, Message message, String[] args, Member member) {
        if (args.length == 0) {
            MessageUtils.sendUsage(this, channel, sender, args);
        } else {
            Command c = FlareBot.getCommandManager().getCommand(args[0], sender);
            if (c == null || (c.getType().isInternal() && !GeneralUtils.canRunInternalCommand(c, sender)))
                MessageUtils.sendErrorMessage("That is not a command!", channel);
            else
                MessageUtils.sendUsage(c, channel, sender, new String[]{});
        }
    }

    @Override
    public String getCommand() {
        return "usage";
    }

    @Override
    public String getDescription() {
        return "Allows you to view usages for other commands";
    }

    @Override
    public String getUsage() {
        return "`{%}usage <command_name>` - Displays the usage for another command.";
    }

    @Override
    public Permission getPermission() {
        return Permission.USAGE_COMMAND;
    }

    @Override
    public CommandType getType() {
        return CommandType.GENERAL;
    }
}
