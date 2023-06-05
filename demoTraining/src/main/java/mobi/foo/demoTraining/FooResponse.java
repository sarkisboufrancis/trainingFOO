package mobi.foo.demoTraining;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FooResponse {

    private  Object data;
    private boolean status;
    private String message;

}
