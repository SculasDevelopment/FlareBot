package stream.flarebot.flarebot.util;

import net.dv8tion.jda.api.JDA;
import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.Getters;

public class ShardUtils {

    private static final FlareBot flareBot = FlareBot.instance();
    private static final long POSSIBLE_DEAD_SHARD_TIMEOUT = 15_000L;

    private static int getShardCount() {
        return flareBot.getShardManager().getShards().size();
    }

    /**
     * Get the shard ID of a JDA instance, if the JDA instance doesn't have ShardInfo (aka not sharded) then it will
     * return 0.
     *
     * @param jda The JDA instance of a certain shard.
     * @return The JDA shard ID as an integer.
     */
    public static int getShardId(JDA jda) {
        if (jda.getShardInfo() == null) return 0;
        return jda.getShardInfo().getShardId();
    }

    /**
     * Get the "display" shard ID, this is basically the normal shard ID + 1 so that it is no longer 0 indexed.
     *
     * @param jda The JDA instance of a certain shard.
     * @return The JDA shard ID as an integer + 1.
     */
    public static int getDisplayShardId(JDA jda) {
        return getShardId(jda) + 1;
    }

    /**
     * Retrieves a {@code JDA} instance of a particular shard (Specified by the ID).
     *
     * @param shardId The shard ID to get.
     */
    public static JDA getShardById(int shardId) {
        return flareBot.getShardManager().getShardById(shardId);
    }

    /**
     * Gets the last time an event happened on a shard.
     *
     * @param shardId The shard ID to get the last event time for.
     * @return The last event time of the provided shard ID.
     */
    public static long getLastEventTime(int shardId) {
        return System.currentTimeMillis() - FlareBot.instance().getEvents().getShardEventTime().get(shardId);
    }

    /**
     * Checks if a shard is reconnecting using the provided JDA instance.
     *
     * @param jda The shard to check for reconnecting.
     * @return If the shard is reconnecting or not.
     * @see ShardUtils#isReconnecting(int)
     */
    public static boolean isReconnecting(JDA jda) {
        return isReconnecting(jda.getShardInfo().getShardId());
    }

    /**
     * Checks if a shard is connecting using the provided shard ID.
     * <p>
     * Returns {@code false} if the shard ID is invalid.
     *
     * @param shardId The shard ID to check for reconnecting.
     * @return If the shard is reconnecting or not
     */
    public static boolean isReconnecting(int shardId) {
        return shardId >= 0 && shardId <= getShardCount() && (getShardById(shardId).getStatus() ==
                JDA.Status.RECONNECT_QUEUED || getShardById(shardId).getStatus() == JDA.Status.ATTEMPTING_TO_RECONNECT);
    }

    /**
     * Checks if a shard is dead by comparing the last event time to the {@link ShardUtils#POSSIBLE_DEAD_SHARD_TIMEOUT}.
     *
     * @param jda The shard to check for being dead.
     * @return Whether the shard is dead or not.
     */
    public static boolean isDead(JDA jda) {
        return isDead(jda.getShardInfo().getShardId(), POSSIBLE_DEAD_SHARD_TIMEOUT);
    }

    /**
     * Checks if a shard is dead by comparing the last event time to the {@link ShardUtils#POSSIBLE_DEAD_SHARD_TIMEOUT}.
     *
     * @param shardId The shard ID to check for being dead.
     * @return Whether the shard is dead or not.
     */
    public static boolean isDead(int shardId) {
        return isDead(shardId, POSSIBLE_DEAD_SHARD_TIMEOUT);
    }

    /**
     * Checks if a shard is dead by comparing the last event time to the provided timeout.
     *
     * @param jda     The shard to check for being dead.
     * @param timeout The timeout the compare the last event time
     * @return Whether the shard is dead or not.
     */
    public static boolean isDead(JDA jda, long timeout) {
        return isDead(jda.getShardInfo().getShardId(), timeout);
    }

    /**
     * Checks if a shard is dead by comparing the last event time to the provided timeout.
     *
     * @param shardId The shard ID to check for being dead.
     * @param timeout The timeout the compare the last event time
     * @return Whether the shard is dead or not.
     */
    public static boolean isDead(int shardId, long timeout) {
        return shardId >= 0 && shardId <= getShardCount() && getLastEventTime(shardId) >= timeout && !isReconnecting(shardId);
    }

    public static long[] getPingsForShards() {
        long[] pings = new long[Getters.getShards().size()];
        for (int shardId = 0; shardId < pings.length; shardId++)
            pings[shardId] = getShardById(shardId).getPing();
        return pings;
    }
}
