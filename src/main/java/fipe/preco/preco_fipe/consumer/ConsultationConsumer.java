package fipe.preco.preco_fipe.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fipe.preco.preco_fipe.dto.message.ConsultationProducerRequest;
import fipe.preco.preco_fipe.service.ConsultationService;
import fipe.preco.preco_fipe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
@RabbitListener(queues = "queue_consultation")
public class ConsultationConsumer {

    private final ConsultationService consultationService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @RabbitHandler
    public void consume(String request) throws JsonProcessingException {

        var consultationProducerRequest = objectMapper.readValue(request, ConsultationProducerRequest.class);

        var user = userService.findById(consultationProducerRequest.userId());

        var fipeInformationResponse = consultationProducerRequest.fipeInformationResponse();

        var comparisonId = consultationProducerRequest.comparisonId();

        var modelYear = consultationProducerRequest.year();

        consultationService.saveConsultation(user, comparisonId, fipeInformationResponse, modelYear);
    }
}
