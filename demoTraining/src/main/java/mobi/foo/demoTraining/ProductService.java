package mobi.foo.demoTraining;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDTOMapper productDTOMapper;
    private final RedisTemplate<String, Object> redisTemplate;


    public ResponseEntity<FooResponse> findAll() {
        List<ProductDTO> cacheProducts = (List<ProductDTO>) redisTemplate.opsForValue().get("products");
        if (cacheProducts != null && !cacheProducts.isEmpty()) {
            System.out.println("we used the cache" + cacheProducts);
            CompletableFuture a = CompletableFuture.completedFuture(cacheProducts);
            return new ResponseEntity<>(FooResponse.builder().status(true).message("OK").data(a).build(), HttpStatus.OK);
        }
        List<ProductDTO> allProducts = productRepository.findAll().stream().map(productDTOMapper).collect(Collectors.toList());
        redisTemplate.opsForValue().set("products", allProducts);
        System.out.println("we used database");

        CompletableFuture b = CompletableFuture.completedFuture(allProducts);
        return new ResponseEntity<>(FooResponse.builder().status(true).message("OK").data(b).build(), HttpStatus.OK);
    }


    public ResponseEntity<FooResponse> findByIdAsync(Long id) throws InterruptedException {
        String cacheKey = "product_" + id;
        ProductDTO cachedProduct = (ProductDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedProduct != null) {
            System.out.println("We used the cache for product ID: " + id);
            Thread.sleep(1000);
            CompletableFuture a=CompletableFuture.completedFuture(Optional.of(cachedProduct));
            return new ResponseEntity<>(FooResponse.builder().status(true).message("OK").data(a).build(), HttpStatus.OK);
        }
        Optional<Product> product = productRepository.findById(id);
        Optional<ProductDTO> productDTO = product.map(productDTOMapper);
        productDTO.ifPresent(p -> redisTemplate.opsForValue().set(cacheKey, p));
        System.out.println("We used the database for product ID: " + id);
        CompletableFuture a= CompletableFuture.completedFuture(productDTO);
        return new ResponseEntity<>(FooResponse.builder().status(true).message("OK").data(a).build(), HttpStatus.OK);
    }

    public ResponseEntity<FooResponse> saveAsync(Product product) {
        Product savedProduct = productRepository.save(product);
        redisTemplate.delete("products"); // Clear the products cache
        redisTemplate.delete("product_" + savedProduct.getId()); // Clear the specific product cache
        CompletableFuture c= CompletableFuture.completedFuture(savedProduct);
        return new ResponseEntity<>(FooResponse.builder().status(true).message("OK").data(c).build(), HttpStatus.OK);
    }


    public ResponseEntity<FooResponse> deleteAsync(Long id) {
        productRepository.deleteById(id);
        redisTemplate.delete("products"); // Clear the products cache
        redisTemplate.delete("product_" + id); // Clear the specific product cache
        return new ResponseEntity<>(FooResponse.builder().status(true).message("OK").data("").build(), HttpStatus.OK);
    }
}