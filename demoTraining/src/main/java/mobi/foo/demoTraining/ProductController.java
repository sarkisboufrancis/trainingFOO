package mobi.foo.demoTraining;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get  all product")
    public ResponseEntity<FooResponse> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get  a product by ID")
    public ResponseEntity<FooResponse> getProductById(@PathVariable("id") Long id) throws InterruptedException {
        return productService.findByIdAsync(id);
    }

    @PostMapping("/add")
    @Operation(summary = "add a product")
    public ResponseEntity<FooResponse> createProduct(@RequestBody @Valid Product product) {
        return productService.saveAsync(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a product by ID")
    public ResponseEntity<FooResponse> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.saveAsync(product);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete  a product by ID")
    public ResponseEntity<FooResponse> deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteAsync(id);

    }
}

