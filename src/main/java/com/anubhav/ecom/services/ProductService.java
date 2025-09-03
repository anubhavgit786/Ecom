package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.ProductDTO;
import java.util.List;

public interface ProductService
{
    List<ProductDTO> getAllProducts(Byte categoryId);
    ProductDTO createProduct(ProductDTO inputProduct);
    ProductDTO updateProduct(Long id, ProductDTO inputProduct);
    void deleteProduct(Long id);
}
