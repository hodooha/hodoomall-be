//package com.hodoo.hodoomall.config;
//
//import com.hodoo.hodoomall.user.model.dto.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class DummyDataLoader {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//    @Autowired
//    private BCryptPasswordEncoder encoder;
//
//    @Bean
//    public CommandLineRunner loadDummyData() {
//        return args -> {
//            List<User> users = new ArrayList<>();
//
//            // test1부터 test1000까지의 사용자 생성
//            for (int i = 1; i <= 1000; i++) {
//                String userName = "test" + i;
//                String userEmail = userName + "@gmail.com";
//                String encodedPassword = encoder.encode(userName); // 비밀번호는 ID와 동일하게 설정
//                User user = new User(userEmail, encodedPassword, userName);
//                users.add(user);
//            }
//
//            // MongoDB에 일괄 삽입
//            mongoTemplate.insertAll(users);
//            System.out.println("Dummy user data inserted successfully!");
//        };
//    }
//}
