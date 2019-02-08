package com.tw.deusemar.gtc.controller;

import static com.tw.deusemar.gtc.util.Config.AFTERNOON_TALK_DURATION;
import static com.tw.deusemar.gtc.util.Config.AFTERNOON_TALK_START_TIME;
import static com.tw.deusemar.gtc.util.Config.EVENT_DURATION_INDEX;
import static com.tw.deusemar.gtc.util.Config.EVENT_DURATION_UNIT_INDEX;
import static com.tw.deusemar.gtc.util.Config.EVENT_NAME_INDEX;
import static com.tw.deusemar.gtc.util.Config.INPUT_LINE_PATTERN;
import static com.tw.deusemar.gtc.util.Config.LUNCH_TALK_DURATION;
import static com.tw.deusemar.gtc.util.Config.LUNCH_TALK_START_TIME;
import static com.tw.deusemar.gtc.util.Config.MAX_EVENT_DURATION;
import static com.tw.deusemar.gtc.util.Config.MORNING_TALK_DURATION;
import static com.tw.deusemar.gtc.util.Config.MORNING_TALK_START_TIME;
import static com.tw.deusemar.gtc.util.Config.NETWORKING_EVENT_DURATION;
import static com.tw.deusemar.gtc.util.Config.NETWORKING_EVENT_DURATION_UNIT;
import static com.tw.deusemar.gtc.util.Config.NETWORKING_EVENT_MIN_START_TIME;
import static com.tw.deusemar.gtc.util.Config.NETWORKING_EVENT_NAME;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import com.tw.deusemar.gtc.entity.Conference;
import com.tw.deusemar.gtc.entity.Event;
import com.tw.deusemar.gtc.entity.Talk;
import com.tw.deusemar.gtc.entity.Track;
import com.tw.deusemar.gtc.entity.enumeration.DurationUnit;
import com.tw.deusemar.gtc.util.Logger;

public final class ConferenceScheduler {
    public ConferenceScheduler() {}

    private static Logger logger = Logger.getLogger();

    public Conference schedule(BufferedReader input) throws IOException {
        List<Event> events = new ArrayList<Event>();
        for (String line; (line = input.readLine()) != null;) {
            line = line.trim();
            Event event = parseInputLine(line);
            if (event == null) {
                continue;
            }
            events.add(event);
        }

        Conference conference = new Conference();
        while (events.size() != 0) {
            Talk morningTalk = new Talk(MORNING_TALK_DURATION, MORNING_TALK_START_TIME);
            fillTalkWithEvents(morningTalk, events);
            
            Talk lunchTalk = new Talk(LUNCH_TALK_DURATION, LUNCH_TALK_START_TIME);
            lunchTalk.addEvent(new Event("Lunch", LUNCH_TALK_DURATION, DurationUnit.MINUTES));
            
            Talk afternoonTalk = new Talk(AFTERNOON_TALK_DURATION, AFTERNOON_TALK_START_TIME);
            fillTalkWithEvents(afternoonTalk, events);
            
            Event networkingEvent = new Event(NETWORKING_EVENT_NAME, NETWORKING_EVENT_DURATION,
                    NETWORKING_EVENT_DURATION_UNIT);
            
            Talk networkingTalk = new Talk(networkingEvent.getDurationInMinutes(),
                    NETWORKING_EVENT_MIN_START_TIME);
            networkingTalk.addEvent(networkingEvent);
            afternoonTalk.addSupplementTalk(networkingTalk);
            Track track = new Track();
            track.addTalk(morningTalk);
            track.addTalk(lunchTalk);
            track.addTalk(afternoonTalk);
            conference.addTrack(track);
        }

        return conference;
    }

    private static void fillTalkWithEvents(Talk talk, List<Event> events) {
        for (Iterator<Event> iter = events.iterator(); iter.hasNext();) {
            Event event = iter.next();
            if (talk.hasRoomFor(event)) {
                talk.addEvent(event);
                iter.remove();
            }
        }
    }

    private static Event parseInputLine(String line) {
        if (line.length() == 0) {
            return null;
        }

        Matcher match = INPUT_LINE_PATTERN.matcher(line);
        if (match.find() == false) {
            logger.warn("Invalid input line: " + line);
            return null;
        }

        DurationUnit unit;
        if (match.group(EVENT_DURATION_UNIT_INDEX).equalsIgnoreCase("min")) {
            unit = DurationUnit.MINUTES;
        } else {
            unit = DurationUnit.LIGHTENING;
        }

        String name = match.group(EVENT_NAME_INDEX);
        String durationInString = match.group(EVENT_DURATION_INDEX);
        if (durationInString == null) {
            durationInString = "1";
        }
        int duration = Integer.parseInt(durationInString);

        Event event = new Event(name, duration, unit);
        if (event.getDurationInMinutes() > MAX_EVENT_DURATION) {
            logger.warn("Duration of event '" + name + "' is more than the maximum duration"
                    + " allowed for an event. Dropping this event for scheduling.");
            return null;
        }

        return event;
    }
}
