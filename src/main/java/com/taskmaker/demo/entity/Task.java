package com.taskmaker.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min=1, max=255)
    private String name;

    @Min(value = 1, message = "estimate cannot be less than 1")
    @Max(value = 10000, message = "estimate cannot be greater than 10000")
    private float estimatedDurationInHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getEstimatedDurationInHours() {
        return estimatedDurationInHours;
    }

    public void setEstimatedDurationInHours(float estimatedDurationInHours) {
        this.estimatedDurationInHours = estimatedDurationInHours;
    }
}
