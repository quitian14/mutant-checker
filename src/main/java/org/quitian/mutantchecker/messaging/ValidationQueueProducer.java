package org.quitian.mutantchecker.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quitian.mutantchecker.model.entities.DNAValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Producer to send the validation to post-processing.
 */
@Component
public class ValidationQueueProducer {

  static final Logger log =
          LoggerFactory.getLogger(ValidationQueueProducer.class);

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Send a validation to post-processing.
   * @param validation
   * @throws Exception
   */
  public void saveDNAValidation(DNAValidation validation) {
    try {
      String message = objectMapper.writeValueAsString(validation);
      rabbitTemplate.convertAndSend("dna_validations", message);
    } catch (Exception exc) {
      log.error("Error producing", exc);
    }
  }
}