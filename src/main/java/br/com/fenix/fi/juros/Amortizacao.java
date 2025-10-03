package br.com.fenix.fi.juros;

public class Amortizacao {

	private int periodo=0; 
	private double juroMes=0;
	private double amortizado=0;
	private double prestacao=0 ;
	private double saldoDevedor=0;
	
	
	
	public Amortizacao() {
		super();
		Clear();
	}
	public void Clear() {
		periodo=0; 
		juroMes=0;
		amortizado=0;
		prestacao =0;
		saldoDevedor=0;
	}
	
	
	public Amortizacao(int periodo, double juroMes, double amortizado, double prestacao, double saldoDevedor) {
		super();
		this.periodo = periodo;
		this.juroMes = juroMes;
		this.amortizado = amortizado;
		this.prestacao = prestacao;
		this.saldoDevedor = saldoDevedor;
	}



	@Override
	public String toString() {
		return "Amortizacao [periodo=" + periodo + ", juroMes=" + juroMes + ", amortizado=" + amortizado
				+ ", prestacao=" + prestacao + ", saldoDevedor=" + saldoDevedor + "]";
	}


	public int getPeriodo() {
		return periodo;
	}


	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}


	public double getJuroMes() {
		return juroMes;
	}


	public void setJuroMes(double juroMes) {
		this.juroMes = juroMes;
	}


	public double getAmortizado() {
		return amortizado;
	}


	public void setAmortizado(double amortizado) {
		this.amortizado = amortizado;
		this.juroMes    = ( prestacao * periodo) - amortizado; 
	}


	public double getPrestacao() {
		return prestacao;
	}


	public void setPrestacao(double prestacao) {
		this.prestacao = prestacao;

		
	}


	public double getSaldoDevedor() {
		return saldoDevedor;
	}


	public void setSaldoDevedor(double saldoDevedor) {
		this.saldoDevedor = saldoDevedor;
	}



	
	
	
}
