package org.quitian.mutantchecker.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quitian.mutantchecker.model.entities.DNAValidation;
import org.quitian.mutantchecker.services.DNAValidationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Post-processing of the dna validations.
 */
@Component
public class DNAValidationConsumer {

    @Autowired
    private DNAValidationService dnaValidationService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Receive the message validation from dna_validations to perform the post processing.
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "dna_validations")
    public void receiveNotificationCommand(String message) throws Exception {
        DNAValidation validation = objectMapper.readValue(message, DNAValidation.class);

        dnaValidationService.saveValidation(validation);
    }
}
