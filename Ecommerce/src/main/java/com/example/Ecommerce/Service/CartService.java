package com.example.Ecommerce.Service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.Ecommerce.Exception.CartNotFoundException;
import com.example.Ecommerce.Mapper.CartMapper;
import com.example.Ecommerce.Repository.CartItemRepository;
import com.example.Ecommerce.Repository.CartRepository;
import com.example.Ecommerce.Request.CartDto;
import com.example.Ecommerce.model.Cart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements CartInterface{

	private final CartRepository cartrepository;
	
	private final CartItemRepository cartitemrepository;
	
	private final CartMapper cartmapper;
	
	@Override
	public CartDto getCart(Long id) {
	Cart cart=cartrepository.findById(id)
			.orElseThrow(()-> new CartNotFoundException("Cart Not found"));
	
        cart.setTotalAmount(calculateTotalAmount(cart));
		return cartmapper.toDto(cart);
	}

	@Override
	public void clearCart(Long id) {
		Cart cart=cartrepository.findById(id)
				.orElseThrow(()-> new CartNotFoundException("Cart Not found"));
		
		cartitemrepository.deleteAllByCartId(id);
		cart.setTotalAmount(BigDecimal.ZERO);
		cartrepository.save(cart);
	}
	
	
	private BigDecimal calculateTotalAmount(Cart cart) {
	    return cart.getCartItems().stream()
	        .map(e -> e.getTotalPrice())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		Cart cart=cartrepository.findById(id)
				.orElseThrow(()->new CartNotFoundException("Cart Not found"));
		
		return calculateTotalAmount(cart);
		
		
	
	}

}
