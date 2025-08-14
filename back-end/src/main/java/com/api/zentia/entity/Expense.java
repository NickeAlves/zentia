package com.api.zentia.entity;

import com.api.zentia.enums.ExpenseCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    private String description;

    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public Expense() {
    }

    public Expense(String name, ExpenseCategory expenseCategory, String description, BigDecimal value, User owner) {
        this.name = name;
        this.expenseCategory = expenseCategory;
        this.description = description;
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

    public ExpenseCategory getCategory() {
        return expenseCategory;
    }

    public void setCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
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
