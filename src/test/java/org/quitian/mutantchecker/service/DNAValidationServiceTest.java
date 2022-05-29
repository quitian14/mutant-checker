package org.quitian.mutantchecker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quitian.mutantchecker.CommonTest;
import org.quitian.mutantchecker.model.entities.DNAValidation;
import org.quitian.mutantchecker.services.DNAValidationService;
import org.springframework.beans.factory.annotation.Autowired;

public class DNAValidationServiceTest extends CommonTest {

    @Autowired
    private DNAValidationService service;

    @Test
    public void validateDNAForMutantTest() throws Exception {
        DNAValidation validation = service.validateDNA(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});

        Assertions.assertEquals(true, validation.isMutant());
    }

    @Test
    public void validateDNAForHumanTest() throws Exception {
        DNAValidation validation = service.validateDNA(new String[]{"ATCCGA", "CAGTGC", "AATGTA", "AGAAGG", "CCCCTA", "TCACTG"});

        Assertions.assertEquals(false, !validation.isMutant());
    }
}
