package com.garud.reviewms.review.impl;


import com.garud.reviewms.review.Review;
import com.garud.reviewms.review.ReviewRepository;
import com.garud.reviewms.review.ReviewService;
import com.garud.reviewms.review.external.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Value("${company.service.url}")
    private String companyServiceUrl;

    private final ReviewRepository reviewRepository;
    private final RestTemplate restTemplate;


    public ReviewServiceImpl(ReviewRepository reviewRepository,RestTemplate restTemplate) {
        this.reviewRepository = reviewRepository;
        this.restTemplate=restTemplate;

    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        // Find the company first to ensure it exists, then retrieve reviews

        Company company = restTemplate.getForObject(
                companyServiceUrl + companyId, Company.class);

        if (company !=null){
            return reviewRepository.findByCompanyId(companyId);
        }else {
           return null;
        }
    }


    @Override
    public boolean create(Long companyId, Review review) {


        if (companyId != null && review != null) {
            review.setCompanyId(companyId); // Set the company in the review
            reviewRepository.save(review);
            return true;// Save and return the created review
        }
        return false; // Return null if company not found
    }

    @Override
    public Review getReview(Long reviewId) {
       return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview( Long reviewId, Review updateReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review != null) {
           review.setTitle(updateReview.getTitle());
           review.setDescription(updateReview.getDescription());
           review.setRating(updateReview.getRating());
            reviewRepository.save(review);
            return true;
        }
        return false; // Return false if review not found
    }

    @Override
    public boolean deleteReviewById(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review !=null) {
            reviewRepository.delete(review);
            return true;

        }
        return false; // Return false if review not found
    }
}
