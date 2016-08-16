package com.dataart.softwarestore.model.domain;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "programs")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private String img128;
    private String img512;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "statistics_id")
    private Statistics statistics;

    public Program() {
    }

    public Program(String name, String description, String img128, String img512, Category category, Statistics
            statistics) {
        this.name = name;
        this.description = description;
        this.img128 = img128;
        this.img512 = img512;
        this.category = category;
        this.statistics = statistics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Optional<String> getImg128() {
        return Optional.ofNullable(img128);
    }

    public void setImg128(String img128) {
        this.img128 = img128;
    }

    public Optional<String> getImg512() {
        return Optional.ofNullable(img512);
    }

    public void setImg512(String img512) {
        this.img512 = img512;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", img128='" + img128 + '\'' +
                ", img512='" + img512 + '\'' +
                ", category=" + category +
                ", statistics=" + statistics +
                '}';
    }
}
