package curso.springboot.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import curso.springboot.springboot.model.Pessoa;
import curso.springboot.springboot.repository.PessoaRepositoy;



@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepositoy pessoaRepository;
	
	@GetMapping(value= "/cadastropessoa")
	public String inicio() {
		
		return "cadastro/cadastropessoa";
	}
	
	@PostMapping(value= "/salvarpessoa")
	public String salvar(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		
		return "cadastro/cadastropessoa";
	}

}