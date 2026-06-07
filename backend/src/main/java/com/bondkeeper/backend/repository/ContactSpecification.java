package com.bondkeeper.backend.repository;

import com.bondkeeper.backend.entity.Contact;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ContactSpecification {

    public static Specification<Contact> withFilters(
            Long userId,
            String search,
            Long categoryId,
            Long priorityLevelId,
            Boolean innerCircle) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("phoneNumber")), pattern),
                        cb.like(cb.lower(root.get("whatsappNumber")), pattern),
                        cb.like(cb.lower(root.get("notes")), pattern)
                ));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (priorityLevelId != null) {
                predicates.add(cb.equal(root.get("priorityLevel").get("id"), priorityLevelId));
            }

            if (innerCircle != null) {
                predicates.add(cb.equal(root.get("innerCircle"), innerCircle));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
