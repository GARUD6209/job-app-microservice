package com.garud.jobms.job.clients;

import com.garud.jobms.job.external.Review;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "review",
             url="${review-service.url}")
public interface ReviewClient {

   @GetMapping("/v1/reviews")
   List<Review> getReviews(@RequestParam("companyId") Long id);
}
