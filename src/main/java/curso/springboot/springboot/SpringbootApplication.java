package curso.springboot.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//anotações para localizar as entidades, tempaltes e repositorios do projeto
/*
@EntityScan(basePackages = "curso.springboot.springboot.model")

@EnableJpaRepositories(basePackages = {"curso.springboot.repository"})
@EnableTransactionManagement
*/
@ComponentScan(basePackages = {"curso.*"})
@EnableTransactionManagement
@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
	/*
	@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/login").setViewName("/login");
			registry.setOrder(Ordered.LOWEST_PRECEDENCE);
		}
	*/
	
}

