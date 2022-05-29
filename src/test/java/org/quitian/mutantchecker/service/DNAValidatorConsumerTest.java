package org.quitian.mutantchecker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quitian.mutantchecker.CommonTest;
import org.quitian.mutantchecker.messaging.DNAValidationConsumer;
import org.quitian.mutantchecker.model.entities.DNAValidation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DNAValidatorConsumerTest extends CommonTest {

    @Autowired
    private DNAValidationConsumer consumer;

    @Test
    public void consumePostValidationMessageForMutantValidation() throws Exception {
        String message = objectMapper.writeValueAsString(new DNAValidation("[1,2,3]", true));

        consumer.receiveNotificationCommand(message);

        Object[] stats = jdbcTemplate.query("SELECT mutant_qty,human_qty, ratio FROM tbl_dna_validations_stats",
                (rs, rn) -> new Object[]{rs.getInt("mutant_qty"),
                        rs.getInt("human_qty"),
                        rs.getDouble("ratio")}
        ).get(0);

        Assertions.assertEquals(1, Integer.valueOf(stats[0].toString()));
        Assertions.assertEquals(0, Integer.valueOf(stats[1].toString()));
        Assertions.assertEquals(100, Double.valueOf(stats[2].toString()));

        List<Object[]> validations = jdbcTemplate.query("SELECT dna,is_mutant FROM tbl_dna_validations WHERE dna = ?",
                (rs, rn) -> new Object[]{rs.getString("dna"),
                        rs.getString("is_mutant"),
                }
                , "[1,2,3]");

        Assertions.assertEquals(1, validations.size());
        Assertions.assertEquals("[1,2,3]", validations.get(0)[0]);
        Assertions.assertEquals("t", validations.get(0)[1]);
    }

    @Test
    public void consumePostValidationMessageForHumanValidation() throws Exception {
        String message = objectMapper.writeValueAsString(new DNAValidation("[1,2,3]", false));

        consumer.receiveNotificationCommand(message);

        Object[] stats = jdbcTemplate.query("SELECT mutant_qty,human_qty, ratio FROM tbl_dna_validations_stats",
                (rs, rn) -> new Object[]{rs.getInt("mutant_qty"),
                        rs.getInt("human_qty"),
                        rs.getDouble("ratio")}
        ).get(0);

        Assertions.assertEquals(0, Integer.valueOf(stats[0].toString()));
        Assertions.assertEquals(1, Integer.valueOf(stats[1].toString()));
        Assertions.assertEquals(0, Double.valueOf(stats[2].toString()));

        List<Object[]> validations = jdbcTemplate.query("SELECT dna,is_mutant FROM tbl_dna_validations WHERE dna = ?",
                (rs, rn) -> new Object[]{rs.getString("dna"),
                        rs.getString("is_mutant"),
                }
                , "[1,2,3]");

        Assertions.assertEquals(1, validations.size());
        Assertions.assertEquals("[1,2,3]", validations.get(0)[0]);
        Assertions.assertEquals("f", validations.get(0)[1]);
    }
}
