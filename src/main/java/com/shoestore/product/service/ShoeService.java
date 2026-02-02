package com.shoestore.product.service;

import com.shoestore.common.exceptions.ResourceNotFoundException;
import com.shoestore.product.dto.ShoeRequest;
import com.shoestore.product.dto.ShoeResponse;
import com.shoestore.product.entity.Shoe;
import com.shoestore.product.repository.ShoeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoeService {
    private final ShoeRepository shoeRepository;

    public ShoeService(ShoeRepository shoeRepository) {
        this.shoeRepository = shoeRepository;
    }

    public ShoeResponse createShoe(ShoeRequest request){
        Shoe shoe=Shoe.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .description(request.getDescription())
                .stock(request.getStock())
                .price(request.getPrice())
                .build();
        Shoe saved=shoeRepository.save(shoe);
        return mapToResponse(saved);
    }
    public ShoeResponse updateShoe(Long id,ShoeRequest shoeRequest){
        Shoe shoe=shoeRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("shoe not found")
        );
        shoe.setName(shoeRequest.getName());
        shoe.setBrand(shoeRequest.getBrand());
        shoe.setDescription(shoeRequest.getDescription());
        shoe.setPrice(shoeRequest.getPrice());
        shoe.setStock(shoeRequest.getStock());

        return mapToResponse(shoeRepository.save(shoe));
    }

    public void deactivateShoe(Long id){
        Shoe shoe=shoeRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("shoe not found")
        );
        shoe.setActive(false);
        shoeRepository.save(shoe);
    }
    public List<ShoeResponse> getAllActiveShoes(){
        return shoeRepository.findByActiveTrue()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public ShoeResponse getShoeById(Long id) {
        Shoe shoe = shoeRepository.findById(id)
                .filter(Shoe::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe not found"));
        return mapToResponse(shoe);
    }

    private ShoeResponse mapToResponse(Shoe shoe) {
        ShoeResponse res = new ShoeResponse();
        res.setId(shoe.getId());
        res.setName(shoe.getName());
        res.setBrand(shoe.getBrand());
        res.setDescription(shoe.getDescription());
        res.setPrice(shoe.getPrice());
        res.setStock(shoe.getStock());
        return res;
    }

}
