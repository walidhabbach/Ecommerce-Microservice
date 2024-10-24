package org.customer.repository;

import org.customer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String > {
    Customer findByEmail(String Email);
}
