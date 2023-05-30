package mobi.foo.demoTraining;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String name;

    private String hide;


}

