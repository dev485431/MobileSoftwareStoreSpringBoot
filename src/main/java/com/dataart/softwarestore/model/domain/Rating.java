package com.dataart.softwarestore.model.domain;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistics_id")
    private Statistics statistics;

    public Rating() {
    }

    public Rating(Float rating) {
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Optional<Float> getRating() {
        return Optional.ofNullable(rating);
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                '}';
    }
}
