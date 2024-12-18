package com.garud.reviewms.review;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews(Long companyId);

    Review getReview(Long reviewId);

    boolean create(Long companyId, Review review);

    boolean updateReview( Long reviewId, Review updateReview);

    boolean deleteReviewById(Long reviewId);
}
