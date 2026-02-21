package manager.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    private LocalDate date;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Transaction() {}
    
    public Transaction(Double amount, Category category, LocalDate date, String description, User user) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.user = user;
    }

    public Long getId() { 
    	return id; 
    }
    
    public void setId(Long id) { 
    	this.id = id; 
    }
    
    public Double getAmount() { 
    	return amount; 
    }
    
    public void setAmount(Double amount) { 
    	this.amount = amount; 
    }
    
    public Category getCategory() { 
    	return category; 
    }
    
    public void setCategory(Category category) {
    	this.category = category; 
    }
    
    public LocalDate getDate() { 
    	return date; 
    }
    
    public void setDate(LocalDate date) {
    	this.date = date; 
    }
    
    public String getDescription() { 
    	return description; 
    }
    
    public void setDescription(String description) { 
    	this.description = description; 
    }
    
    public User getUser() { 
    	return user; 
    }
    
    public void setUser(User user) { 
    	this.user = user; 
    }
    
}
