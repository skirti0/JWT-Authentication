package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProducts() {
        return Arrays.asList(new ProductDTO(1L, "tea"), new ProductDTO(2L, "soup"));
    }
}
