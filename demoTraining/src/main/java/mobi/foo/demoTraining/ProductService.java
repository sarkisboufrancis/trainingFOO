package mobi.foo.demoTraining;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Async
    public CompletableFuture<List<ProductDTO>> findAll() {
        List<ProductDTO> cacheProducts = (List<ProductDTO>) redisTemplate.opsForValue().get("products");
        if (cacheProducts != null && !cacheProducts.isEmpty()) {
            System.out.println("we used the cache" + cacheProducts);
            return CompletableFuture.completedFuture(cacheProducts);
        }
        List<ProductDTO> allProducts = productRepository.findAll().stream().map(productDTOMapper).collect(Collectors.toList());
        redisTemplate.opsForValue().set("products", allProducts);
        System.out.println("we used database");
        return CompletableFuture.completedFuture(allProducts);
    }

    @Async
    public CompletableFuture<Optional<ProductDTO>> findByIdAsync(Long id) throws InterruptedException {
        String cacheKey = "product_" + id;
        ProductDTO cachedProduct = (ProductDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedProduct != null) {
            System.out.println("We used the cache for product ID: " + id);
            Thread.sleep(1000);
            return CompletableFuture.completedFuture(Optional.of(cachedProduct));
        }
        Optional<Product> product = productRepository.findById(id);
        Optional<ProductDTO> productDTO = product.map(productDTOMapper);
        productDTO.ifPresent(p -> redisTemplate.opsForValue().set(cacheKey, p));
        System.out.println("We used the database for product ID: " + id);
        return CompletableFuture.completedFuture(productDTO);
    }

    @Async
    public CompletableFuture<Product> saveAsync(Product product) {
        Product savedProduct = productRepository.save(product);
        redisTemplate.delete("products"); // Clear the products cache
        redisTemplate.delete("product_" + savedProduct.getId()); // Clear the specific product cache
        return CompletableFuture.completedFuture(savedProduct);
    }

    @Async
    public CompletableFuture<Void> deleteAsync(Long id) {
        productRepository.deleteById(id);
        redisTemplate.delete("products"); // Clear the products cache
        redisTemplate.delete("product_" + id); // Clear the specific product cache
        return CompletableFuture.completedFuture(null);
    }
}