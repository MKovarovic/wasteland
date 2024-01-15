package com.greenfox.tribes.services;

import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class CharacterEquipmentService {
  @Autowired
  CharacterEquipmentRepository repo;
}
