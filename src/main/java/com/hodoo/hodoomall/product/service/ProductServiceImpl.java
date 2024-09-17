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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductDTO productDTO) throws Exception {

        productRepository.save(productDTO.toEntity());
    }

    @Override
    public List<ProductDTO> getProductList(QueryDTO queryDTO) throws Exception {

        List<Product> data = productRepository.findByQuery(queryDTO);
        System.out.println(data);

        List<ProductDTO> productList = new ArrayList<>();
        for (Product p : data) {

            productList.add(new ProductDTO(p));
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

        if (existingProduct.isPresent()) {
            Product targetProduct = productDTO.toEntity();
            targetProduct.setId(productDTO.getId());
            productRepository.save(targetProduct);
        } else {
            throw new Exception("상품이 존재하지 않습니다.");
        }
    }

    @Override
    public void deleteProduct(String id) throws Exception {

        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product targetProduct = existingProduct.get();

            targetProduct.setDeleted(true);
            productRepository.save(targetProduct);

        } else {
            throw new Exception("상품이 존재하지 않습니다.");
        }
    }

    @Override
    public ProductDTO getProductDetail(String id) throws Exception {

        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            return new ProductDTO(product);
        } else {
            throw new Exception("상품이 존재하지 않습니다.");
        }


    }
}
