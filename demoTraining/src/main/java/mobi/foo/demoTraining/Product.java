package mobi.foo.demoTraining;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="products")
@Data
public class Product  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Product id  cannot be null")
    private Long Id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    @Size(min=2,max=10,message="this must be between 2 and 10 characters")
    private String name;

    private String hide;
}

