package org.quitian.mutantchecker.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quitian.mutantchecker.CommonTest;
import org.quitian.mutantchecker.model.entities.DNAValidationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class DNAValidationControllerTest extends CommonTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void validaDNATestOfHuman() throws Exception {
        String body = "{\n" +
                "    \"dna\":[\"TTGCGT\",\"CAGTAC\",\"TTAAGT\",\"AGAAAG\",\"CCCATA\",\"TCACTG\"]\n" +
                "}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/mutant")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(403, response.getStatus());

        Mockito.verify(queueProducer, Mockito.never()).convertAndSend(
                "dna_validation",
                "{\"dna\":\"[TTGCGT, CAGTAC, TTAAGT, AGAAAG, CCCATA, TCACTG]\",\"mutant\":false}");
    }

    @Test
    public void validaDNATestOfMutant() throws Exception {
        String body = "{\n" +
                "    \"dna\":[\"TTGCGA\",\"CAGTAC\",\"TTAAGT\",\"AGAAAG\",\"CCCATA\",\"TCACTG\"]\n" +
                "}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/mutant")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(200, response.getStatus());

        Mockito.verify(queueProducer).convertAndSend(
                "dna_validations",
                "{\"dna\":\"[TTGCGA, CAGTAC, TTAAGT, AGAAAG, CCCATA, TCACTG]\",\"mutant\":true}");
    }

    @Test
    public void validaDNATestBadSequence() throws Exception {
        String body = "{\n" +
                "    \"dna\":[\"TTGXGA\",\"CAGTAC\",\"TTAAGT\",\"AGAAAG\",\"CCCATA\",\"TCACTG\"]\n" +
                "}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/mutant")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    public void validaDNATestBadLength() throws Exception {
        String body = "{\n" +
                "    \"dna\":[\"TTGGA\",\"CAGTAC\",\"TTAAGT\",\"AGAAAG\",\"CCCATA\",\"TCACTG\"]\n" +
                "}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/mutant")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @Sql("/test/data/get_stats.sql")
    public void getStatsTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/stats")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(200, response.getStatus());

        DNAValidationStats stats = objectMapper.readValue(response.getContentAsString(), DNAValidationStats.class);
        Assertions.assertEquals(100, stats.getCountHumanDna());
        Assertions.assertEquals(100, stats.getCountHumanDna());
        Assertions.assertEquals(0.1, stats.getRatio());
    }
}