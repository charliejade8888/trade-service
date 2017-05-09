package at;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrell.replicant.trade.service.TradeServiceImplTest;
import com.tyrell.replicant.trade.service.model.Trade;
import org.json.JSONException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import java.io.IOException;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@EnableJms
@Configuration
@ComponentScan
public class CucumberTestConfig {

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @JmsListener(destination = "myQueue", containerFactory = "myFactory")
    public void receiveMessage(String tradeString) throws JSONException, IOException, InterruptedException {
        String fieldToReplace = "onQueue";
        Trade receivedTrade = new ObjectMapper().readValue(tradeString, Trade.class);
        setField(StepDefinitions.class, fieldToReplace, receivedTrade);
    }

}
