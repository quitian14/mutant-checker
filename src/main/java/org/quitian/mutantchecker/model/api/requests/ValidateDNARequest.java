package org.quitian.mutantchecker.model.api.requests;

import java.util.List;

/**
 * Model for validate dna request
 */
public class ValidateDNARequest {
    private List<String> dna;

    public ValidateDNARequest() {
    }

    public ValidateDNARequest(List<String> dna) {
        this.dna = dna;
    }

    public List<String> getDna() {
        return dna;
    }

    public void setDna(List<String> dna) {
        this.dna = dna;
    }
}
