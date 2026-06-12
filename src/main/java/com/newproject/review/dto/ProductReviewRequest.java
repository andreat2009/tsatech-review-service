package com.newproject.review.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductReviewRequest {
    @NotBlank
    @Size(max = 128)
    private String authorName;

    @Email
    @Size(max = 255)
    private String authorEmail;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    // SECURITY: cap di lunghezza anti DoS/DB-bloat (defense-in-depth; il render e' gia' escaped).
    @Size(max = 255)
    private String title;
    @Size(max = 5000)
    private String text;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
