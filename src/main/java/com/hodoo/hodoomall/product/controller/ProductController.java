package com.hodoo.hodoomall.product.controller;

import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;
import com.hodoo.hodoomall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO){

        try {
            productService.createProduct(productDTO);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getProductList(@ModelAttribute QueryDTO queryDTO){
        System.out.println(queryDTO);

        try{
            List<ProductDTO> productList = productService.getProductList(queryDTO);
            long totalProducts = productService.getTotalProductCount(queryDTO);
            int totalPageNum = (int) Math.ceil((double) totalProducts/queryDTO.getPageSize());

            return ResponseEntity.ok().body(Map.of("status", "success", "productList", productList, "totalPageNum", totalPageNum));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

}
