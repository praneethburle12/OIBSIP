package com.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "library_settings")
public class LibrarySettings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer allowedDays = 14; // Default 14 days
    
    @Column(nullable = false)
    private Double finePerDay = 5.0; // Default ₹5 per day
    
    @Column(nullable = false)
    private Integer maxBooksPerUser = 3; // Max books a user can issue
    
    // Constructors
    public LibrarySettings() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getAllowedDays() {
        return allowedDays;
    }
    
    public void setAllowedDays(Integer allowedDays) {
        this.allowedDays = allowedDays;
    }
    
    public Double getFinePerDay() {
        return finePerDay;
    }
    
    public void setFinePerDay(Double finePerDay) {
        this.finePerDay = finePerDay;
    }
    
    public Integer getMaxBooksPerUser() {
        return maxBooksPerUser;
    }
    
    public void setMaxBooksPerUser(Integer maxBooksPerUser) {
        this.maxBooksPerUser = maxBooksPerUser;
    }
}
