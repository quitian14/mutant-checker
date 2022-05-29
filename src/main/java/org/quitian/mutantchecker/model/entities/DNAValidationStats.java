package org.quitian.mutantchecker.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;

/**
 * Entity to summarize the DNA validations
 */
public class DNAValidationStats implements Serializable {

    private Integer id;
    /**
     * number of dna validation resulting in mutants
     */
    @JsonProperty("count_mutant_dna")
    private Integer countMutantDna;

    /**
     * number of dna validation resulting in humans
     */
    @JsonProperty("count_human_dna")
    private Integer countHumanDna;

    /**
     * mutantQty/humanQty
     */
    @JsonProperty("ratio")
    private Double ratio;

    public DNAValidationStats() {
    }

    public DNAValidationStats(Integer countMutantDna, Integer countHumanDna, Double ratio) {
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        this.ratio = ratio;
        id = 1;
    }

    public DNAValidationStats(Integer id, Integer countMutantDna, Integer countHumanDna, Double ratio) {
        this.id = id;
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        this.ratio = ratio;
    }

    public Integer getCountMutantDna() {
        return countMutantDna;
    }

    @JsonSetter("count_mutant_dna")
    public void setCountMutantDna(Integer countMutantDna) {
        this.countMutantDna = countMutantDna;
    }

    public Integer getCountHumanDna() {
        return countHumanDna;
    }

    public void setCountHumanDna(Integer countHumanDna) {
        this.countHumanDna = countHumanDna;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
