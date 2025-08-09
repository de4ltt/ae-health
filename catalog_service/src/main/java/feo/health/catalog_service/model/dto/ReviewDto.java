package feo.health.catalog_service.model.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class ReviewDto {

    private String text;

    private Date date;

    private Float rating;
}
