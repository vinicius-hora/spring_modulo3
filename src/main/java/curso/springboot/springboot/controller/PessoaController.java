package curso.springboot.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.springboot.model.Pessoa;
import curso.springboot.springboot.model.Telefone;
import curso.springboot.springboot.repository.PessoaRepositoy;
import curso.springboot.springboot.repository.ProfissaoRepository;
import curso.springboot.springboot.repository.TelefoneRepository;
import curso.springboot.springboot.service.ReportUtil;



@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepositoy pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private ReportUtil reportUtil;
	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@GetMapping(value= "/cadastropessoa")
	public ModelAndView inicio() {
		ModelAndView modelandView = new  ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoaObj", new Pessoa());
		Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		modelandView.addObject("pessoas", pessoaIt);
		modelandView.addObject("profissoes", profissaoRepository.findAll());
		
		return modelandView;
	}
	
	@PostMapping(value= "**/salvarpessoa",
			consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult,
			final MultipartFile file) throws IOException {
		
		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		if(bindingResult.hasErrors()) {
			ModelAndView andView = new  ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			andView.addObject("pessoas", pessoaIt);
			andView.addObject("pessoaObj", pessoa);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				//erro vem das anotações do modelo pessoa
				msg.add(objectError.getDefaultMessage());
			}
			andView.addObject("msg", msg);			
			return andView;
		}
		else {
			//salvar arquivo no banco de dados
			if(file.getSize() > 0) {
				pessoa.setCurriculo(file.getBytes());
				pessoa.setTipoFileCurriculo(file.getContentType());
				pessoa.setNomeFileCurriculo(file.getOriginalFilename());
			}
			else {
				// verifica se já tem arquivo no banco para não perder em uma edição
				if(pessoa.getId() != null && pessoa.getId()> 0) {
					Pessoa pessoatemp = pessoaRepository.findById(pessoa.getId()).get();
					
					pessoa.setCurriculo(pessoatemp.getCurriculo());
					pessoa.setTipoFileCurriculo(pessoatemp.getTipoFileCurriculo());
					pessoa.setNomeFileCurriculo(pessoatemp.getNomeFileCurriculo());
				}
			}
			pessoaRepository.save(pessoa);
			ModelAndView andView = new  ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			andView.addObject("pessoas", pessoaIt);
			andView.addObject("pessoaObj", new Pessoa());
			return andView;
		}
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
		modelandView.addObject("profissoes", profissaoRepository.findAll());
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
	
	@PostMapping(value= "**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa, 
			@RequestParam("pesquisasexo") String pesquisasexo) {
		
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		
		if (pesquisasexo != null && !pesquisasexo.isEmpty()) {
			pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa, pesquisasexo);
		}
		else {
			pessoas = pessoaRepository.findPessoaByName(nomepesquisa);
		}
		
		ModelAndView modelandView = new  ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoas", pessoas);
		modelandView.addObject("pessoaObj",new Pessoa());
		return modelandView;
		
	}
	//get para imprimir/gerar relatório
	@GetMapping(value= "**/pesquisarpessoa")
	public void imprimePDF(@RequestParam("nomepesquisa") String nomepesquisa, 
			@RequestParam("pesquisasexo") String pesquisasexo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		
		//busca por nome esexo
		if (pesquisasexo != null && !pesquisasexo.isEmpty()
				&& nomepesquisa != null && !nomepesquisa.isEmpty() ) {
			
			
			pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa, pesquisasexo);
			
		}
		//busca por nome
		else if(nomepesquisa != null && !nomepesquisa.isEmpty()) {
			
			pessoas = pessoaRepository.findPessoaByName(nomepesquisa);
		}
		//busca por sexo
		else if(pesquisasexo != null && !pesquisasexo.isEmpty()) {
			
			pessoas = pessoaRepository.findPessoaBySexo(pesquisasexo);
		}
		else {//busca todos
			Iterable<Pessoa> iterator = pessoaRepository.findAll();
			for( Pessoa pessoa : iterator) {
				pessoas.add(pessoa);
			}
		}
		
		//chamar o serviço que gera o relatório
		byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
		
		//tamanho da resposta
		response.setContentLength(pdf.length);
		
		//definir na resposta o tipo do arquivo
		response.setContentType("application/octet-stream");
		
		//cabeçalho de resposta
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		//finalizar a resposta
		response.getOutputStream().write(pdf);
		
		
	}
	
	@GetMapping(value= "/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		ModelAndView modelandView = new  ModelAndView("cadastro/telefones");
		
		modelandView.addObject("pessoaObj", pessoa.get());
		modelandView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		return modelandView;
		
	}
	
	@PostMapping("**/addfonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {
		
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		
		if (telefone != null
				&& telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaObj",pessoa);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			
			List<String> msg = new ArrayList<String>();
			if(telefone.getNumero().isEmpty()) {
				msg.add("O número deve ser informado");
			}
			
			if(telefone.getTipo().isEmpty()) {
				msg.add("O tipo deve ser informado");
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
			
		}
		
		
		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj",pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		return modelAndView;
		
		
	}
	
	@GetMapping(value= "/removertelefone/{idtelefone}")
	public ModelAndView removerTelefone(@PathVariable("idtelefone") Long idtelefone) {
		
		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		telefoneRepository.deleteById(idtelefone);
		
		ModelAndView modelAndView = new  ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj",pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
		return modelAndView;
		
	}
	
	
	
	
	
	
	
	

}
