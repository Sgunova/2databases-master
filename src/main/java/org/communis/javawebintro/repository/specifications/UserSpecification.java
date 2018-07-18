package org.communis.javawebintro.repository.specifications;

import org.communis.javawebintro.dto.filters.UserFilterWrapper;
import org.communis.javawebintro.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public abstract class UserSpecification implements Specification<User>
{
    private UserSpecification(){}

    public static UserSpecification build(final UserFilterWrapper filter)
    {
        return new UserSpecification()
        {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb)
            {
                final List predicates = new ArrayList();

                if (filter != null)
                {
                    if (filter.getSearch() != null && !filter.getSearch().isEmpty())
                    {
                        predicates.add(cb.or(
                                cb.like(cb.upper(root.get("login")), '%' + filter.getSearch().toUpperCase() + '%'),
                                cb.like(cb.upper(root.get("mail")), '%' + filter.getSearch().toUpperCase() + '%'),
                                cb.like(cb.upper(root.get("name")), '%' + filter.getSearch().toUpperCase() + '%'),
                                cb.like(cb.upper(root.get("surname")), '%' + filter.getSearch().toUpperCase() + '%'),
                                cb.like(cb.upper(root.get("secondName")), '%' + filter.getSearch().toUpperCase() + '%')));
                    }
                    if (filter.getRole() != null) {
                        predicates.add(cb.equal(root.get("role"), filter.getRole()));
                    }
                    if (filter.getStatus() != null) {
                        predicates.add(cb.equal(root.get("status"), filter.getStatus()));
                    }

                }
                return cb.and((Predicate[]) predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}