package mobi.foo.demoTraining;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDTOMapper productDTOMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public List<ProductDTO> findAll() {
        List<ProductDTO> cacheProducts = (List<ProductDTO>) redisTemplate.opsForValue().get("products");
        if (cacheProducts != null && !cacheProducts.isEmpty()) {
            System.out.println("we used the cache" + cacheProducts);
            return cacheProducts;
        }
        List<ProductDTO> allProducts = productRepository.findAll().stream().map(productDTOMapper).collect(Collectors.toList());
        redisTemplate.opsForValue().set("products", allProducts);
        System.out.println("we used database");
        return allProducts;
    }

    public Optional<ProductDTO> findById(Long id) {
        String cacheKey = "product_" + id;
        ProductDTO cachedProduct = (ProductDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedProduct != null) {
            System.out.println("We used the cache for product ID: " + id);
            return Optional.of(cachedProduct);
        }
        Optional<Product> product = productRepository.findById(id);
        Optional<ProductDTO> productDTO = product.map(productDTOMapper);
        productDTO.ifPresent(p -> redisTemplate.opsForValue().set(cacheKey, p));
        System.out.println("We used the database for product ID: " + id);
        return productDTO;
    }

    public Product save(Product product) {
        Product savedProduct = productRepository.save(product);
        redisTemplate.delete("products"); // Clear the products cache
        redisTemplate.delete("product_" + savedProduct.getId()); // Clear the specific product cache
        return savedProduct;
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
        redisTemplate.delete("products"); // Clear the products cache
        redisTemplate.delete("product_" + id); // Clear the specific product cache
    }
}