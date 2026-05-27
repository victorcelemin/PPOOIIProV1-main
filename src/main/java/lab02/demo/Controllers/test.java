package lab02.demo.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/test")
public class test {
    @GetMapping("/test")
    public String defaula() {
        return "<h1>Hola</h1>";
    }
    
}
