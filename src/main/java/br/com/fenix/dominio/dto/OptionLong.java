package br.com.fenix.dominio.dto;

public  class OptionLong {
    private Long id;
    private String ajuda;

    public OptionLong(Long id, String ajuda) {
        this.id = id;
        this.ajuda = ajuda;
    }
    public OptionLong(Long id, int ajuda) {
        this.id = id;
        this.ajuda = String.valueOf(ajuda);    		
	}
	public Long getId() {
        return id;
    }
    public String getAjuda() {
        return ajuda;
    }
}
