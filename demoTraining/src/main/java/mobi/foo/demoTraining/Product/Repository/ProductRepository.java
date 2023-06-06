package mobi.foo.demoTraining.Product.Repository;

import mobi.foo.demoTraining.Product.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
