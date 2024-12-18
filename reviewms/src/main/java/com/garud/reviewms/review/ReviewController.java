package com.garud.reviewms.review;


import com.garud.reviewms.review.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }



    // GET /companies/{companyId}/reviews - Retrieve all reviews for a company
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        if (reviews != null && !reviews.isEmpty()) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // GET /companies/{companyId}/reviews/{reviewId} - Retrieve a review by its ID for a specific company
    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // POST /companies/{companyId}/reviews - Create a new review for a company
    @PostMapping
    public ResponseEntity<String> createReview(@RequestParam Long companyId, @RequestBody Review review) {
        boolean reviewRes = reviewService.create(companyId, review);
        if (reviewRes) {
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>("Review Created Successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to create Review",HttpStatus.BAD_REQUEST);
    }

    // PUT /companies/{companyId}/reviews/{reviewId} - Update an existing review
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
                                               @RequestBody Review updateReview) {
        boolean isUpdated = reviewService.updateReview( reviewId, updateReview);
        if (isUpdated) {
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to update Review",HttpStatus.NOT_FOUND);
    }

    // DELETE /companies/{companyId}/reviews/{reviewId} - Delete a review by its ID
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReviewById(@PathVariable Long reviewId) {
        boolean isDeleted = reviewService.deleteReviewById(reviewId);
        if (isDeleted) {
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete Review" ,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/averageRating")
    public Double getReviewAverage(@RequestParam Long companyId){
        List<Review> allReviews = reviewService.getAllReviews(companyId);

        return allReviews.stream().mapToDouble(Review::getRating)
                .average().orElse(0.0);
    }
}