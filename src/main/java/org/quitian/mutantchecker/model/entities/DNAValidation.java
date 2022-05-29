package org.quitian.mutantchecker.model.entities;

import java.io.Serializable;

/**
 * Entity to save the adn validations.
 */
public class DNAValidation implements Serializable {

    /**
     * DNA sequence validated
     */
    private String dna;

    /**
     * the sequence represents a mutant.
     */
    private boolean isMutant;

    public DNAValidation() {
    }

    public DNAValidation(String dna, boolean isMutant) {
        this.dna = dna;
        this.isMutant = isMutant;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public boolean isMutant() {
        return isMutant;
    }

    public void setMutant(boolean mutant) {
        isMutant = mutant;
    }
}
