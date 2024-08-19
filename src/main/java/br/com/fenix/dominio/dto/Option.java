package br.com.fenix.dominio.dto;

public  class Option {
    private String id;
    private String ajuda;

    public Option(String id, String ajuda) {
        this.id = id;
        this.ajuda = ajuda;
    }
    public Option(Long id, String ajuda) {
        this.id = id.toString();
        this.ajuda = ajuda;
    }
    public Option(int id, int ajuda) {
        this.id = Integer.toString(id);
        this.ajuda = Integer.toString(ajuda);
	}
	public String getId() {
        return id;
    }

    public String getAjuda() {
        return ajuda;
    }
}
