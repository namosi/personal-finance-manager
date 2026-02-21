package manager.service;

import manager.dto.TransactionDTO;
import org.springframework.data.domain.Pageable;

import manager.dto.UserDTO;
import manager.exceptions.BudgetExceededException;
import manager.exceptions.ResourceNotFoundException;
import manager.model.Transaction;
import manager.model.User;
import manager.repository.TransactionRepository;
import manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class FinanceService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public TransactionDTO addTransaction(Long userId, Transaction transaction) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));


        if (user.getMonthlyBudgetLimit() != null) {
            double currentMonthTotal = calculateMonthlyTotal(user, transaction.getDate());
            if (currentMonthTotal + transaction.getAmount() > user.getMonthlyBudgetLimit()) {
                throw new BudgetExceededException("Transaction exceeds monthly budget of " + user.getMonthlyBudgetLimit());
            }
        }

        transaction.setUser(user);
        Transaction saved = transactionRepository.save(transaction);
        return mapToDTO(saved);
    }

    public Page<TransactionDTO> getUserTransactions(Long userId, LocalDate start, LocalDate end, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return transactionRepository
                .findByUserIdAndDateBetween(userId, start, end, pageable)
                .map(this::mapToDTO);
    }
    
    private double calculateMonthlyTotal(User user, LocalDate date) {
        LocalDate start = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = date.with(TemporalAdjusters.lastDayOfMonth());
      
        return transactionRepository.findByUserIdAndDateBetween(user.getId(), start, end, Pageable.unpaged())
                .stream().mapToDouble(Transaction::getAmount).sum();
    }

    private TransactionDTO mapToDTO(Transaction t) {
        return new TransactionDTO(t.getAmount(), t.getCategory(), t.getDate(), t.getDescription());
    }
    

    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public User updateUser(Long userId, UserDTO userDTO) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        existingUser.setName(userDTO.getName());
        existingUser.setMonthlyBudgetLimit(userDTO.getMonthlyBudgetLimit());

        return userRepository.save(existingUser);
    }

    
    public TransactionDTO updateTransaction(
            Long userId,
            Long transactionId,
            Transaction updatedTransaction) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        if (!existingTransaction.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Transaction does not belong to user " + userId);
        }

        // Budget check
        if (user.getMonthlyBudgetLimit() != null) {
            double currentMonthTotal = calculateMonthlyTotal(user, updatedTransaction.getDate())
                    - existingTransaction.getAmount();

            if (currentMonthTotal + updatedTransaction.getAmount() > user.getMonthlyBudgetLimit()) {
                throw new BudgetExceededException(
                        "Updated transaction exceeds monthly budget of " + user.getMonthlyBudgetLimit());
            }
        }

        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setCategory(updatedTransaction.getCategory());
        existingTransaction.setDate(updatedTransaction.getDate());
        existingTransaction.setDescription(updatedTransaction.getDescription());

        Transaction saved = transactionRepository.save(existingTransaction);
        return mapToDTO(saved);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }
    
    public void deleteTransaction(Long userId, Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Transaction does not belong to user " + userId);
        }

        transactionRepository.delete(transaction);
    }
    
    public UserDTO getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToUserDTO(user);
    }
    
    private UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setMonthlyBudgetLimit(user.getMonthlyBudgetLimit());
        return dto;
    }





}
