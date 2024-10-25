package com.cathalob.medtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Evaluation {
    public void setEntries(List<EvaluationEntry> entries) {
        this.entries = entries;
    }

    public List<EvaluationEntry> getEntries() {
        return entries;
    }

    private List<EvaluationEntry> entries;
    public Evaluation(String newDatePrefix) {

        entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new EvaluationEntry(newDatePrefix, i));
        }
    }

    public Evaluation() {
    }
}
