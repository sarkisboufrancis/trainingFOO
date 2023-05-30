package mobi.foo.demoTraining;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDTOMapper productDTOMapper;
    @Cacheable("Product")
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(productDTOMapper).collect(Collectors.toList());
    }
    @Cacheable(value="Product",key = "#id")
    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id).map(productDTOMapper);
    }
    @CachePut(value = "Product",key = "#product.id")
    public Product save(Product product) {
        return productRepository.save(product);
    }
    @CacheEvict(value = "Product",key = "#id")
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}