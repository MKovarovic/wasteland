package com.greenfox.tribes.models;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class Portrait {
    private Long id;
    private String head;
    private String hair;
    private String eyes;
    private String eyebrows;
    private String nose;
    private String mouth;

    @OneToOne
    @JoinColumn(name = "persona_id")
    Persona persona;

}
