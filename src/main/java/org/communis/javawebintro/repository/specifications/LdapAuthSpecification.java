package org.communis.javawebintro.repository.specifications;

import org.communis.javawebintro.dto.filters.LdapAuthFilterWrapper;
import org.communis.javawebintro.entity.LdapAuth;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public abstract class LdapAuthSpecification implements Specification<LdapAuth> {

    private LdapAuthSpecification() {

    }

    public static LdapAuthSpecification build(final LdapAuthFilterWrapper filter) {
        return new LdapAuthSpecification() {
            @Override
            public Predicate toPredicate(Root<LdapAuth> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                final List predicates = new ArrayList();

                if (filter != null) {
                    if (filter.getSearch() != null && !filter.getSearch().isEmpty()) {
                        predicates.add(cb.or(
                                cb.like(cb.upper(root.get("name")), '%' + filter.getSearch().toUpperCase() + '%'),
                                cb.like(cb.upper(root.get("address")), '%' + filter.getSearch().toUpperCase() + '%'),
                                cb.like(cb.upper(root.get("port")), '%' + filter.getSearch().toUpperCase() + '%')));
                    }
                    if (filter.getActive() != null) {
                        predicates.add(cb.equal(root.get("active"), filter.getActive()));
                    }
                }
                return cb.and((Predicate[]) predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
