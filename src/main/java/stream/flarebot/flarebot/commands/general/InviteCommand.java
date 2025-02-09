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

public class InviteCommand implements Command {

    @Override
    public void onCommand(User sender, GuildWrapper guild, TextChannel channel, Message message, String[] args, Member member) {
        MessageUtils.sendPM(channel, sender, "You can invite me to your server using the link below!\n"
                + FlareBot.getInvite());
    }

    @Override
    public String getCommand() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Get my invite link!";
    }

    @Override
    public String getUsage() {
        return "`{%}invite` - Gets FlareBot's invite link.";
    }

    @Override
    public Permission getPermission() {
        return Permission.INVITE_COMMAND;
    }

    @Override
    public CommandType getType() {
        return CommandType.GENERAL;
    }
}
