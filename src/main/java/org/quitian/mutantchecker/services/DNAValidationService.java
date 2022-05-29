package org.quitian.mutantchecker.services;

import org.quitian.mutantchecker.exceptions.IsMutantException;
import org.quitian.mutantchecker.messaging.ValidationQueueProducer;
import org.quitian.mutantchecker.model.entities.DNAValidation;
import org.quitian.mutantchecker.model.entities.DNAValidationStats;
import org.quitian.mutantchecker.repositories.definitions.IDNAValidationRepository;
import org.quitian.mutantchecker.repositories.definitions.IDNAValidationStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Business Logic implementation
 */
@Service
public class DNAValidationService {

    @Autowired
    private IDNAValidationRepository dnaValidationRepository;

    @Autowired
    private IDNAValidationStatsRepository dnaValidationStatsRepository;

    @Autowired
    private DNAValidator dnaValidator;

    @Autowired
    private ValidationQueueProducer producer;

    /**
     * Save the dna validation
     * @param validation dna secuence validation result
     */
    @CacheEvict(value = "dna_stats", key = "'stat'")
    public void saveValidation(DNAValidation validation) {
        dnaValidationRepository.save(validation);

        DNAValidationStats stats = dnaValidationStatsRepository.get();

        if (validation.isMutant()) {
            stats.setCountMutantDna(stats.getCountMutantDna() + 1);
        } else {
            stats.setCountHumanDna(stats.getCountHumanDna() + 1);
        }

        stats.setRatio(stats.getCountHumanDna() == 0 ? 100 : (double)stats.getCountMutantDna()/(double)stats.getCountHumanDna());
        dnaValidationStatsRepository.save(stats);
    }

    /**
     * VAlidate a DNA sequence. this methods send the validation result to a queue por post-processing.
     * @param dnaSequence list of dna strings
     * @return a validation
     */
    public DNAValidation validateDNA(String[] dnaSequence) {
        DNAValidation validation = dnaValidationRepository.getValidation(Arrays.toString(dnaSequence));

        if (validation == null) {
            validation = dnaValidator.validate(dnaSequence);
            producer.saveDNAValidation(validation);
        }

        if (!validation.isMutant()) {
            throw new IsMutantException("This DNA is not a mutant sequence");
        }

        return validation;
    }

    /**
     * Get the last statistics from dna validations.
     * @return the stats
     */
    @Cacheable(value = "dna_stats", cacheManager = "expire30Secs", unless="#result == null", key = "'stat'")
    public DNAValidationStats getStats() {
        return dnaValidationStatsRepository.get();
    }

}
