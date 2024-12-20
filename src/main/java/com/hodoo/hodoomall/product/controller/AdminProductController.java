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
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

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
    public ResponseEntity<?> getAllProductList(@ModelAttribute QueryDTO queryDTO){

        try{
            queryDTO.setStatus(null);
            List<ProductDTO> productList = productService.getProductList(queryDTO);
            long totalProducts = productService.getTotalProductCount(queryDTO);
            int totalPageNum = (int) Math.ceil((double) totalProducts/queryDTO.getPageSize());

            return ResponseEntity.ok().body(Map.of("status", "success", "productList", productList, "totalPageNum", totalPageNum));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @PutMapping()
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO){

        try {
            productService.updateProduct(productDTO);
            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id){

        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }
}
