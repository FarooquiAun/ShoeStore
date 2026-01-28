package com.shoestore.product.controller;

import com.shoestore.product.dto.ShoeRequest;
import com.shoestore.product.dto.ShoeResponse;
import com.shoestore.product.service.ShoeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ShoeController {
    private final ShoeService shoeService;

    public ShoeController(ShoeService shoeService) {
        this.shoeService = shoeService;
    }
    @GetMapping("/shoes")
    public ResponseEntity<List<ShoeResponse>> getAllShoes(){

        return ResponseEntity.ok(shoeService.getAllActiveShoes());
    }
    @GetMapping("/shoe/{id}")
    public ResponseEntity<ShoeResponse> getShoe(@PathVariable Long id){
        return ResponseEntity.ok(shoeService.getShoeById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/shoes")
    public ResponseEntity<ShoeResponse> addShoe(@RequestBody ShoeRequest request) {
        return ResponseEntity.ok(shoeService.createShoe(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/shoes/{id}")
    public ResponseEntity<ShoeResponse> updateShoe(
            @PathVariable Long id,
            @RequestBody ShoeRequest request) {
        return ResponseEntity.ok(shoeService.updateShoe(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/shoes/{id}")
    public ResponseEntity<Void> deactivateShoe(@PathVariable Long id) {
        shoeService.deactivateShoe(id);
        return ResponseEntity.noContent().build();
    }



}
