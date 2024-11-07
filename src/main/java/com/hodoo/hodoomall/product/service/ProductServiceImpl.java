package com.hodoo.hodoomall.product.service;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.product.model.dao.ProductRepository;
import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;
import com.hodoo.hodoomall.product.model.dto.StockCheckResultDTO;
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
        if (productDTO.getSku() == null || productDTO.getName() == null || productDTO.getDescription() == null || productDTO.getName().isEmpty() || productDTO.getCategory() == null || productDTO.getCategory().isEmpty() || productDTO.getPrice() <= 0 || productDTO.getStock() == null || productDTO.getStock().isEmpty())
            throw new Exception("필수 입력 사항을 모두 채워주세요.");
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
        try {
            productRepository.deleteById(id);
        } catch (Exception e){
            throw new Exception("상품이 존재하지 않습니다.");
        }
    }

    @Override
    public ProductDTO getProductDetail(String id) throws Exception {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new Exception("상품이 존재하지 않습니다."));
        return new ProductDTO(existingProduct);
    }

    @Override
    public StockCheckResultDTO checkAndUpdateStock(OrderDTO.OrderItemDTO orderItemDTO) throws Exception {

        Product updatedProduct = productRepository.updateStock(orderItemDTO);

        if (updatedProduct == null) {
            Product product = productRepository.findById(orderItemDTO.getProductId().toString()).orElseThrow(() -> new Exception("상품이 존재하지 않습니다."));
            return new StockCheckResultDTO(false, product.getName() + "의 " + orderItemDTO.getSize() + "재고가 부족합니다.");
        } else {
            return new StockCheckResultDTO(true, null);
        }
    }


}
