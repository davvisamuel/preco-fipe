package fipe.preco.preco_fipe.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fipe.preco.preco_fipe.dto.response.FipeInformationResponse;
import fipe.preco.preco_fipe.mapper.ConsultationProducerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    private final ConsultationProducerMapper consultationProducerMapper;
    private final ObjectMapper objectMapper;


    public void send(Integer userId, Integer comparisonId, FipeInformationResponse fipeInformationResponse, String modelYear) throws JsonProcessingException {

        var consultationProducerRequest = consultationProducerMapper.toConsultationProducerRequest(userId, comparisonId, fipeInformationResponse, modelYear);

        var request = objectMapper.writeValueAsString(consultationProducerRequest);

        rabbitTemplate.convertAndSend(queue.getName(), request);
    }
}
