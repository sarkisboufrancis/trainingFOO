package mobi.foo.demoTraining.Product.DTO;

import java.io.Serializable;

public record ProductDTO(Long id, String name , Double price) implements Serializable {

}
