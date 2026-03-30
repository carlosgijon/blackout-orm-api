package com.blackout.api.finance.infrastructure.web;

import com.blackout.api.finance.application.service.FinanceApplicationService;
import com.blackout.api.finance.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    private final FinanceApplicationService service;

    public FinanceController(FinanceApplicationService service) {
        this.service = service;
    }

    // ── Transactions ──────────────────────────────────────────────────────────

    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(BlackoutAuthentication auth) {
        return service.findAllTransactions(auth.getBandId());
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            BlackoutAuthentication auth,
            @Valid @RequestBody CreateTransactionRequest dto) {
        return service.createTransaction(auth.getBandId(), dto);
    }

    @PutMapping("/transactions/{id}")
    public TransactionResponse updateTransaction(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @RequestBody UpdateTransactionRequest dto) {
        return service.updateTransaction(auth.getBandId(), id, dto);
    }

    @DeleteMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(BlackoutAuthentication auth, @PathVariable String id) {
        service.removeTransaction(auth.getBandId(), id);
    }

    // ── Wish List ─────────────────────────────────────────────────────────────

    @GetMapping("/wish-list")
    public List<WishListItemResponse> getWishList(BlackoutAuthentication auth) {
        return service.findAllWishList(auth.getBandId());
    }

    @PostMapping("/wish-list")
    @ResponseStatus(HttpStatus.CREATED)
    public WishListItemResponse createWishListItem(
            BlackoutAuthentication auth,
            @Valid @RequestBody CreateWishListItemRequest dto) {
        return service.createWishListItem(auth.getBandId(), dto);
    }

    @PutMapping("/wish-list/{id}")
    public WishListItemResponse updateWishListItem(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @RequestBody UpdateWishListItemRequest dto) {
        return service.updateWishListItem(auth.getBandId(), id, dto);
    }

    @DeleteMapping("/wish-list/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWishListItem(BlackoutAuthentication auth, @PathVariable String id) {
        service.removeWishListItem(auth.getBandId(), id);
    }

    // ── Initial Balance ───────────────────────────────────────────────────────

    @GetMapping("/initial-balance")
    public InitialBalanceResponse getInitialBalance(BlackoutAuthentication auth) {
        return service.getInitialBalance(auth.getBandId());
    }

    @PutMapping("/initial-balance")
    public InitialBalanceResponse setInitialBalance(
            BlackoutAuthentication auth,
            @RequestBody InitialBalanceRequest dto) {
        return service.setInitialBalance(auth.getBandId(), dto.initialBalance());
    }
}
