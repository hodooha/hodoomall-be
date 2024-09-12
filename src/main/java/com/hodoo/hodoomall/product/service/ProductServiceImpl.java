package com.hodoo.hodoomall.product.service;

import com.hodoo.hodoomall.product.model.dao.ProductRepository;
import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

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

        productRepository.save(newProduct);
    }

    @Override
    public List<ProductDTO> getProductList(QueryDTO queryDTO) throws Exception {

        List<Product> data = productRepository.findByQuery(queryDTO);
        System.out.println(data);

        List<ProductDTO> productList = new ArrayList<>();
        for(Product p : data){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(p.getId());
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

    @Override
    public long getTotalProductCount(QueryDTO queryDTO) throws Exception {

        return productRepository.getTotalProductCount(queryDTO);
    }

    @Override
    public void updateProduct(ProductDTO productDTO) throws Exception {

        Optional<Product> existingProduct = productRepository.findById(productDTO.getId());

        if(existingProduct.isPresent()){
            Product targetProduct = existingProduct.get();

            targetProduct.setId(productDTO.getId());
            targetProduct.setCategory(productDTO.getCategory());
            targetProduct.setSku(productDTO.getSku());
            targetProduct.setName(productDTO.getName());
            targetProduct.setPrice(productDTO.getPrice());
            targetProduct.setImage(productDTO.getImage());
            targetProduct.setDescription(productDTO.getDescription());
            targetProduct.setStatus(productDTO.getStatus());
            targetProduct.setStock(productDTO.getStock());

            productRepository.save(targetProduct);
        } else{
            throw new Exception("상품이 존재하지 않습니다.");
        }
    }

    @Override
    public void deleteProduct(String id) throws Exception {

        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isPresent()) {
            Product targetProduct = existingProduct.get();

            targetProduct.setDeleted(true);
            productRepository.save(targetProduct);

        } else{
            throw new Exception("상품이 존재하지 않습니다.");
        }
    }
}
