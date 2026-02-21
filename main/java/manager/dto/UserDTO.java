package manager.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import manager.model.Transaction;

public class UserDTO {
	
	private Long id;

    @NotBlank(message = "Name is required")
    private String name;


    @Positive(message = "Monthly budget must be positive")
    private Double monthlyBudgetLimit;

	private List<Transaction> transactions;

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
    
    public Double getMonthlyBudgetLimit() { 
    	return monthlyBudgetLimit; 
    }
    
    public void setMonthlyBudgetLimit(Double monthlyBudgetLimit) { 
    	this.monthlyBudgetLimit = monthlyBudgetLimit; 
    }
    
    public void setTransactions(List<Transaction> transactions) { 
    	this.transactions = transactions; 
    }
    
}
