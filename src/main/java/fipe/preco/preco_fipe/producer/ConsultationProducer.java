package fipe.preco.preco_fipe.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fipe.preco.preco_fipe.mapper.ConsultationProducerMapper;
import fipe.preco.preco_fipe.response.FipeInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ConsultationProducerMapper consultationProducerMapper;
    private final ObjectMapper objectMapper;


    public void send(Integer userId, Integer comparisonId, FipeInformationResponse fipeInformationResponse) throws JsonProcessingException {

        var consultationProducerRequest = consultationProducerMapper.toConsultationProducerRequest(userId, comparisonId, fipeInformationResponse);

        rabbitTemplate.convertAndSend(
                "consultation-exchange",
                "consultation-rout-key",
                objectMapper.writeValueAsString(consultationProducerRequest)
        );
    }
}
