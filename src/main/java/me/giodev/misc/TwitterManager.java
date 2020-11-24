package me.giodev.misc;

import me.giodev.BrrMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TwitterManager {

    private String channelId;
    private Twitter twitter;
    private ArrayList<String> previousStatuses = new ArrayList<>();
    private String[] users;
    private static boolean firstTime;


    public TwitterManager(String[] users){
        this.users = users;
        this.twitter = BrrMain.getTwitter();
        this.channelId = BrrMain.getChannelId();
    }

    public void startTimer(){

        firstTime = false;
        getTimeline();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            ArrayList<String> ps = previousStatuses;
            ResponseList<Status> statuses;

            @Override
            public void run() {
                boolean found = false;
                statuses = getTimeline();
                for(Status s : statuses ){
                    if(!(ps.contains(s.getText()))) {

                        EmbedBuilder eb = new EmbedBuilder();

                        eb.setTitle("@"+s.getUser().getScreenName(), "https://twitter.com/" + users[0]);
                        eb.setThumbnail(s.getUser().get400x400ProfileImageURL());
                        eb.setDescription("New tweet from @" + users[0]);

                        if(s.getMediaEntities().length >= 1){
                            eb.setImage(s.getMediaEntities()[0].getMediaURL());
                            String ss = s.getText().split(" ")[s.getText().split(" ").length - 1];
                            eb.addField(new MessageEmbed.Field(s.getText().replace(ss, ""), "", false, false));
                        }
                        else{
                            eb.addField(new MessageEmbed.Field(s.getText(), "", false, false));
                        }
                        Guild g = BrrMain.getGuild();
                        JDA api = g.getJDA();

                        eb.setFooter("@" + users[0] + " // " + s.getCreatedAt() + " // Retweets: " +s.getRetweetCount(), api.getSelfUser().getAvatarUrl());

                        g.getTextChannelById(channelId).sendMessage(eb.build()).queue();
                        g.getTextChannelById(channelId).sendMessage("@everyone").queue();

                        found = true;
                        System.out.println("NEW TWEET: @" + s.getUser().getScreenName() + " - " + s.getText());
                        previousStatuses.add(s.getText());

                    }
                }

                if(!found) System.out.println("Did not find any new tweets");

            }
        }, 0, 600*1000);

    }

    public ResponseList<Status> getTimeline(){

        try {

            ResponseList<Status> statuses;
            String user;

            if (users.length == 1) {

                user = users[0];
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
