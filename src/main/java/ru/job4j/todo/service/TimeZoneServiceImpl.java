package ru.job4j.todo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
public class TimeZoneServiceImpl implements TimeZoneService {

    @Override
    public List<TimeZone> getTimeZones() {
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        return zones;
    }

}
