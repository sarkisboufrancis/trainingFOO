package mobi.foo.demoTraining;

import java.io.Serializable;

public record ProductDTO(Long id, String name , Double price) implements Serializable {

}
