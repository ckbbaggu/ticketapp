package com.bagaria.ticketapp8.specification;

import com.bagaria.ticketapp8.entity.Ticket;
import com.bagaria.ticketapp8.entity.TicketStatus;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecification {
    public static Specification<Ticket> hasStatus(TicketStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("status"), status);

        };

    }
}
