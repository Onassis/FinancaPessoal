package br.com.fenix.dominio.modelo;


import java.util.UUID;

public  class Option {
    private String id;
    private String ajuda;

    public Option(UUID id, String ajuda) {
    	if (id == null) {
    		return;
    	}
        this.id = id.toString();
        this.ajuda = ajuda;
    }
    public Option(String id, String ajuda) {
        this.id = id;
        this.ajuda = ajuda;
    }
    public Option(Long id, String ajuda) {
    	if (id == null) {
    		return;
    	}
    		
        this.id = id.toString();
        this.ajuda = ajuda;
    }

    public Option(int id, int ajuda) {
        this.id = String.valueOf(id);
        this.ajuda = String.valueOf(ajuda);    		
        this.id = Integer.toString(id);
        this.ajuda = Integer.toString(ajuda);
	}
	public String getId() {
        return id;
    }
    public String getAjuda() {
        return ajuda;
    }
	@Override
	public String toString() {
		return "Option [id=" + id + ", ajuda=" + ajuda + "]";
	}
    
}

