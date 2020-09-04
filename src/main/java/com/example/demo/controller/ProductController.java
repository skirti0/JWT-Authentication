package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProduct() {
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(productService.getProducts());
    }

}
