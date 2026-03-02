package com.newproject.review.repository;

import com.newproject.review.domain.ProductReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProductIdOrderByCreatedAtDesc(Long productId);
    List<ProductReview> findByProductIdAndApprovedOrderByCreatedAtDesc(Long productId, Boolean approved);
    List<ProductReview> findByApprovedOrderByCreatedAtDesc(Boolean approved);
}
