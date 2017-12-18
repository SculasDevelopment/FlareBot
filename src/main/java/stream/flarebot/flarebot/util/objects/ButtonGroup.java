package stream.flarebot.flarebot.util.objects;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.eclipse.jetty.util.ConcurrentHashSet;
import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.util.buttons.ButtonRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ButtonGroup {

    private List<Button> buttons;

    public ButtonGroup() {
        buttons = new ArrayList<>();
    }

    /**
     * Adds a button to the button group.
     *
     * @param btn The button which you would like to add.
     */
    public void addButton(Button btn) {
        // Note I don't check if it already exists... so just don't fuck up :blobthumbsup:
        this.buttons.add(btn);
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public static class Button {

        private String unicode;
        private long emoteId;
        private ButtonRunnable runnable;

        public Button(String unicode, ButtonRunnable runnable) {
            this.unicode = unicode;
            this.runnable = runnable;
        }

        public Button(long emoteId, ButtonRunnable runnable) {
            this.emoteId = emoteId;
            this.runnable = runnable;
        }

        public long getEmoteId() {
            return emoteId;
        }

        public String getUnicode() {
            return unicode;
        }

        public void addReaction(Message message) {
            if(unicode != null)
                message.addReaction(unicode).queue();
            else
                message.addReaction(FlareBot.getInstance().getEmoteById(emoteId)).queue();
        }

        public void onClick(User user) {
            runnable.run(user);
        }
    }
}
