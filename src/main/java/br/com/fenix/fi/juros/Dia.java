package br.com.fenix.fi.juros;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
//import rwk.util.Format;

public class Dia{
  private Date zero;
  private Date data;
  private BigDecimal valor;
  private BigDecimal dias;
  /*=======================================================

  =======================================================*/
  public Date getZero(){return zero;}
  public void setZero(Date zero){this.zero=zero;}
  public Date getData(){return data;}
  public void setData(Date data){this.data=data;}
  public BigDecimal getValor(){return valor;}
  public void setValor(BigDecimal valor){this.valor=valor;}
  public BigDecimal getDias(){return dias;}
  public void setDias(BigDecimal dias){this.dias=dias;}
  /*=======================================================

  =======================================================*/
  public static Dia create(Date zero,Date data,double valor){
    Dia dia=new Dia();

    dia.setData(data);
    dia.setZero(zero);
    dia.setValor(BigDecimal.valueOf(valor));
    
//    dia.setDias(BigDecimal.valueOf(Format.dateDiff(data,zero)));
    return dia;
  }
  /*=======================================================

  =======================================================*/
  public BigDecimal valorPresente(BigDecimal taxa){
    return valor.divide(BigDecimal.ONE.add(taxa).pow(dias.intValue()),10,RoundingMode.FLOOR);
  }
  /*=======================================================

  =======================================================*/
  public Dia inverte(){
    valor=valor.multiply(BigDecimal.valueOf(-1));
    return this;
  }
  /*=======================================================

  =======================================================*/
  public static BigDecimal taxaRetorno(List<Dia>dias){
    Dia ultimo=dias.get(dias.size()-1);
    BigDecimal taxa=BigDecimal.ZERO;
    BigDecimal somatorio;
    BigDecimal passo,limite;

    ultimo.inverte();
    somatorio=BigDecimal.ZERO;
    for(Dia dia:dias)
      somatorio=somatorio.add(dia.valorPresente(taxa));
    passo=BigDecimal.valueOf(somatorio.doubleValue() >=0?-0.000001:+0.000001);
    limite=BigDecimal.valueOf(somatorio.doubleValue()>=0?-1:+1);

    while(true){
      taxa=taxa.add(passo);
      somatorio=BigDecimal.ZERO;
      for(Dia dia:dias)
        somatorio=somatorio.add(dia.valorPresente(taxa));
      if(somatorio.multiply(limite).doubleValue()>0)
        return taxa;//ao dia
    }
  }
  /*=======================================================

  =======================================================*/
  public static BigDecimal taxaAoMes(BigDecimal taxa){
    return taxa.add(BigDecimal.ONE).pow(30).subtract(BigDecimal.ONE);
  }
}

