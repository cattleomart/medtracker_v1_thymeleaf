package com.cathalob.medtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Evaluation {
    private List<EvaluationEntry> entries;
    public void setEntries(List<EvaluationEntry> entries) {
        this.entries = entries;
    }
    public List<EvaluationEntry> getEntries() {
        return entries;
    }
    private String importedFilename = "";
    public String getImportedFilename() {
        return importedFilename;
    }

    public void setImportedFilename(String importedFilename) {
        this.importedFilename = importedFilename;
    }
    public Evaluation(String newDatePrefix) {
        setImportedFilename("Uninitialized");
        entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new EvaluationEntry(newDatePrefix, i));
        }
    }

    public Evaluation() {
    }
}
