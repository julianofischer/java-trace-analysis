package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Event {
    private int from;
    private int to;
    private Integer time;
    private boolean isOpening;

    public Event(int from, int to, int time, boolean isOpening){
        this.setFrom(from);
        this.setTo(to);
        this.setTime(time);
        this.setOpening(isOpening);
    }


    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public boolean isOpening() {
        return isOpening;
    }

    public void setOpening(boolean opening) {
        isOpening = opening;
    }

    private static Event[] getEventFromLine(String line){
        Event[] events = new Event[2];
        String splitted[] = line.trim().split("\\s+");
        int from = Integer.parseInt(splitted[0]);
        int to = Integer.parseInt(splitted[1]);
        int initTime = Integer.parseInt(splitted[2]);
        int endTime = Integer.parseInt(splitted[2]);
        int closeTime = endTime + 1;
        //opening event
        events[0] = new Event(from, to, initTime, true);

        //close event
        events[1] = new Event(from, to, closeTime, false);
        return events;
    }

    public static ArrayList<Event> getEventsFromFile(File f){
        ArrayList<Event> events = new ArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String strline = null;
            while((strline = br.readLine()) != null){
                Event evs[]  = getEventFromLine(strline);
                Collections.addAll(events, evs);
            }
        }catch(IOException fnfe){
            fnfe.printStackTrace();
        }
        return events;
    }
}
