package fr.esic.mastering.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_attributes")
public class UserAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String attributeKey;

    @Column(columnDefinition = "TEXT")
    private String attributeValue;

    public UserAttribute() {}

    public UserAttribute(Long userId, String attributeKey, String attributeValue) {
        this.userId = userId;
        this.attributeKey = attributeKey;
        this.attributeValue = attributeValue;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}