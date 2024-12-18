package com.garud.companyms.company.messaging;

import com.garud.companyms.company.CompanyService;
import com.garud.companyms.company.dto.ReviewMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageConsumer {
    private final CompanyService companyService;

    public ReviewMessageConsumer(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage){
        try {
            companyService.updateCompanyRating(reviewMessage);
        } catch (Exception e) {
            // Log the error with a meaningful message
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
