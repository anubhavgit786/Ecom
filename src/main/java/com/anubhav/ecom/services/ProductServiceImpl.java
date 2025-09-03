package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.ProductDTO;
import com.anubhav.ecom.exceptions.BadRequestException;
import com.anubhav.ecom.exceptions.ResourceNotFoundException;
import com.anubhav.ecom.models.Category;
import com.anubhav.ecom.models.Product;
import com.anubhav.ecom.repositories.CategoryRepository;
import com.anubhav.ecom.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductDTO> getAllProducts(Byte categoryId)
    {
        List<Product> products;

        if(categoryId != null)
        {
            products = productRepository.findByCategoryId(categoryId);
        }
        else
        {
            products = productRepository.findAllWithCategory();
        }


        return products
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO inputProduct)
    {
        Category category = categoryRepository.findById(inputProduct.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category does not exist for id : " + inputProduct.getCategoryId()));

        Product product = modelMapper.map(inputProduct, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO inputProduct)
    {
        // Step 1: Find product from DB
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id : " + id));

        // Step 2: Map fields from DTO → existingProduct
        // This will overwrite fields present in DTO
        modelMapper.map(inputProduct, existingProduct);
        existingProduct.setId(id);

        // Step 3: Handle category manually (since DTO only has categoryId)
        if (inputProduct.getCategoryId() != null) {
            Category category = categoryRepository.findById(inputProduct.getCategoryId())
                    .orElseThrow(() -> new BadRequestException("Category does not exist for id : " + inputProduct.getCategoryId()));
            existingProduct.setCategory(category);
        }

        // Step 4: Save product
        Product savedProduct = productRepository.save(existingProduct);

        // Step 5: Return DTO
        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public void deleteProduct(Long id)
    {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id : " + id));
        productRepository.deleteById(id);
    }


}
