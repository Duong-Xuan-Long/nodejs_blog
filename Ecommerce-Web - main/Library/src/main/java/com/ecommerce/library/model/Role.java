package com.ecommerce.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String name;
//    @ManyToMany(mappedBy = "roles")
//    private Collection<Customer> customers;
//
//    public void remove(Customer customer){
//        this.customers.remove(customer);
//        customer.getRoles().remove(this);
//    }
}
