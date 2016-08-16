package com.dataart.softwarestore.model.domain;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "statistics")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time_uploaded")
    private OffsetDateTime timeUploaded;
    private Long downloads;

    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratings;

    public Statistics() {
    }

    public Statistics(OffsetDateTime timeUploaded, Long downloads) {
        this.timeUploaded = timeUploaded;
        this.downloads = downloads;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OffsetDateTime getTimeUploaded() {
        return timeUploaded;
    }

    public void setTimeUploaded(OffsetDateTime timeUploaded) {
        this.timeUploaded = timeUploaded;
    }

    public Long getDownloads() {
        return downloads;
    }

    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                ", timeUploaded=" + timeUploaded +
                ", downloads=" + downloads +
                ", ratings=" + ratings +
                '}';
    }
}
