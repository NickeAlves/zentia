package com.api.zentia.entity;

import com.api.zentia.enums.IncomeCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "incomes")
public class Income {
    @Id
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;

    private String description;

    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public Income() {
    }

    public Income(String name, IncomeCategory incomeCategory, String description, BigDecimal value, User owner) {
        this.name = name;
        this.description = description;
        this.incomeCategory = incomeCategory;
        this.value = value;
        this.owner = owner;
    }

    @PrePersist
    public void generateId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IncomeCategory getIncomeCategory() {
        return incomeCategory;
    }

    public void setIncomeCategory(IncomeCategory incomeCategory) {
        this.incomeCategory = incomeCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
