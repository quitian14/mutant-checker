package org.quitian.mutantchecker.controllers;

import org.quitian.mutantchecker.model.api.requests.ValidateDNARequest;
import org.quitian.mutantchecker.model.entities.DNAValidationStats;
import org.quitian.mutantchecker.services.DNAValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DNAValidationController {

    static final Logger log =
            LoggerFactory.getLogger(DNAValidationController.class);

    @Autowired
    private DNAValidationService dnaValidationService;

    @PostMapping("/mutant")
    public void validateMutant(@RequestBody ValidateDNARequest body) {
        log.info("validating DNA:"+ body.getDna());
        dnaValidationService.validateDNA(body.getDna().toArray(new String[]{}));
    }

    @GetMapping("/stats")
    public DNAValidationStats getStats() {
        log.info("Getting DNA validation statistics:");

        return dnaValidationService.getStats();
    }
}
