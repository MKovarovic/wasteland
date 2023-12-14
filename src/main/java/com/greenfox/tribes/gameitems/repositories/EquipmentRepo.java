package com.greenfox.tribes.gameitems.repositories;

import com.greenfox.tribes.gameitems.models.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepo extends JpaRepository<Equipment, Long> {}
