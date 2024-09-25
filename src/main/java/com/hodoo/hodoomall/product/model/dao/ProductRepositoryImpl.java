package com.hodoo.hodoomall.product.model.dao;

import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

        if(!queryDTO.isDeleted()){
            query.addCriteria(Criteria.where("isDeleted").is(false));
        }

        if(queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()){
            query.addCriteria(Criteria.where("status").is("active"));
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

        if(!queryDTO.isDeleted()){
            query.addCriteria(Criteria.where("isDeleted").is(false));
        }

        if(queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()){
            query.addCriteria(Criteria.where("status").is("active"));
        }

        return mongoTemplate.count(query, Product.class);
    }

}
