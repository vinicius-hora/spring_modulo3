package curso.springboot.springboot.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.springboot.model.Pessoa;


@Repository
@Transactional
public interface PessoaRepositoy extends CrudRepository<Pessoa, Long> {
	
	@Query("select p from Pessoa p where p.nome like %?1% ")
	List<Pessoa> findPessoaByName(String nome);
	
	@Query("select p from Pessoa p where p.nome like %?1% and p.sexopessoa = ?2 ")
	List<Pessoa> findPessoaByNameSexo(String nome, String sexopessoa);
	

}
