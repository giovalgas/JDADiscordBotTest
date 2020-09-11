package me.giodev;


import com.sun.tools.javac.util.List;
import me.giodev.listener.JoinVoiceChannelListener;
import me.giodev.listener.MessageReceivedListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BrrMain {

    static String lgbtId = "595773130300063777";
    static String cleitonId = "712260285762633800";
    static ArrayList<String> previousStatuses = new ArrayList<String>();
    static Twitter twitter;
    static boolean firstTime = true;

    public static void main(String[] arguments) throws Exception
    {
        JDA api = JDABuilder.createDefault("NzUzNDU3NTU0NjM3NTg2NTQz.X1meBw.kyu1J1oDmJRIshTXbS0CsS0eRHo").build();
        System.out.println("Bot Running.");

        api.addEventListener(new JoinVoiceChannelListener());
        api.addEventListener(new MessageReceivedListener());

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("pFp8shi3j3YPf8NhDQrtplOZy")
                .setOAuthConsumerSecret("UPlh4vvYBz8UQ509vslRgsMjWkKXwxlf4PwQUYO02LOrXEfGMV")
                .setOAuthAccessToken("2270699095-vRRl68w3vDuXd1MGbgBoS0JWyYF38i7EUtcNypL")
                .setOAuthAccessTokenSecret("1J5p5UmADOwSReYqUlYWsdkpas26zspkMRs4ZLkZ5BOD8");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

        getTimeline();
        firstTime = false;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            ArrayList<String> ps = previousStatuses;
            ResponseList<Status> statuses;

            @Override
            public void run() {

                statuses = getTimeline();
                for(Status s : statuses ){
                    if(!(ps.contains(s.getText()))) {

                        EmbedBuilder eb = new EmbedBuilder();

                        eb.setTitle("@"+s.getUser().getScreenName(), "https://twitter.com/fgp_arthurr");
                        eb.setThumbnail(s.getUser().get400x400ProfileImageURL());
                        eb.setDescription("AQUI TEMOS O TWEET DO NOSSO AMIGAOOO");

                        if(s.getMediaEntities().length >= 1){
                            eb.setImage(s.getMediaEntities()[0].getMediaURL());
                            String ss = s.getText().split(" ")[s.getText().split(" ").length - 1];
                            eb.addField(new MessageEmbed.Field(s.getText().replace(ss, ""), "", false, false));
                        }
                        else{
                            eb.addField(new MessageEmbed.Field(s.getText(), "", false, false));
                        }
                        eb.setFooter("haha skrr // " + s.getCreatedAt() + " // Retweets: " +s.getRetweetCount(), api.getSelfUser().getAvatarUrl());
                        Guild g = api.getGuildById(lgbtId);

                        g.getTextChannelById(cleitonId).sendMessage(eb.build()).queue();
                        g.getTextChannelById(cleitonId).sendMessage("@everyone").queue();

                        previousStatuses.add(s.getText());

                    }
                }
            }
        }, 0, 1000);

    }

    public static ResponseList<Status> getTimeline(){
        String[] args = {"fgp_arthurr"};
        try {
            ResponseList<Status> statuses;
            String user;
            if (args.length == 1) {
                user = args[0];

                statuses = twitter.getUserTimeline(user);
            } else {
                user = twitter.verifyCredentials().getScreenName();
                statuses = twitter.getUserTimeline();
            }

            if(firstTime) {
                System.out.println("Showing @" + user + "'s user timeline.");
                for (Status status : statuses) {
                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                    previousStatuses.add(status.getText());

                }
            }

            return statuses;

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
        return null;
    }

}
