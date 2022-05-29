package org.quitian.mutantchecker.repositories.implementations;

import org.quitian.mutantchecker.model.entities.DNAValidation;
import org.quitian.mutantchecker.repositories.definitions.IDNAValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for DNAValidation
 */
@Repository
public class DNAValidationRepository implements IDNAValidationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Save the validation on the database. only one record by DNA
     * @param dnaValidation
     * @return
     */
    @Override
    public void save(DNAValidation dnaValidation) {
        jdbcTemplate.update("INSERT INTO tbl_dna_validations(dna,is_mutant) VALUES (?,?) " +
                        "ON CONFLICT (dna) DO UPDATE SET is_mutant = ?",
                dnaValidation.getDna(),
                dnaValidation.isMutant(),
                dnaValidation.isMutant());
    }

    @Override
    @Cacheable(value = "dna_validation", key = "#dna", cacheManager = "expireOneDay", unless="#result == null")
    public DNAValidation getValidation(String dna) {
        List<DNAValidation> stats = jdbcTemplate.query("SELECT dna, is_mutant FROM tbl_dna_validations WHERE dna = ?",
                (rs, rn) -> new DNAValidation(
                        rs.getString("dna"),
                        rs.getBoolean("is_mutant")), dna
        );

        return stats.isEmpty() ? null : stats.get(0);
    }
}
