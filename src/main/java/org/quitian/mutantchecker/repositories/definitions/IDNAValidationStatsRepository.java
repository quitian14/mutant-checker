package org.quitian.mutantchecker.repositories.definitions;

import org.quitian.mutantchecker.model.entities.DNAValidationStats;

public interface IDNAValidationStatsRepository {

    void save(DNAValidationStats stats);

    DNAValidationStats get();
}
