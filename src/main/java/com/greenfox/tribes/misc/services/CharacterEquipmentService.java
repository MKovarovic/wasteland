package com.greenfox.tribes.misc.services;

import com.greenfox.tribes.misc.repositories.CharacterEquipmentRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



@Service
public class CharacterEquipmentService {
  @Autowired CharacterEquipmentRepo repo;
}
