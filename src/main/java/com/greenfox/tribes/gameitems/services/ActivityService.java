package com.greenfox.tribes.gameitems.services;

import com.greenfox.tribes.game.repositories.ActivityLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    @Autowired
    private ActivityLogRepo activityLogRepo;



}
