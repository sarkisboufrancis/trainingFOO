package mobi.foo.demoTraining.Product.DTO;

import mobi.foo.demoTraining.Product.DTO.ProductDTO;
import mobi.foo.demoTraining.Product.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ProductDTOMapper implements Function<Product, ProductDTO> {

    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

}