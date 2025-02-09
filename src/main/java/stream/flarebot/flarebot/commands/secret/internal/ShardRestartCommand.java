package stream.flarebot.flarebot.commands.secret.internal;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.Getters;
import stream.flarebot.flarebot.commands.CommandType;
import stream.flarebot.flarebot.commands.InternalCommand;
import stream.flarebot.flarebot.objects.GuildWrapper;
import stream.flarebot.flarebot.permissions.PerGuildPermissions;
import stream.flarebot.flarebot.util.MessageUtils;

public class ShardRestartCommand implements InternalCommand {

    @Override
    public void onCommand(User sender, GuildWrapper guild, TextChannel channel, Message message, String[] args, Member member) {
        if (PerGuildPermissions.isAdmin(sender)) {
            int shard = Integer.parseInt(args[0]);
            if (shard >= 0 && shard < Getters.getShards().size()) {
                MessageUtils.sendSuccessMessage("Restarting shard " + shard, channel);
                FlareBot.instance().getShardManager().restart(shard);
            } else
                MessageUtils.sendErrorMessage("Invalid shard ID!", channel);
        }
    }

    @Override
    public String getCommand() {
        return "restart";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getUsage() {
        return "{%}restart <shard>";
    }

    @Override
    public CommandType getType() {
        return CommandType.INTERNAL;
    }

    @Override
    public boolean isDefaultPermission() {
        return false;
    }
}
