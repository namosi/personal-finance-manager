package manager.controller;

import manager.dto.TransactionDTO;
import org.springframework.data.domain.Pageable;
import manager.dto.UserDTO;
import manager.exceptions.ApiError;
import manager.exceptions.BudgetExceededException;
import manager.exceptions.ResourceNotFoundException;
import manager.model.Transaction;
import manager.model.User;
import manager.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class FinanceController {

    @Autowired
    private FinanceService financeService;
    
    //Create Transaction
    @PostMapping
    public User createUser(@RequestBody User user) {
        return financeService.createUser(user);
    }
    
    //Add Transaction
    @PostMapping("/{userId}/transactions")
    public ResponseEntity<TransactionDTO> addTransaction(
            @PathVariable Long userId,
            @Valid @RequestBody Transaction transaction) {
        return new ResponseEntity<>(financeService.addTransaction(userId, transaction), HttpStatus.CREATED);
    }

    //Get Transactions with Filter and Pagination
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<Page<TransactionDTO>> getTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable
    ) {

        //Default to current year if dates are missing
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfYear(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        }

        Page<TransactionDTO> transactions =
                financeService.getUserTransactions(userId, startDate, endDate, pageable);

        return ResponseEntity.ok(transactions);
    }

    
    //Update User
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDTO userDTO) {

        User updated = financeService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updated);
    }
    
     //Update Transaction
    @PutMapping("/{userId}/transactions/{transactionId}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long userId,
            @PathVariable Long transactionId,
            @RequestBody Transaction transaction) {

        TransactionDTO updatedTransaction =
                financeService.updateTransaction(userId, transactionId, transaction);

        return ResponseEntity.ok(updatedTransaction);
    }
    
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        financeService.deleteUser(userId);
    }

    
    @DeleteMapping("/{userId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(
            @PathVariable Long userId,
            @PathVariable Long transactionId) {

        financeService.deleteTransaction(userId, transactionId);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {

        UserDTO user = financeService.getUserById(userId);

        return ResponseEntity.ok(user);
    }
    
    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

            String message = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            ApiError error = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    message
            );

            return ResponseEntity.badRequest().body(error);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(404, ex.getMessage()));
        }

        @ExceptionHandler(BudgetExceededException.class)
        public ResponseEntity<ApiError> handleBudget(BudgetExceededException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(400, ex.getMessage()));
        }
    }


    
}
