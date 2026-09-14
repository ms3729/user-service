package com.rata.userService.repositories.mongodb.role;

import com.rata.userService.models.docs.RoleGrid;
import com.rata.userService.records.newRecords.RoleSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class RoleGridCustomRepositoryImpl implements RoleGridCustomRepository {

    private final MongoTemplate mongoTemplate;

    public RoleGridCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<RoleGrid> searchRoles(
            RoleSearchCriteria criteria,
            Pageable pageable
    ) {
        Query query = new Query();

        // فیلتر بر اساس اپلیکیشن
        if (criteria.appId() != null) {
            query.addCriteria(Criteria.where("appId").is(criteria.appId()));
        }

        // فیلتر بر اساس systemRole
        if (criteria.systemRole() != null) {
            query.addCriteria(Criteria.where("systemRole").is(criteria.systemRole()));
        }

        // فیلتر بر اساس وضعیت
        if (criteria.status() != null) {
            query.addCriteria(Criteria.where("status").is(criteria.status()));
        }

        // جستجوی متنی
        if (criteria.search() != null && !criteria.search().isBlank()) {
            String searchPattern = Pattern.quote(criteria.search().trim());
            Pattern regex = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);

            Criteria searchCriteria = new Criteria().orOperator(
                // جستجو در نام نقش
                Criteria.where("name").regex(regex),
                // جستجو در کد نقش
                Criteria.where("code").regex(regex),
                // جستجو در توضیحات
                Criteria.where("description").regex(regex)
            );

            query.addCriteria(searchCriteria);
        }
        
        query.with(Sort.by("roleId").ascending());

        // صفحه‌بندی
        query.with(pageable);

        // اجرای query
        List<RoleGrid> content = mongoTemplate.find(query, RoleGrid.class);
        // محاسبه تعداد کل
        long total = mongoTemplate.count(query.limit(-1).skip(-1), RoleGrid.class);

        return new PageImpl<>(content, pageable, total);
    }
}
