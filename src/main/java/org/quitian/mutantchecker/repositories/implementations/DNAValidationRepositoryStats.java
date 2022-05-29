package org.quitian.mutantchecker.repositories.implementations;

import org.quitian.mutantchecker.model.entities.DNAValidationStats;
import org.quitian.mutantchecker.repositories.definitions.IDNAValidationStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC implementation of IDNAValidationStatsRepository
 */
@Repository
public class DNAValidationRepositoryStats implements IDNAValidationStatsRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Save the validation statistic.
     * @param stats
     */
    @Override
    public void save(DNAValidationStats stats) {
        jdbcTemplate.update("INSERT INTO tbl_dna_validations_stats(id,mutant_qty,human_qty, ratio) VALUES (:id,:mutant_qty,:human_qty,:ratio) " +
                        "ON CONFLICT (id) DO UPDATE SET mutant_qty=:mutant_qty,human_qty=:human_qty, ratio=:ratio",
        new MapSqlParameterSource()
                .addValue("mutant_qty", stats.getCountMutantDna())
                .addValue("human_qty", stats.getCountHumanDna())
                .addValue("ratio", stats.getRatio())
                .addValue("id", stats.getId())
        );
    }

    /**
     * Get the validations' statistics.
     * @return statistics.
     */
    @Override
    public DNAValidationStats get() {
        List<DNAValidationStats> stats = jdbcTemplate.query("SELECT id, mutant_qty,human_qty, ratio FROM tbl_dna_validations_stats order by id desc",
                (rs, rn) -> new DNAValidationStats(
                        rs.getInt("id"),
                        rs.getInt("mutant_qty"),
                        rs.getInt("human_qty"),
                        rs.getDouble("ratio"))
        );

        if (stats.isEmpty()) return new DNAValidationStats(0, 0, 0.0);

        return stats.get(0);
    }
}
