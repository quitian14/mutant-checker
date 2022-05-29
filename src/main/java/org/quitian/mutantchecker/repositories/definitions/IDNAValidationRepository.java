package org.quitian.mutantchecker.repositories.definitions;

import org.quitian.mutantchecker.model.entities.DNAValidation;

/**
 * Interface to define the operations of DNAValidationRepository
 */
public interface IDNAValidationRepository {

    void save(DNAValidation dnaValidation);

    DNAValidation getValidation(String dna);
}
