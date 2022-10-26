package com.ecommerce.library.repository;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //Tim theo username
    Optional<Customer> findByUsername(String username);

    //tìm customer theo keyword
    @Query("select c from Customer c where c.lastName like %?1% or c.firstName like %?1%")
    List<Customer> searchCustomers(String searchWords);

    //Xóa vĩnh viễn customer
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM customers c WHERE c.customer_id = ?1")
    void harddeleteCustomerById(Long id);

}
