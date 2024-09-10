package com.hodoo.hodoomall.product.service;

import com.hodoo.hodoomall.product.model.dao.ProductRepositoty;
import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepositoty productRepositoty;

    @Override
    public void createProduct(ProductDTO productDTO) throws Exception {

        Product newProduct = new Product();
        newProduct.setName(productDTO.getName());
        newProduct.setSku(productDTO.getSku());
        newProduct.setStock(productDTO.getStock());
        newProduct.setCategory(productDTO.getCategory());
        newProduct.setImage(productDTO.getImage());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setStatus(productDTO.getStatus());
        newProduct.setPrice(productDTO.getPrice());

        productRepositoty.save(newProduct);
    }

    @Override
    public List<ProductDTO> getProductList() throws Exception {

        List<Product> data = productRepositoty.findAll();
        System.out.println(data);

        List<ProductDTO> productList = new ArrayList<>();
        for(Product p : data){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setCategory(p.getCategory());
            productDTO.setSku(p.getSku());
            productDTO.setName(p.getName());
            productDTO.setPrice(p.getPrice());
            productDTO.setImage(p.getImage());
            productDTO.setDescription(p.getDescription());
            productDTO.setStatus(p.getStatus());
            productDTO.setStock(p.getStock());
            productList.add(productDTO);
        }

        return productList;
    }
}
