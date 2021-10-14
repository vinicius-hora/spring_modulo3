package curso.springboot.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.springboot.model.Pessoa;
import curso.springboot.springboot.repository.PessoaRepositoy;



@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepositoy pessoaRepository;
	
	@GetMapping(value= "/cadastropessoa")
	public ModelAndView inicio() {
		ModelAndView modelandView = new  ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoaObj", new Pessoa());
		
		return modelandView;
	}
	
	@PostMapping(value= "**/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		ModelAndView andView = new  ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIt);
		andView.addObject("pessoaObj", new Pessoa());
		return andView;
	}
	
	@GetMapping(value= "/listapessoas")
	public ModelAndView pessoas () {
		ModelAndView andView = new  ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIt);
		andView.addObject("pessoaObj", new Pessoa());
		return andView;
		
	}
	
	@GetMapping(value= "/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		ModelAndView modelandView = new  ModelAndView("cadastro/cadastropessoa");
		
		modelandView.addObject("pessoaObj", pessoa.get());
		return modelandView;
		
	}
	
	@GetMapping(value= "/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView modelandView = new  ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoaObj",new Pessoa());
		modelandView.addObject("pessoas", pessoaRepository.findAll());
		return modelandView;
		
	}
	
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		
		ModelAndView modelandView = new  ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
		modelandView.addObject("pessoaObj",new Pessoa());
		return modelandView;
		
	}
	

}
