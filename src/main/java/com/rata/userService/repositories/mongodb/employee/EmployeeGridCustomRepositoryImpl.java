package com.rata.userService.repositories.mongodb.employee;

import com.rata.userService.models.docs.EmployeeGrid;
import com.rata.userService.records.newRecords.EmployeeSearchCriteria;
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
public class EmployeeGridCustomRepositoryImpl implements EmployeeGridCustomRepository {

    private final MongoTemplate mongoTemplate;

    public EmployeeGridCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<EmployeeGrid> searchEmployees(
            EmployeeSearchCriteria criteria,
            Pageable pageable
    ) {
        Query query = new Query();

        // فیلتر بر اساس شرکت
        if (criteria.organizationId() != null) {
            query.addCriteria(Criteria.where("organizationId").is(criteria.organizationId()));
        }

        // فیلتر بر اساس جنسیت
        if (criteria.gender() != null && !criteria.gender().isBlank()) {
            query.addCriteria(Criteria.where("gender").is(criteria.gender()));
        }

        // جستجوی متنی
        if (criteria.search() != null && !criteria.search().isBlank()) {
            String searchPattern = Pattern.quote(criteria.search().trim());
            Pattern regex = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);

            Criteria searchCriteria = new Criteria().orOperator(
                // جستجو در نام نمایشی
                Criteria.where("displayName").regex(regex),
                // جستجو در نام‌های چندزبانه
                Criteria.where("displayNames.fa").regex(regex),
                Criteria.where("displayNames.en").regex(regex),
                // جستجو در کد ملی
                Criteria.where("nationalCode").regex(regex),
                // جستجو در موبایل
                Criteria.where("mobile").regex(regex),
                // جستجو در ایمیل
                Criteria.where("email").regex(regex),
                // جستجو در شناسه‌ها
                Criteria.where("identifiers.value").regex(regex)
            );

            query.addCriteria(searchCriteria);
        }
        query.with(Sort.by("id").ascending());


        // صفحه‌بندی
        query.with(pageable);

        // اجرای query
        List<EmployeeGrid> content = mongoTemplate.find(query, EmployeeGrid.class);
        // محاسبه تعداد کل
        long total = mongoTemplate.count(query.limit(-1).skip(-1), EmployeeGrid.class);

        return new PageImpl<>(content, pageable, total);
    }
}