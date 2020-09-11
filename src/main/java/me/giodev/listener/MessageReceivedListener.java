package me.giodev.listener;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class MessageReceivedListener extends ListenerAdapter {

    String botRoleId = "753494143757713531";
    String[] commands = {"PING", "AUTOKICK", "SPAMROLE", "REMOVESPAM"};
    String prefix = "-";
    String[] ids = {"216340083035340801", "462030738020106261"};

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

        if(mentionedMembersList.isEmpty()) {
            channel.sendMessage(getUsage(command)).queue();
            return;
        };

        Member member = mentionedMembersList.get(0);
        Role botRole = e.getGuild().getRoleById(botRoleId);

        String[] args = e.getMessage().getContentRaw().split(" ");


        switch (command.replace(prefix, "").toUpperCase()) {

            case "PING":
                channel.sendMessage("Pong!").queue();
                break;
            case "AUTOKICK":

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
