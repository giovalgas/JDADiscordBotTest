package me.giodev.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class JoinVoiceChannelListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        ArrayList<Member> autokick = MessageReceivedListener.getAutokick();
        Member member = e.getMember();
        Guild guild = e.getGuild();

        if (autokick.isEmpty() || !(autokick.contains(member))) return;

        try {
            guild.moveVoiceMember(member, null).queue();

            member.getUser().openPrivateChannel().queue((chanel) -> {
                chanel.sendMessage("HOJE N, AQUI É XANDÃO").queue();
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
