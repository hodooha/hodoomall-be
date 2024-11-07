package com.hodoo.hodoomall.product.model.dao;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Product> findByQuery(QueryDTO queryDTO) {
        Query query = new Query();

        if(queryDTO.getName() != null && !queryDTO.getName().isEmpty()){
            query.addCriteria(Criteria.where("name").regex(queryDTO.getName(), "i"));
        }

        if(queryDTO.getCategory() != null && !queryDTO.getCategory().isEmpty()){
            query.addCriteria(Criteria.where("category").regex(queryDTO.getCategory(), "i"));
        }

        if(queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()){
            query.addCriteria(Criteria.where("status").is(queryDTO.getStatus()));
        }

        int page = queryDTO.getPage();
        int pageSize = queryDTO.getPageSize();
        query.skip((long) (page -1) * pageSize).limit(pageSize);

        return mongoTemplate.find(query, Product.class);
    }

    @Override
    public long getTotalProductCount(QueryDTO queryDTO) {
        Query query = new Query();

        if(queryDTO.getName() != null && !queryDTO.getName().isEmpty()){
            query.addCriteria(Criteria.where("name").regex(queryDTO.getName(), "i"));
        }

        if(queryDTO.getCategory() != null && !queryDTO.getCategory().isEmpty()){
            query.addCriteria(Criteria.where("category").regex(queryDTO.getCategory(), "i"));
        }

        if(queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()){
            query.addCriteria(Criteria.where("status").is("active"));
        }

        return mongoTemplate.count(query, Product.class);
    }

    @Override
    public Product updateStock(OrderDTO.OrderItemDTO orderItemDTO){
        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("_id").is(orderItemDTO.getProductId().toString()));
        query.addCriteria(Criteria.where("stock." +orderItemDTO.getSize()).gte(orderItemDTO.getQty()));

        update.inc("stock."+orderItemDTO.getSize(), -orderItemDTO.getQty());

        return mongoTemplate.findAndModify(query, update, Product.class);

    }

}
