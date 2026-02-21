package manager.dto;

import manager.model.Category;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class TransactionDTO {
	
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;
    
    @NotNull(message = "Category is required")
    private Category category;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    private String description;

    public TransactionDTO(Double amount, Category category, LocalDate date, String description) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public Double getAmount() { 
    	return amount; 
    }
    
    public Category getCategory() { 
    	return category; 
    }
    
    public LocalDate getDate() { 
    	return date; 
    }
    
    public String getDescription() { 
    	return description; 
    }
}
