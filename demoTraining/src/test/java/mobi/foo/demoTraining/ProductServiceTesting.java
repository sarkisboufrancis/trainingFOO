package mobi.foo.demoTraining;

import mobi.foo.demoTraining.Product.DTO.ProductDTOMapper;
import mobi.foo.demoTraining.Product.Repository.ProductRepository;
import mobi.foo.demoTraining.Product.Service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
@SpringBootTest
class ProductServiceTesting {
    @Mock
    private ProductRepository productRepository;
    private AutoCloseable autoCloseable;
    @Mock
    private ProductService underTest;
    @Mock
    private ProductDTOMapper productDTOMapper;
    @Mock
    private RedisTemplate redisTemplate;
    @Mock
    private ValueOperations valueOperations;
    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Mockito.doNothing().when(valueOperations).set(anyString(), anyString());
    }
    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }
    @Test
    void findallproduct() throws InterruptedException {
        underTest = new ProductService(productRepository, productDTOMapper, redisTemplate);
        underTest.findAll();
        verify(productRepository).findAll();
    }
    //    @Test
//    void saveProduct() {
//        // Your test data
//        String productName = "Test Product";
//        double productPrice = 10.99;
//
//        // Create a ProductDTO instance using the record syntax
//        ProductDTO productDTO = new ProductDTO(null, productName, productPrice);
//
//        // Mock the behavior of the repository
//        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(new Product());
//
//        // Perform the save operation
//        underTest = new ProductService(productRepository, productDTOMapper, redisTemplate);
//        underTest.saveAsync(productDTO);
//
//        // Verify the interaction with the repository
//        verify(productRepository).save(Mockito.any(Product.class));
//    }
    @Test
    void findProductById() throws InterruptedException {
        // Your test data
        Long productId = 1L;

        // Perform the find operation
        underTest = new ProductService(productRepository, productDTOMapper, redisTemplate);
        underTest.findByIdAsync(productId);

        // Verify the interaction with the repository
        verify(productRepository).findById(productId);
    }
    @Test
    void deleteProduct() {
        // Your test data
        Long productId = 1L;

        // Perform the delete operation
        underTest = new ProductService(productRepository, productDTOMapper, redisTemplate);
        underTest.deleteAsync(productId);

        // Verify the interaction with the repository
        verify(productRepository).deleteById(productId);
    }

}