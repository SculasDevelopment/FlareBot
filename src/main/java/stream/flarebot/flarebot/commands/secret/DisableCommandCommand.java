package stream.flarebot.flarebot.commands.secret;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.FlareBotManager;
import stream.flarebot.flarebot.commands.Command;
import stream.flarebot.flarebot.commands.CommandType;
import stream.flarebot.flarebot.commands.InternalCommand;
import stream.flarebot.flarebot.objects.GuildWrapper;
import stream.flarebot.flarebot.permissions.PerGuildPermissions;
import stream.flarebot.flarebot.util.MessageUtils;
import stream.flarebot.flarebot.util.general.GuildUtils;

import java.awt.Color;
import java.util.Map;

public class DisableCommandCommand implements InternalCommand {

    @Override
    public void onCommand(User sender, GuildWrapper guild, TextChannel channel, Message message, String[] args, Member member) {
        if (PerGuildPermissions.isCreator(sender) || PerGuildPermissions.isContributor(sender)) {
            if (args.length == 0) {
                channel.sendMessage("Can't really disable commands if you don't supply any ¯\\_(ツ)_/¯").queue();
                return;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                StringBuilder sb = new StringBuilder();
                Map<String, String> disabledCmds = FlareBotManager.instance().getDisabledCommands();
                for (String cmd : disabledCmds.keySet()) {
                    sb.append("`").append(cmd).append("` - ").append(disabledCmds.get(cmd));
                }
                channel.sendMessage(MessageUtils.getEmbed(sender).setColor(Color.orange).addField("Disabled Commands",
                        sb.toString(), false).build()).queue();
                return;
            }
            String msg = MessageUtils.getMessage(args, 0);
            String reason = "This command is currently disabled! Please check our support server for more info! " +
                    "https://flarebot.stream/support-server";
            if (msg.contains("-"))
                reason = msg.substring(msg.indexOf("-") + 1).trim();
            String[] cmds = msg.substring(0, msg.contains("-") ? msg.indexOf("-") : msg.length()).trim().split("\\|");
            StringBuilder sb = new StringBuilder();
            for (String command : cmds) {
                Command cmd = FlareBot.getCommandManager().getCommand(command.trim(), sender);
                if (cmd == null || cmd.getType() == CommandType.SECRET) continue;

                // If it's already disabled and there's only 1 (with a reason supplied) just update the reason.
                if (cmds.length == 1 && msg.contains("-") && FlareBotManager.instance().isCommandDisabled(cmd.getCommand())) {
                    FlareBotManager.instance().getDisabledCommands().put(cmd.getCommand(), reason);
                    sb.append("`").append(cmd.getCommand()).append("` - ").append(getEmote(false))
                            .append(" New reason: ").append(reason);
                } else
                    sb.append("`").append(cmd.getCommand()).append("` - ").append(getEmote(FlareBotManager.instance()
                            .toggleCommand(cmd.getCommand(), reason))).append("\n");
            }
            if (sb.toString().isEmpty()) return;
            channel.sendMessage(MessageUtils.getEmbed(sender).setColor(Color.orange).setDescription(sb.toString())
                    .build()).queue();
        }
    }

    @Override
    public String getCommand() {
        return "disablecommand";
    }

    @Override
    public String getDescription() {
        return "Disable or enable commands.";
    }

    @Override
    public String getUsage() {
        return "{%}disablecommand <command | ...> (- reason)";
    }

    @Override
    public CommandType getType() {
        return CommandType.SECRET;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"fuckthat", "disablecmd"};
    }

    private String getEmote(boolean b) {
        // tick - enabled (true)
        return (b ? GuildUtils.getEmoteById(355776056092917761L).getAsMention()
                // cross - disabled (false)
                : GuildUtils.getEmoteById(355776081384570881L).getAsMention());
    }
}
