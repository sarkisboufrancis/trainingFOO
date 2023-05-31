package mobi.foo.demoTraining;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Product id  cannot be null")
    private Long Id;

    @Column(nullable = false)
    @DecimalMin(value = "0.0")
    private double price;

    @Column(nullable = false)
    @Size(min=2,max=10,message="this must be between 2 and 10 characters")
    private String name;

    private String hide;
}

