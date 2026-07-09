package com.spotit.api.log.entity;

/** Not a DB column type — symptoms are stored as a text[] on CycleLog; this enum is only for request validation. */
public enum SymptomType {
    cramps, headache, bloating, tender, fatigue, nausea, backpain, acne, moodswings, insomnia, discharge
}
