package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.ReviewDto;
import feo.health.catalog_service.entity.Doctor;
import feo.health.catalog_service.entity.DoctorReview;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {

    public ReviewDto toDto(DoctorReview doctorReview) {

        if (doctorReview == null) return null;

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setDate(doctorReview.getDate());
        reviewDto.setText(doctorReview.getText());
        reviewDto.setRating(doctorReview.getRating());

        return reviewDto;
    }

    public DoctorReview toEntity(ReviewDto reviewDto, Doctor doctor) {

        if (reviewDto == null) return null;

        DoctorReview doctorReview = new DoctorReview();
        doctorReview.setText(reviewDto.getText());
        doctorReview.setDate(reviewDto.getDate());
        doctorReview.setRating(reviewDto.getRating());
        doctorReview.setDoctor(doctor);

        return doctorReview;
    }

    public List<DoctorReview> toEntity(List<ReviewDto> reviewDtos, Doctor doctor) {
        return reviewDtos.stream().map(reviewDto -> this.toEntity(reviewDto, doctor)).toList();
    }

    public List<ReviewDto> toDto(List<DoctorReview> doctorReviews) {
        return doctorReviews.stream().map(this::toDto).toList();
    }

}
