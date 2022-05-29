package org.quitian.mutantchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CommonTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    @MockBean
    protected RabbitTemplate queueProducer;

    @BeforeEach
    protected void beforeEach() {
        RedisConnection connection = jedisConnectionFactory.getConnection();
        connection.flushAll();
        connection.close();

        Mockito.reset(queueProducer);

        Mockito.doNothing().when(queueProducer).convertAndSend(
                Mockito.any(),
                Mockito.<Object>any());
    }
}
