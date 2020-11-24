package me.giodev.listener;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.User;
import java.util.ArrayList;
import java.util.List;

public class MessageReceivedListener extends ListenerAdapter {

    String botRoleId = "753494143757713531";
    String[] commands = {"PING", "AUTOKICK", "SPAMROLE", "REMOVESPAM", "MUTEALL", "UNMUTEALL", "CN"};
    String prefix = "-";
    String[] ids = {"216340083035340801", "462030738020106261", "267952534952476673"};

    private static ArrayList<Member> autokick = new ArrayList<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        String command = e.getMessage().getContentRaw().split(" ")[0];
        if (e.getAuthor().isBot() || !(isAuthorised(e.getAuthor())) || !(command.startsWith(prefix))) return;
        if(!(isCommand(command.toUpperCase().replace(prefix, "")))) return;

        System.out.println(command + " : " + e.getAuthor().getName());

        MessageChannel channel = e.getChannel();
        Guild guild = e.getGuild();
        List<Member> mentionedMembersList = e.getMessage().getMentionedMembers();
        VoiceChannel vc = guild.getVoiceChannelById("755889573170642955");

        Role botRole = e.getGuild().getRoleById(botRoleId);

        String[] args = e.getMessage().getContentRaw().split(" ");

        Member member;

        switch (command.replace(prefix, "").toUpperCase()) {

            case "PING":
                channel.sendMessage("Pong!").queue();
                break;
            case "CN":
                if(mentionedMembersList.isEmpty()) {
                    channel.sendMessage(getUsage(command)).queue();
                    return;
                };

                member = mentionedMembersList.get(0);
                member.modifyNickname(args[1]).queue();

                break;
            case "AUTOKICK":
                if(mentionedMembersList.isEmpty()) {
                    channel.sendMessage(getUsage(command)).queue();
                    return;
                };

                member = mentionedMembersList.get(0);

                System.out.println(args.length);

                if (mentionedMembersList.isEmpty() || args.length < 3) {
                    channel.sendMessage(getUsage(command)).queue();
                    break;
                }

                if (args[2].equals("true")) {
                    channel.sendMessage("Now auto-kicking " + member.getAsMention() + "!").queue();

                    autokick.add(member);

                    if (member.getVoiceState().getChannel() != null) {
                        try {
                            guild.moveVoiceMember(member, null).queue();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                } else if (args[2].equals("false") && autokick.contains(member)) {
                    channel.sendMessage("Now stopped auto-kicking " + member.getAsMention() + "!").queue();
                    autokick.remove(member);
                }

                break;
            case "SPAMROLE":
                if(mentionedMembersList.isEmpty()) {
                    channel.sendMessage(getUsage(command)).queue();
                    return;
                };

                member = mentionedMembersList.get(0);

                if (mentionedMembersList.isEmpty() || args.length < 2) {
                    channel.sendMessage(getUsage(command)).queue();
                    break;
                }

                for (Role role : guild.getRoles()) {
                    if (botRole.getPosition() > role.getPosition() && !(role.isPublicRole()) ) {

                        System.out.println(botRole.canInteract(role));

                        guild.addRoleToMember(member, role).queue();

                        member.getUser().openPrivateChannel().queue((c) -> {
                            c.sendMessage(role.getName() + " Foi adicionado a sua conta, AQUI É XANDÃO").queue();
                        });
                    }
                }

                e.getChannel().sendMessage("Done!").queue();
                break;
            case "REMOVESPAM":
                if(mentionedMembersList.isEmpty()) {
                    channel.sendMessage(getUsage(command)).queue();
                    return;
                };

                member = mentionedMembersList.get(0);

                if (mentionedMembersList.isEmpty() || args.length < 2) {
                    channel.sendMessage(getUsage(command)).queue();
                    break;
                }

                for (Role role : guild.getRoles()) {
                    if ( botRole.getPosition() > role.getPosition() && !(role.isPublicRole()) ) {
                        guild.removeRoleFromMember(member, role).queue();
                    }
                }

                e.getChannel().sendMessage("Done!").queue();

                break;
            case "MUTEALL":
                for(Member m : vc.getMembers()){
                    m.mute(true).queue();
                    System.out.println("mutando " + m.getNickname());
                }
                break;
            case "UNMUTEALL":
                for(Member m : vc.getMembers()){
                    m.mute(false).queue();
                    System.out.println("desmutando " + m.getNickname());
                }

                break;

        }
    }

    public String getUsage(String command) {
        switch (command.toUpperCase().replace(prefix , "")) {
            case"PING":
                return "Pong!";
            case "SPAMROLE":
                return "Usage: -spamrole @user";
            case "AUTOKICK":
                return "Usage: -autokick @user (true/false)";
            case "REMOVESPAM":
                return "Usage: -removespam @user";
            case "MUTEALL":
                return "";
            default:
                return "error: " + command.toUpperCase();
        }
    }

    public boolean isCommand(String command){
        for(String c : commands){
            if(c.equals(command)) return true;
        }
        return false;
    }

    public boolean isAuthorised(User author){

        for(String id : ids){
//            System.out.println(id + "=" + author.getId());
            if(author.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Member> getAutokick() {
        return autokick;
    }
    public static void setAutokick(ArrayList<Member> autokick) {
        MessageReceivedListener.autokick = autokick;
    }
}
