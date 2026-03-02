package com.newproject.review.controller;

import com.newproject.review.dto.ProductReviewRequest;
import com.newproject.review.dto.ProductReviewResponse;
import com.newproject.review.service.ProductReviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog")
public class ProductReviewController {
    private final ProductReviewService productReviewService;

    public ProductReviewController(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    @GetMapping("/products/{productId}/reviews")
    public List<ProductReviewResponse> listByProduct(
        @PathVariable Long productId,
        @RequestParam(required = false) Boolean approved
    ) {
        return productReviewService.listByProduct(productId, approved != null ? approved : true);
    }

    @PostMapping("/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductReviewResponse create(
        @PathVariable Long productId,
        @Valid @RequestBody ProductReviewRequest request,
        Authentication authentication
    ) {
        boolean autoApprove = authentication != null
            && AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("ROLE_ADMIN");
        return productReviewService.create(productId, request, autoApprove);
    }

    @GetMapping("/reviews")
    public List<ProductReviewResponse> listAll(@RequestParam(required = false) Boolean approved) {
        return productReviewService.listAll(approved);
    }

    @PatchMapping("/reviews/{id}/approval")
    public ProductReviewResponse setApproval(@PathVariable Long id, @RequestParam boolean approved) {
        return productReviewService.setApproval(id, approved);
    }
}
