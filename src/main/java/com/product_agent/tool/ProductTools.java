package com.product_agent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.product_agent.model.Product;
import com.product_agent.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductTools {

    private final ProductRepository productRepository;

    public ProductTools(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public record StockUpdateRequest(String sku, int newQuantity) {}
    public record CreateProductRequest(String sku, String name, String category, BigDecimal price, int initialStock) {}

    @Tool(description = "Retrieve all products in the database")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Tool(description = "Search for a product by its name (case insensitive matching)")
    public Product searchProductByName(String productName) {
        return productRepository.findByNameContainingIgnoreCase(productName)
                .orElse(null);
    }

    @Tool(description = "Get products whose stock quantity is less than or equal to a given threshold")
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    @Tool(description = "Update the inventory/stock level of an existing product using its SKU")
    @Transactional
    public String updateStockQuantity(StockUpdateRequest request) {
        return productRepository.findBySku(request.sku())
                .map(product -> {
                    int oldQty = product.getStockQuantity();
                    product.setStockQuantity(request.newQuantity());
                    productRepository.save(product);
                    return String.format("Successfully updated stock for '%s' (SKU: %s) from %d to %d.",
                            product.getName(), product.getSku(), oldQty, request.newQuantity());
                })
                .orElseGet(() -> String.format("Error: Product with SKU '%s' was not found.", request.sku()));
    }

    @Tool(description = "Create and register a brand-new product in the inventory")
    @Transactional
    public String createProduct(CreateProductRequest request) {
        if (productRepository.findBySku(request.sku()).isPresent()) {
            return String.format("Error: Product with SKU '%s' already exists.", request.sku());
        }

        Product newProduct = Product.builder()
                .sku(request.sku())
                .name(request.name())
                .category(request.category())
                .price(request.price())
                .stockQuantity(request.initialStock())
                .build();

        Product saved = productRepository.save(newProduct);
        return String.format("Product '%s' created successfully with ID %d.", saved.getName(), saved.getId());
    }
}
