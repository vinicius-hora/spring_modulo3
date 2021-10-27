package curso.springboot.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.springboot.springboot.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override //configura os acessos por http
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf()
		.disable()//desativa as configurações padrões do spring
		.authorizeRequests()// permite resgringir acessos
		.antMatchers(HttpMethod.GET, "/").permitAll()// qualquer usuário acessa essa página 
		.antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll() // permite qualquer usuário
		.and().logout() //mapeia URL de logout e nvalida usuário
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Override //cria autenticação do usuário com banco de dados
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	
	/*
	@Override //cria autenticação do usuário  em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
		.withUser("vinicius")
		.password("$2a$10$gsyfjNrM2VL6qQsBeKsyiObj.joXqFmAzbLwyeDcj0KtMLAmfU.o2")
		.roles("ADMIN");
	}
	*/
	
	@Override //ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**");
	}

}
