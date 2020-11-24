package me.giodev;


import me.giodev.listener.JoinVoiceChannelListener;
import me.giodev.listener.MessageReceivedListener;
import me.giodev.misc.SchoolClasses;
import me.giodev.misc.TwitterManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BrrMain {

    public static Guild getGuild() {
        return guild;
    }
    public static Guild guild;
    public static String guildId = "595773130300063777";
    private static String[] users = {"Hytale"};

    public static String getChannelId() {
        return channelId;
    }

    public static String channelId = "712260285762633800";
    static ArrayList<String> previousStatuses = new ArrayList<String>();
    public static Twitter twitter;

    public static Twitter getTwitter(){
        return twitter;
    }

    static boolean firstTime = true;

    public static void main(String[] arguments) throws Exception {

        JDA api = JDABuilder.createDefault(config.getDiscordApi()).build();
        System.out.println("Bot Running.");

        api.addEventListener(new JoinVoiceChannelListener());
        api.addEventListener(new MessageReceivedListener());

        SchoolClasses schoolClasses = new SchoolClasses();
        TwitterManager twitterManager = new TwitterManager(users);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(config.getConsumerKey())
                .setOAuthConsumerSecret(config.getConsumerSecret())
                .setOAuthAccessToken(config.getAccesToken())
                .setOAuthAccessTokenSecret(config.getAcessTokenSecret());

        TwitterFactory tf = new TwitterFactory(cb.build());

        twitter = tf.getInstance();

        guild = api.getGuildById(guildId);

        schoolClasses.startTimer();
        twitterManager.startTimer();
    }
}
