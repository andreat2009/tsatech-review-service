package com.newproject.review.service;

import com.newproject.review.domain.ProductReview;
import com.newproject.review.dto.ProductReviewRequest;
import com.newproject.review.dto.ProductReviewResponse;
import com.newproject.review.events.EventPublisher;
import com.newproject.review.exception.BadRequestException;
import com.newproject.review.exception.NotFoundException;
import com.newproject.review.repository.ProductReviewRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final EventPublisher eventPublisher;

    public ProductReviewService(ProductReviewRepository productReviewRepository, EventPublisher eventPublisher) {
        this.productReviewRepository = productReviewRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<ProductReviewResponse> listByProduct(Long productId, Boolean approvedOnly) {
        if (productId == null || productId <= 0) {
            throw new BadRequestException("Invalid product id");
        }

        List<ProductReview> reviews = approvedOnly == null
            ? productReviewRepository.findByProductIdOrderByCreatedAtDesc(productId)
            : productReviewRepository.findByProductIdAndApprovedOrderByCreatedAtDesc(productId, approvedOnly);

        return reviews.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductReviewResponse> listAll(Boolean approvedOnly) {
        List<ProductReview> reviews = approvedOnly == null
            ? productReviewRepository.findAll()
            : productReviewRepository.findByApprovedOrderByCreatedAtDesc(approvedOnly);

        return reviews.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ProductReviewResponse create(Long productId, ProductReviewRequest request, boolean autoApprove) {
        if (productId == null || productId <= 0) {
            throw new BadRequestException("Invalid product id");
        }

        ProductReview review = new ProductReview();
        review.setProductId(productId);
        review.setAuthorName(request.getAuthorName());
        review.setAuthorEmail(request.getAuthorEmail());
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setText(request.getText());
        review.setApproved(autoApprove);

        OffsetDateTime now = OffsetDateTime.now();
        review.setCreatedAt(now);
        review.setUpdatedAt(now);

        ProductReview saved = productReviewRepository.save(review);
        eventPublisher.publish("PRODUCT_REVIEW_CREATED", "product_review", saved.getId().toString(), toResponse(saved));
        return toResponse(saved);
    }

    @Transactional
    public ProductReviewResponse setApproval(Long reviewId, boolean approved) {
        ProductReview review = productReviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("Product review not found"));

        review.setApproved(approved);
        review.setUpdatedAt(OffsetDateTime.now());
        ProductReview saved = productReviewRepository.save(review);
        eventPublisher.publish("PRODUCT_REVIEW_UPDATED", "product_review", saved.getId().toString(), toResponse(saved));
        return toResponse(saved);
    }

    private ProductReviewResponse toResponse(ProductReview review) {
        ProductReviewResponse response = new ProductReviewResponse();
        response.setId(review.getId());
        response.setProductId(review.getProductId());
        response.setAuthorName(review.getAuthorName());
        response.setAuthorEmail(review.getAuthorEmail());
        response.setRating(review.getRating());
        response.setTitle(review.getTitle());
        response.setText(review.getText());
        response.setApproved(review.getApproved());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        return response;
    }
}
