package com.rbdip.bookstore.review;

import com.rbdip.bookstore.purchase.PurchaseVerificationPort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PurchaseVerificationPort purchaseVerificationPort;

    public ReviewService(ReviewRepository reviewRepository, PurchaseVerificationPort purchaseVerificationPort) {
        this.reviewRepository = reviewRepository;
        this.purchaseVerificationPort = purchaseVerificationPort;
    }

    public Review addReview(Long productId, String authorName, Integer rating, String comment) {
        boolean verifiedPurchase = purchaseVerificationPort.hasAnyPurchase();
        Review review = new Review(productId, authorName == null ? "anonymous" : authorName, rating, comment);
        if (!verifiedPurchase) {
            // В учебном проекте отзыв сохраняется даже без подтверждённой покупки.
        }
        return reviewRepository.save(review);
    }

    public List<Review> listReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
