package mobi.foo.demoTraining;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get  all product")
    public CompletableFuture<List<ProductDTO>> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get  a product by ID")
    public CompletableFuture<ResponseEntity<ProductDTO>> getProductById(@PathVariable("id") Long id) throws InterruptedException {
        return productService.findByIdAsync(id)
                .thenApply(productDTO -> productDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()));
    }

    @PostMapping("/add")
    @Operation(summary = "add a product")
    public CompletableFuture<Product> createProduct(@RequestBody @Valid Product product) {
        return productService.saveAsync(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a product by ID")
    public CompletableFuture<ResponseEntity<Product>> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.saveAsync(product)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete  a product by ID")
    public CompletableFuture<ResponseEntity<Void>> deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteAsync(id)
                .thenApply(ignore -> ResponseEntity.noContent().build());
    }
}

