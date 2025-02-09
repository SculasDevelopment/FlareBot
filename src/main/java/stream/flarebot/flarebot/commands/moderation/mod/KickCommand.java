package stream.flarebot.flarebot.commands.moderation.mod;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import stream.flarebot.flarebot.commands.Command;
import stream.flarebot.flarebot.commands.CommandType;
import stream.flarebot.flarebot.mod.modlog.ModAction;
import stream.flarebot.flarebot.mod.modlog.ModlogHandler;
import stream.flarebot.flarebot.objects.GuildWrapper;
import stream.flarebot.flarebot.util.MessageUtils;
import stream.flarebot.flarebot.util.general.GuildUtils;

import java.util.EnumSet;

public class KickCommand implements Command {

    @Override
    public void onCommand(User sender, GuildWrapper guild, TextChannel channel, Message message, String[] args, Member member) {
        if (args.length >= 1) {
            User user = GuildUtils.getUser(args[0], guild.getGuildId());
            if (user == null) {
                MessageUtils.sendErrorMessage("We cannot find that user! Try their ID if you didn't already.", channel, sender);
                return;
            }
            Member target = guild.getGuild().getMember(user);
            if (target == null) {
                MessageUtils.sendErrorMessage("That user is not on this server therefore cannot be kicked!",
                        channel, sender);
                return;
            }
            String reason = null;
            if (args.length >= 2)
                reason = MessageUtils.getMessage(args, 1);
            ModlogHandler.getInstance().handleAction(guild, channel, sender, user, ModAction.KICK, reason);
        } else {
            MessageUtils.sendUsage(this, channel, sender, args);
        }
    }

    @Override
    public String getCommand() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kicks a user";
    }

    @Override
    public String getUsage() {
        return "`{%}kick <user> [reason]` - Kicks a user with an optional reason.";
    }

    @Override
    public stream.flarebot.flarebot.permissions.Permission getPermission() {
        return stream.flarebot.flarebot.permissions.Permission.KICK_COMMAND;
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }

}
