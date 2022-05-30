package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class TestTask {

    public static void main(String[] args) throws IOException, ParseException {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yy HH:mm");
        List<Long> flytime = new ArrayList<>();
        long summa = 0;

        try{
            if(args.length!=1){
                System.out.println("Usage: TestTask name_of_file");
                return; }
            BufferedReader reader = new BufferedReader( new FileReader (args[0]));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while( ( line = reader.readLine() ) != null ) stringBuilder.append( line );
            json = stringBuilder.toString();
            json = json.substring(json.indexOf("["), json.indexOf("]") + 1);
            json = json.replaceAll(" {4}", " ");
            json = json.replaceAll(" {2}", " ");
            json = json.replaceFirst(" ", "");
        }
        catch(IOException e) {
            System.out.println(e);
        }

        try{
            List<TestTask.Ticket> tickets = mapper.readValue(json, new TypeReference<>() {});
            for (TestTask.Ticket tmp : tickets) {
                Date departure = ft.parse(tmp.departure_date + " " + tmp.departure_time);
                Date arrival = ft.parse(tmp.arrival_date + " " + tmp.arrival_time);
//                System.out.println(tmp.departure_time + " " + tmp.arrival_time);
                long difference = arrival.getTime() - departure.getTime();
                flytime.add(difference);
                System.out.println(toOurTime(difference));
                summa += difference;
            }
            long middleone = summa/flytime.size();
            System.out.println("Average fly time = " + toOurTime(middleone));
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        Collections.sort(flytime);
        int index = (int) ((flytime.size() * 0.9) - 1);
        System.out.println("90th percentile = " + toOurTime(flytime.get(index)));
    }

    public static String toOurTime(long time){

        long hours = time / (60 * 60 * 1000);
        long minutes = time / (60 * 1000);
        minutes = minutes - (hours * 60);

        return hours + " hours and " + minutes + " minutes";
    }
    public static class Ticket {
        public String origin;
        public String origin_name;
        public String destination;
        public String destination_name;
        public String departure_date;
        public String departure_time;
        public String arrival_date;
        public String arrival_time;
        public String carrier;
        public int stops;
        public int price;


        public Ticket() {
        }
    }
}


