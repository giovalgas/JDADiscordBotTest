package me.giodev.misc;

import me.giodev.BrrMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.*;

public class Classes {

    HashMap<String, String> className = new HashMap<>();
    HashMap<String, String> classLink = new HashMap<>();

    int[] schedule = {730, 815, 920, 105, 110, 1145, 1230};

    String[][] classes =  {
            {"ER", "BIO", "ESP", "ENG", "MAT", "MAT", "ACABOU"},
            {"BIO", "ART", "FIS","FIS", "ENG", "EF", "ACABOU"},
            {"EF", "BIO", "MAT", "QUIM", "FILO", "GEO", "ACABOU"},
            {"SOC", "QUIM", "PORT", "PORT", "MAT", "HIST", "ACABOU"},
            {"PORT", "PORT", "HIST", "ER", "GEO", "FIS", "ACABOU"}
    };

    public Classes(){

    }

    public void startTimer(){

        addLinks();
        addNames();

        Timer timer = new Timer();

        Date now = new Date();

        while(now.getSeconds() != 0){
            now = new Date();
        }

        System.out.println("[INFO] - Started the classes timer");

        timer.scheduleAtFixedRate(new TimerTask() {

            Date date = new Date();
            int day = date.getDay() - 1;

            @Override
            public void run() {
                date = new Date();

                if(day == -1 || day == 5) {
                    System.out.println("[INFO] - Cancelando a task das aulas (Não é um dia de semana) // " + day );
                    this.cancel();
                }

                switch(currentTime(date.getHours(), date.getMinutes())){

                    case 730:
                        sendMessage(classes[day][0]);
                        System.out.println("[INFO] - Aula de: " + classes[day][0]);
                        break;
                    case 815:
                        sendMessage(classes[day][1]);
                        System.out.println("[INFO] - Aula de: " + classes[day][1]);
                        break;
                    case 920:
                        sendMessage(classes[day][2]);
                        System.out.println("[INFO] - Aula de: " + classes[day][2]);
                        break;
                    case 105:
                        sendMessage(classes[day][3]);
                        System.out.println("[INFO] - Aula de: " + classes[day][3]);
                        break;
                    case 110:
                        sendMessage(classes[day][4]);
                        System.out.println("[INFO] - Aula de: " + classes[day][4]);
                        break;
                    case 1145:
                        sendMessage(classes[day][5]);
                        System.out.println("[INFO] - Aula de: " + classes[day][5]);
                        break;
                    case 1230:
                        sendMessage(classes[day][6]);
                        System.out.println("[INFO] - Aula de: " + classes[day][6]);
                        break;
                }

            }
        }, 0, 1000*60);
    }

    private void addLinks() {

        classLink.put("ER", "https://ead.redesalvatoriana.org.br/lms/disciplina/ensino-religioso-766#/resources");
        classLink.put("BIO", "https://ead.redesalvatoriana.org.br/lms/disciplina/biologia-773#/resources");
        classLink.put("ESP", "https://ead.redesalvatoriana.org.br/lms/disciplina/lingua-estr-espanhol-turma-202-775#/resources");
        classLink.put("ENG", "https://ead.redesalvatoriana.org.br/lms/disciplina/lingua-estr-ingles-turma-202-763#/resources");
        classLink.put("MAT", "https://ead.redesalvatoriana.org.br/lms/disciplina/matematica-771#/resources");
        classLink.put("ART", "https://ead.redesalvatoriana.org.br/lms/disciplina/arte-776#/resources");
        classLink.put("FIS", "https://ead.redesalvatoriana.org.br/lms/disciplina/fisica-764#/resources");
        classLink.put("EF", "https://ead.redesalvatoriana.org.br/lms/disciplina/turma-202-educacao-fisica-774#/resources");
        classLink.put("QUIM", "https://ead.redesalvatoriana.org.br/lms/disciplina/quimica-768/resources");
        classLink.put("FILO", "https://ead.redesalvatoriana.org.br/lms/disciplina/filosofia-770/resources");
        classLink.put("GEO", "https://ead.redesalvatoriana.org.br/lms/disciplina/geografia-769#/resources");
        classLink.put("SOC", "https://ead.redesalvatoriana.org.br/lms/disciplina/sociologia-765#/resources");
        classLink.put("PORT", "https://ead.redesalvatoriana.org.br/lms/disciplina/lingua-portuguesa-e-literatura-772#/resources");
        classLink.put("HIST", "https://ead.redesalvatoriana.org.br/lms/disciplina/historia-767#/resources");
        classLink.put("ACABOU", "https://www.pornhub.com");

    }

//    "Ensino Religioso"
//    "Biologia"
//    "Espanhol"
//    "Ingles"
//    "Matematica"
//    "Artes"
//    "Fisica"
//    "Educação Fisica"
//    "Quimica"
//    "Filosofia"
//    "Geografia"
//    "Sociologia"
//    "Lingua Portuguesa"
//    "Historia"

    private void addNames(){

        className.put("ER", "Ensino Religioso");
        className.put("BIO", "Biologia");
        className.put("ESP", "Espanhol");
        className.put("ENG", "Ingles");
        className.put("MAT", "Matematica");
        className.put("ART", "Artes");
        className.put("FIS", "Fisica");
        className.put("EF", "Educação Fisica");
        className.put("QUIM", "Quimica");
        className.put("FILO", "Filosofia");
        className.put("GEO", "Geografia");
        className.put("SOC", "Sociologia");
        className.put("PORT", "Lingua Portuguesa");
        className.put("HIST", "Historia");
        className.put("ACABOU", "Acabou");

    }

    public void sendMessage(String s){

        System.out.println(s + "//" + className.get(s) + "//" + classLink.get(s));

        Date now = new Date();


        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(className.get(s), classLink.get(s));
        eb.addField(new MessageEmbed.Field("GRANDESSISSIMA AULA DE: ", className.get(s), false, false));
        eb.setFooter("haha brr - " + now.getHours() + ":" + now.getMinutes(), BrrMain.getGuild().getSelfMember().getUser().getAvatarUrl());
        eb.setThumbnail("https://pbs.twimg.com/profile_images/983018479644303361/UD5uocVf_400x400.jpg");

        BrrMain.getGuild().getTextChannelById(BrrMain.getCleitonId()).sendMessage(eb.build()).queue();
        BrrMain.getGuild().getTextChannelById(BrrMain.getCleitonId()).sendMessage(BrrMain.getGuild().getRoleById("754331414618439722").getAsMention()).queue();

    }

    public int currentTime(int hours, int minutes){
        String s = hours + "" + minutes;
        return Integer.parseInt(s);
    }

}
