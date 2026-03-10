package com.shoestore.product.specification;

import com.shoestore.product.entity.Shoe;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ShoeSpecification {
    public static Specification<Shoe> hasBrand(String brand){
        return ((root, query, criteriaBuilder) -> brand==null ?null:criteriaBuilder.equal(root.get("brand"),brand));
    }
    public static Specification<Shoe> minPrice(BigDecimal minPrice){
        return ((root, query, criteriaBuilder) -> minPrice==null?null:criteriaBuilder.greaterThanOrEqualTo(root.get("price"),minPrice));
    }

    public static Specification<Shoe> maxPrice(BigDecimal maxPrice){
        return ((root, query, criteriaBuilder) -> maxPrice==null?null:criteriaBuilder.lessThanOrEqualTo(root.get("price"),maxPrice));
    }
    public static Specification<Shoe> nameContains(String name){
        return ((root, query, criteriaBuilder) ->
                name==null?null:criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase()+"%"));
    }
    public static Specification<Shoe> isActive(){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("active")));
    }
}
