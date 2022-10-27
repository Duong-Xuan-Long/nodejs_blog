package com.ecommerce.library.model;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_cart_id")
    private Long id;
    private int totalItems;
    private double totalPrices;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

   // ,,CascadeType.REMOVE  Xóa cha thì xóa con
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "cart", fetch = FetchType.EAGER)
    private Set<CartItem> cartItem;

    //@PreRemove
   // void delete(){
       // this.customer = null;
        //Nếu xóa shoppingcart mà muốn giữ laijcartitem thì làm như này
//        this.cartItem.forEach(c->c.setCart(null));
//        this.cartItem.clear() ;
    //}
}