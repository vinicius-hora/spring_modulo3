package curso.springboot.springboot.model;

public enum Cargo {
	
	JUNIOR("Júnior"),
	PLENO("Pleno"),
	SENIOR("Senior");
	
	private String nome;
	
	
	private Cargo(String nome) {
		this.setNome(nome);
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name();
	}

}
