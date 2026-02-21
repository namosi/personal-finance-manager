package manager.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double monthlyBudgetLimit;

    //One to many relationship
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();


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
