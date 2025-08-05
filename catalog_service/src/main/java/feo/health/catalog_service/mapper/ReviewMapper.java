package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.ReviewDto;
import feo.health.catalog_service.entity.Doctor;
import feo.health.catalog_service.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review review) {

        if (review == null) return null;

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setDate(review.getDate());
        reviewDto.setText(review.getText());
        reviewDto.setRating(review.getRating());

        return reviewDto;
    }

    public Review toEntity(ReviewDto reviewDto, Doctor doctor) {

        if (reviewDto == null) return null;

        Review review = new Review();
        review.setText(reviewDto.getText());
        review.setDate(reviewDto.getDate());
        review.setRating(reviewDto.getRating());
        review.setDoctor(doctor);

        return review;
    }

    public List<Review> toEntity(List<ReviewDto> reviewDtos, Doctor doctor) {
        return reviewDtos.stream().map(reviewDto -> this.toEntity(reviewDto, doctor)).toList();
    }

    public List<ReviewDto> toDto(List<Review> reviews) {
        return reviews.stream().map(this::toDto).toList();
    }

}
