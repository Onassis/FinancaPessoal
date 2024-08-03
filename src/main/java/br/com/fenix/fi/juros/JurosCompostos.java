package br.com.fenix.fi.juros;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JurosCompostos {
   
	
	private int  n=0; 
	private double i= 0.0; 
	private double vp= 0.0; 
	private double fv= 0.0; 
	private double pmt= 0.0; 	
	private TipoPgtoJuros tipoPgto = TipoPgtoJuros.PO; 
	
/*	FV: Future Value
	PV: Present Value
	NPV: Net Present Value
	PMT: (Periodic) Payment
*/	
	public void Clear() {
		n   = 0; 
		i   = 0.0; 
		vp  = 0.0; 
		fv  = 0.0; 
		pmt = 0.0; 
		tipoPgto = TipoPgtoJuros.PO;		
	}
	
	private double Taxa() {
	   return ( this.i / 100 ) ;	
	}
	private double Fator () {
		   return ( Taxa() + 1 ) ;	
		}
	// ------------------- Fator de Valor Atual -----------------------------	
		public double FatorFuturo() {
		    return  (  Math.pow( Fator() , n )   ) ;
		}
		public double FatorPresente() {
		    return  (  Math.pow( Fator() , -1 * n )   ) ;
		}
		
		public double FatorPeriodo   (int periodo ) {
		    return    ( 1 - Math.pow ( Fator() , -1*periodo ) ) /  Taxa()  ;
	}
	
	public  double ValorFuturo() {
		// TODO Auto-generated method stub

        if  (tipoPgto == TipoPgtoJuros.AT) { 
		    fv = (vp + pmt) *  FatorFuturo()  ;
		}
        else {
    		fv = vp * FatorFuturo() ;         	
        }
		fv = fv	+ pmt *  ( FatorFuturo() - 1)  / Taxa();
		
		return fv;
	}

	
	
	public  double ValorPresente() {

		// TODO Auto-generated method stub
	     vp = fv * ValorPresente() ; 
	     
	     vp +=  pmt * ( ValorPresente() - 1) / Taxa() ;  
	     if (tipoPgto == TipoPgtoJuros.AT) { 
	         vp +=  pmt *  Fator(); 		
	    	 
	     }
		
		return vp;
	}	
	public  BigDecimal calculaPMT() {
		// TODO Auto-generated method stub
		
		
		return null;
	}
	
	public  BigDecimal calculaPRICE() {
		// TODO Auto-generated method stub
		
		
		return null;
	}	
	public  float calculaTaxa() {
		// TODO Auto-generated method stub
		
		
		return 0.0f;
	}	

	public  int calculaPrazo() {
		
		// TODO Auto-generated method stub
		
		return 0;
	}	
/*
 *   TipoPgto = (Postecipado,Antecipado);
    Mat = record
           Valor           : extended ;
           PeriodoInicial  : Integer;
           FlagPI          : Boolean;    // Flag se Periodo Inicial foi Setado
           Nro             : Integer;
           Intervalo       : Integer;
     end;
//    function Sinal (Valor : Extended ) : Extended ;
    function  FATU (N,I : Extended ) : Extended ;
    function  FVA  (N,I : Extended ) : Extended ;
    function  FX   (N,I,PV,PMT,FV : Extended ; S : TipoPgto; C : Boolean ) : Extended ;
    function  FX_N (N,I,PV,PMT,FV : Extended ; S : TipoPgto ) : Extended ;
    function  Raiz_I (N,PV,PMT,FV : Extended; S : TipoPgto; C : Boolean )  : Extended ;
    function  Raiz_PMT (N,I,PV,FV : Extended; S : TipoPgto; C : Boolean )  : Extended ;
    function  Raiz_PV (N,I,PMT,FV : Extended; S : TipoPgto; C : Boolean )  : Extended ;
    function  Raiz_FV (N,I,PV,PMT : Extended; S : TipoPgto; C : Boolean )  : Extended ;
    function  Raiz_N  (I,PV,PMT,FV : Extended; S : TipoPgto; C : Boolean)  : Extended ;
    function  NumerodePeriodo(I,PV,PMT,FV : Extended; S : TipoPgto; C : Boolean   ) : Extended ;
    Function  Taxa(N,PV,PMT,FV : Extended; S : TipoPgto; C : Boolean ) : Extended ;
    Function  ValorPrestacao(N,I,PV,FV : Extended; S : TipoPgto; C : Boolean  ) : Extended ;
    Function  ValorPresente(N,I,PMT,FV : Extended; S : TipoPgto; C : Boolean ) : Extended ;
    Function  ValorFuturo(N,I,PV,PMT : Extended; S : TipoPgto; C : Boolean ) : Extended ;
    function  NPV       ( I : Extended ) : Extended;
    Procedure Amortizacao ( T,I,PMT : Extended; Var N,PV,Juros,TotalAmort : Extended;Decimal :Integer ; S : TipoPgto ) ;
    function  Amort     ( N,T,I,PMT : Extended; S : TipoPgto )  : Extended ;
    function  AmortPV   ( N,PV,T,I,PMT : Extended; S : TipoPgto )  : Extended ;
    function  SD        ( N,T,I,PMT : Extended; S : TipoPgto ) : Extended ;
    function  Juro      ( N,T,I,PMT : Extended; S : TipoPgto ) : Extended ;
    function  SomaAmort ( N,T,I,PMT : Extended; S : TipoPgto )  : Extended ;
    function  SomaAmortPV ( N,PV,T,I,PMT : Extended; S : TipoPgto )  : Extended ;
    function  SomaJuro  ( N,T,I,PMT : Extended; S : TipoPgto ) : Extended ;
    function  IRR                                : Extended ;
    function  JuroSimples360 (N,I,PV   : Extended ) : Extended ;
    function  JuroSimples365 (N,I,PV   : Extended ) : Extended ;
    Procedure PrecoAcao ( DatCmp,DatVen,YTM,IC : Extended;Flag_Data : Boolean ; Var preco,Juro : Extended );
    Function  FXPrecoAcao (DCV,DSC,E,Nro,YTM,IC,VPreco :  Extended;Flag_Data : Boolean ) : Extended ;
    Function  Lucro ( DatCmp,DatVen,IC,Preco : Extended;Flag_Data : Boolean ) : Extended ;
    Procedure GravaFluxo( Nome : String) ;
    Procedure LeFluxo   ( Nome : String) ;
  var
      MatFin   :  array[0..MaxFin] of Mat ;
      NumTermo : Integer ;

implementation

Uses
  FCal;

type
    EDataError = class(Exception);

{ ------------------- Retorna o Sinal de um valor recebido -------------}
{function Sinal ( Valor : Extended ) : Extended ;
var
  Aux : Extended ;
begin
  if ( Valor >= 0 ) then
      Aux := 1
  else
      Aux := -1 ;
  Sinal := Aux ;
end;}
{ ------------------- Fator de Valor Atual -----------------------------}
function FATU   (N,I : Extended ) : Extended ;
var
  Aux : Extended ;
begin
  try
    Aux := 0 ;
  try
    Aux :=   ( 1 - Power ( (1 + I) , -1*N ) ) /  I ;

 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   FATU := Aux ;
end;
end;

{ ------------------- Fator de Valor Atual -----------------------------}
function FVA  (N,I : Extended ) : Extended ;
var
  Aux : Extended ;
begin
  try
    Aux := 0 ;
  try
    Aux :=  ( ( Power ( (1 + I) , N ) - 1 ) /
             ( Power  ( (1 + I) , N ) * I ) ) ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   FVA := Aux ;
end;
end;
{ Fim da Funcao FRC }
{ -------------------------------------------------------------------- }
function  FX  (N,I,PV,PMT,FV  : Extended; S : TipoPgto; C : Boolean ) : Extended ;
Var
  T, Inicio : Integer ;
  Aux  : Extended ;
begin
 try
  Aux := 0 ;
  I := I / 100;
  try

  If (S = Postecipado ) then
     Inicio := 0
  else
     Inicio := 1 ;
// -----------------------------------------------------------------------
// Sem Periodo Singular ; N nao e' fracionario
//------------------------------------------------------------------------
  if ( Frac(N) = 0 ) then
      Aux  := PV +
             ( 1 + Inicio * I )* PMT * FVA  ( N,I ) +
              FV * power( 1 + I,-N )
  else
// -----------------------------------------------------------------------
// Utiliza Juros Compostos  no periodo Simgular
//------------------------------------------------------------------------
    if ( C = True ) then
           Aux  := PV  * power( 1 + I , Frac(N))  +
                  ( 1 + Inicio * I )* PMT * FVA( Int(N) , I ) +
                  FV * power( 1 + I,Int( -N ) )
    else
// -----------------------------------------------------------------------
// Utiliza Juros Simples no periodo Simgular
//------------------------------------------------------------------------
           Aux  := PV  * ( 1 + I * Frac(N) )  +
                  ( 1 + Inicio * I )* PMT * FVA (Int( N ), I ) +
                  FV * power( 1 + I,Int( -N ) ) ;

 // -----------------------------------------------------------------------
{{  if ( PV = 0 ) and (FV <> 0) then
    begin
       for T := Inicio to Trunc(N) do
           Aux := Aux + PMT * power (( 1 + I) , (N -  T) )  ;
       Aux := Aux - FV ;
    end
  else
    begin
      for T := Inicio to Trunc(N) do
           Aux := Aux + PMT / power (( 1 + I) , T )  ;
      Aux := Aux - PV ;
    end;}
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   FX := Aux ;
end;
end;    { End da funcao FX }
{------------------------------------------------------------------------}
function  FX_N  (N,I,PV,PMT,FV  : Extended; S : TipoPgto ) : Extended ;
Var
  T, Inicio : Integer ;
  Aux  : Extended ;
begin
 try
  Aux := 0 ;
  I := I / 100 ;
  try
   If (S = Postecipado ) then
      Inicio := 0
   else
      Inicio := 1 ;

   Aux  := PV +
          ( 1 + Inicio * I )* PMT * FVA(N,I) +
          FV * power( 1 + I,-N );
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   FX_N := Aux ;
end;
end;    { End da funcao FX }
{------------------------------------------------------------------------}
function  Raiz_I (N,PV,PMT,FV : Extended; S : TipoPgto; C : Boolean )  : Extended ;
Var
 Taxa_A,Taxa_B,Taxa_C, A_FX,B_FX  : Extended ;
 Nro : Integer;
begin
 try
    Taxa_A := -99.99999999;
    Taxa_B := 30.01;
    Taxa_C := 0.0 ;
    Nro := 0 ;
 try
{---------------- Testa Se Tenho FX (A) ou FX(B) Positivo e negativo -------}
 While ( FX(N,Taxa_A,PV,PMT,FV,S,C) * FX(N,Taxa_B,PV,PMT,FV,S,C)  >  0)
   and (Nro < MaxTen ) do
   begin
//     Taxa_A := Taxa_A *  100 ;
     Taxa_B := Taxa_B *  100 ;
     Nro := Nro + 1 ;
   end;
   if Nro >= MaxTen then raise EOperacao.Create (ErrMatFin) ;
{ --------------------------------------------------------------------------}
  Nro := 0 ;
  while ( Abs(Taxa_A - Taxa_B ) > 0.00000000001 ) and (Nro < MaxTen )   do
   begin
     Nro := Nro + 1 ;
// ----------------------------------------------------------------------
//  Testa a Raiz da Taxa
// ----------------------------------------------------------------------
     A_FX := FX(N,Taxa_A,PV,PMT,FV,S,C) ;
     if A_FX = 0 then
        Taxa_B := Taxa_A ;

     B_FX := FX(N,Taxa_B,PV,PMT,FV,S,C) ;
     if B_FX = 0 then
        Taxa_A := Taxa_B ;
// ----------------------------------------------------------------------
//  Testa a Raiz da Taxa
//  ==> Se Um dos FX de Taxa  for Zero ocorre erro
// ----------------------------------------------------------------------
     if ( A_FX * B_FX )  < 0 then
     begin
        Taxa_C := (Taxa_A + Taxa_b)  / 2 ;
        if ( A_FX * FX(N,Taxa_C,PV,PMT,FV,S,C) ) < 0 then
            Taxa_B := Taxa_C
        else
            Taxa_A := Taxa_C ;
     end; { If  }
  end; { While }
  if Nro >= MaxTen then
  begin
    Taxa_C :=  0 ;
    raise EOperacao.Create (ErrMatFin) ;
  end
  else
    Taxa_C  := ((Taxa_A + Taxa_B) / 2)  ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
  Raiz_I := Taxa_C ;
end;
end; { Fim da Funcao Raiz }
{ --------------------------------------------------------------------}
function  Raiz_PMT (N,I,PV,FV : Extended; S : TipoPgto; C : Boolean )  : Extended ;
Var
 PMT_A,PMT_B,PMT_C , A_Aux , B_Aux : Extended ;
 Nro : Integer;
begin
 try
  PMT_A := -10.00;
  PMT_B :=  10.00;
  PMT_C := 0.0 ;
  Nro := 0 ;
 try
{---------------- Testa Se Tenho FX (A) ou FX(B) Positivo e negativo -------}
 While ( FX(N,I,PV,PMT_A,FV,S,C) * FX(N,I,PV,PMT_B,FV,S,C)  >  0)
   and (Nro < MaxTen ) do
   begin
     PMT_A := PMT_A * 100  ;
     PMT_B := PMT_B * 100  ;
     Nro := Nro + 1 ;
   end;
   if Nro >= MaxTen then raise EOperacao.Create (ErrMatFin) ;
{ --------------------------------------------------------------------------}
  Nro := 0 ;
  while ( Abs(PMT_A - PMT_B ) > Erro  ) and (Nro < MaxTen )   do
   begin
     Nro := Nro + 1 ;
     A_Aux := FX(N,I,PV,PMT_A,FV,S,C) ;
     B_Aux := FX(N,I,PV,PMT_B,FV,S,C) ;

    if A_Aux  = 0 then
        PMT_B := PMT_A ;

    if  B_Aux =  0 then
        PMT_A := PMT_B ;

     if ( A_Aux  * B_Aux  )  < 0 then
     begin
        PMT_C := (PMT_A + PMT_B)  / 2 ;
        if ( A_Aux  * FX(N,I,PV,PMT_C,FV,S,C) ) < 0 then
            PMT_B := PMT_C
        else
            PMT_A := PMT_C ;
     end; { If  }
  end; { While }
  if Nro >= MaxTen then
     PMT_C :=  0
  else
    PMT_C  := ((PMT_A + PMT_B) / 2)  ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
  Raiz_PMT := PMT_C ;
end;
end; { Fim da Funcao Raiz }
{ --------------------------------------------------------------------}
function  Raiz_PV (N,I,PMT,FV : Extended; S : TipoPgto; C : Boolean )  : Extended ;
Var
 PV_A,PV_B,PV_C,A_Aux,B_Aux : Extended ;
 Nro : Integer;
begin
try
  PV_A := -10.00;
  PV_B :=  10.00;
  PV_C := 0.0 ;
  Nro := 0 ;
try
{---------------- Testa Se Tenho FX (A) ou FX(B) Positivo e negativo -------}
 While ( FX(N,I,PV_A,PMT,FV,S,C) * FX(N,I,PV_B,PMT,FV,S,C)  >  0)
   and (Nro < MaxTen ) do
   begin
     PV_A := PV_A * 100  ;
     PV_B := PV_B * 100  ;
     Nro := Nro + 1 ;
   end;
   if Nro >= MaxTen then raise EOperacao.Create (ErrMatFin) ;
{ --------------------------------------------------------------------------}
  Nro := 0 ;
  while ( Abs(PV_A - PV_B ) > Erro  ) and (Nro < MaxTen )   do
   begin
     Nro := Nro + 1 ;
     A_Aux :=  FX(N,I,PV_A,PMT,FV,S,C) ;
     if A_Aux = 0 then
        PV_B := PV_A;

     B_Aux :=  FX(N,I,PV_B,PMT,FV,S,C) ;
     if B_Aux = 0 then
        PV_A := PV_B;

     if ( A_Aux *  B_Aux )  < 0 then
     begin
        PV_C := (PV_A + PV_B)  / 2 ;
        if ( A_Aux * FX(N,I,PV_C,PMT,FV,S,C) ) < 0 then
            PV_B := PV_C
        else
            PV_A := PV_C ;
     end; { If  }
  end; { While }
  if Nro >= MaxTen then
     PV_C :=  0
  else
     PV_C  := ((PV_A + PV_B) / 2)  ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
  Raiz_PV := PV_C ;
end;
end; { Fim da Funcao Raiz_PV }
{ ------------------------------------------------------------------------}
function  Raiz_FV (N,I,PV,PMT : Extended; S : TipoPgto; C : Boolean )  : Extended ;
Var
 FV_A,FV_B,FV_C,A_Aux,B_Aux : Extended ;
 Nro : Integer;
begin
try
  FV_A := -10.00;
  FV_B :=  10.00;
  FV_C := 0.0 ;
  Nro := 0 ;
try
{---------------- Testa Se Tenho FX (A) ou FX(B) Positivo e negativo -------}
 While ( FX(N,I,PV,PMT,FV_A,S,C) * FX(N,I,PV,PMT,FV_B,S,C)  >  0)
   and (Nro < MaxTen ) do
   begin
     FV_A := FV_A * 100  ;
     FV_B := FV_B * 100  ;
     Nro := Nro + 1 ;
   end;
   if Nro >= MaxTen then raise EOperacao.Create (ErrMatFin) ;
{ --------------------------------------------------------------------------}
  Nro := 0 ;
  while ( Abs(FV_A - FV_B ) > Erro)  and (Nro < MaxTen )   do
   begin
     Nro := Nro + 1 ;
     A_Aux := FX(N,I,PV,PMT,FV_A,S,C);
     If A_Aux = 0 then
        FV_B := FV_A;

     B_Aux := FX(N,I,PV,PMT,FV_B,S,C) ;
     if B_Aux = 0 then
        FV_A := FV_B ;

     if ( A_Aux * B_Aux )   < 0 then
     begin
        FV_C := (FV_A + FV_B)  / 2 ;
        if ( A_Aux * FX(N,I,PV,PMT,FV_C,S,C) ) < 0 then
            FV_B := FV_C
        else
            FV_A := FV_C ;
     end; { If  }
  end; { While }
  if Nro >= MaxTen then
     FV_C  :=  0
  else
     FV_C  := ((FV_A + FV_B) / 2)  ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
  Raiz_FV := FV_C ;
end;
end; { Fim da Funcao Raiz_FV }
{ --------------------------------------------------------------------}
function  Raiz_N (I,PV,PMT,FV : Extended; S : TipoPgto; C : Boolean )  : Extended ;
Var
 N_A,N_B,N_C,
        A_FX,
        B_FX           // Resultado de Numero B
            : Extended ;
 Nro : Integer;
begin
try
  N_A := 0 ;
  N_B :=  10.99;
  N_C := 0.0 ;
  Nro := 0 ;
 try
{---------------- Testa Se Tenho FX (A) ou FX(B) Positivo e negativo -------}
 While ( FX (N_A,I,PV,PMT,FV,S,C) * FX(N_B,I,PV,PMT,FV,S,C)  >  0)
   and (Nro <= MaxTen ) do
   begin
     Nro := Nro + 1 ;
//     N_A := N_A * 100  ;
     N_B := N_B * 100  ;
   end;
   if Nro >= MaxTen then raise EOperacao.Create (ErrMatFin) ;
{ --------------------------------------------------------------------------}
  Nro := 0 ;
  while ( Abs(N_A - N_B ) > 0.00000001 ) and (Nro < MaxTen )   do
   begin
     Nro := Nro + 1 ;
// ----------------------------------------------------------------------
//  Testa se achou a Raiz do Nro de Periodos
// ----------------------------------------------------------------------
     A_FX := FX_N(N_A,I,PV,PMT,FV,S) ;
     if A_FX = 0 then
        N_B := N_A ;
     B_FX := FX_N(N_B,I,PV,PMT,FV,S) ;
     if B_FX = 0 then
        N_A := N_B ;
// ----------------------------------------------------------------------
//  Testa a Raiz do Nro de Periodos,
//  ==> Se Um dos FX de N for Zero ocorre erro
// ----------------------------------------------------------------------
     if ( A_FX * B_FX  )  < 0 then
     begin
        N_C := (N_A + N_B)  / 2 ;
        if ( A_FX * FX_N(N_C,I,PV,PMT,FV,S) ) < 0 then
            N_B := N_C
        else
            N_A := N_C ;
     end; { If  }
  end; { While }
  if Nro >= MaxTen then
      raise EOperacao.Create (ErrMatFin) ;

  N_C  := ((N_A + N_B) / 2)  ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
  Raiz_N := N_C ;
end;
end; { Fim da Funcao Raiz_N }
{ --------------------------------------------------------------------------}
function  NumerodePeriodo(I,PV,PMT,FV : Extended; S : TipoPgto; C : Boolean ) : Extended ;
Var
  Aux_N , Y : Extended;
begin
 try
  try
   if ( I <= - 100 ) then
         raise EOperacao.Create( ErrMatFin);
    Aux_N := Raiz_N(I,PV,PMT,FV,S,C)  ;
   if Frac(Aux_N) >= 0.005 then
       Aux_N := Trunc(Aux_N) +1
    else
      Aux_N := Round(Aux_N) ;

//------------------------------------------------------------------------
// Calculo do Periodo por formula 
//------------------------------------------------------------------------
{   I := I / 100 ;
   if (S = Antecipado) then
      Y :=  (1 + I) * PMT
   else
      Y :=   PMT;

     Aux_N  := (   LN (  Abs(PV * I + Y) ) -  LN (Abs(Y - ( FV *  I)))
                 )/ LN (Abs( 1 + I));
 }

{ If ( FV <> 0 ) and ( PV <> 0 ) and ( I <> 0 ) and (PMT = 0)   then
     Aux_N  := ( LN ( FV )  - LN ( PV )) / LN ( 1 + I )
     { ----------------------------------------------------------------- }
{ else  If ( PV <> 0 ) and ( I <> 0 ) and ( PMT <>  0 )
     and ( S = Postecipado) then
     Aux_N  := ( LN (PMT) - LN ( PMT - PV * I )) / LN ( 1 + I)
      { ----------------------------------------------------------------- }
{ else  If ( PV <> 0 ) and ( I <> 0 ) and ( PMT <>  0 )
     and  ( S = Antecipado )  then
     Aux_N  := (   LN (( PMT * (1 + I)))
                 - LN (( (PMT *(1 + I)) - ( PV *  I) ))
                 )/ LN ( 1 + I)
     { ----------------------------------------------------------------- }
 { else  If ( FV <> 0 ) and ( I <> 0 ) and ( PMT <>  0) and (PV = 0)
      and ( S = Postecipado) then
      Aux_N  :=  ( LN ( FV * I + PMT ) - LN(PMT) )  /   LN (1 + I)
    { ----------------------------------------------------------------- }
{  else  If ( FV <> 0 ) and ( I <> 0 ) and ( PMT <>  0)
      and ( S = Antecipado) then
      Aux_N  := (  LN (( (FV * I) + (PMT * (1 + I))  ))
                  - LN(PMT * (1 + I))
                 ) /  LN (1 + I)  ;
      { ----------------------------------------------------------------- }
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   NumerodePeriodo   := Aux_N  ;
end;  { End Finally }
end; { Fim da Funcao Cal_N }
{ --------------------------------------------------------------------}
Function Taxa(N,PV,PMT,FV : Extended; S : TipoPgto; C : boolean ) : Extended ;
Var
  Aux_I : Extended;
begin
  try
   try
   if (N <= 0)  then
      raise EOperacao.Create( ErrMatFin);
   Aux_I := Raiz_I(N,PV,PMT,FV,S,C);
   if Aux_I < 1E-8 then
      Aux_I := 0 ;

{
   If ( FV <> 0 ) and ( PV <> 0 ) and ( N <> 0 ) and ( PMT = 0 ) then
        Aux_i  := ((power(FV,(1 / N))  / PV )  - 1 ) * 100
   { ----------------------------------------------------------------- }
{
   else If (( PV <> 0 ) or (FV <> 0)) and ( N <> 0 )
           and ( PMT <>  0 )  then
         Aux_I := Raiz_I(N,PMT,PV,FV,S,C);}
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   Taxa  := Aux_I ;
end;
end;
{ --------------------------------------------------------------------}
Function ValorPrestacao(N,I,PV,FV : Extended; S : TipoPgto ; C : Boolean ) : Extended ;
Var
    Aux_I,Aux_PMT : Extended;
    Inicio : Integer ;
begin
  try
   try
    if (N = 0) or ( I = 0 ) OR ( I <= - 100 ) then
       raise EOperacao.Create( ErrMatFin);
// ----------------------------------------------------------------------
// Testa o Inicio de Pagamento
//-----------------------------------------------------------------------
  If (S = Postecipado ) then
     Inicio := 0
  else
     Inicio := 1 ;

  I := I / 100 ;

//     if (PV = 0 ) or ( FV = 0 ) then
//        Aux_PMT := 0
//     else
//        Aux_PMT := Raiz_PMT (N,I,PV,FV,S,C);  ;
// ---------------------------------------------------------------------
// Utilizacao da Formula de Calculo
// Utiliza Juros Compostos  no periodo Simgular
//------------------------------------------------------------------------
    if ( C = True ) then
           Aux_PMT   :=
                -1 * (
                       (  PV * power( 1 + I , Frac(N)) +
                          FV * power( 1 + I,Int( -N ) )
                        )
                         / ((1 + Inicio * I )* FATU( Int(N) , I ))
                     )
    else
// -----------------------------------------------------------------------
// Utiliza Juros Simples no periodo Simgular
//------------------------------------------------------------------------
           Aux_PMT   :=
                -1 * (
                       (  PV * ( 1 + I * Frac(N)) +
                          FV * power( 1 + I,Int( -N ) )
                        )
                          / (
                             (1 + Inicio * I )* FATU( Int(N) , I )
                            )
                      )
 // -----------------------------------------------------------------------

{    I := I / 100 ;
    try
    If ( N <> 0 ) and ( PV <> 0 ) and ( I <> 0 )
       and ( S = Postecipado )  then
       Aux_PMT  :=  PV * ( power((1 +  I) , N ) * I ) /
                    ( power((1 +  I) , N ) - 1 )
      { ----------------------------------------------------------------- }
{    else If ( N <> 0 ) and ( PV <> 0 ) and ( I <> 0 )
         and (S = Antecipado )   then
      Aux_PMT  :=  PV * ( 1 / (1 + I) )
                      * ( power((1 + I) , N ) * I ) /
                        ( power((1 + I) , N ) - 1 )
       { ----------------------------------------------------------------- }
 {    else If ( FV <> 0 ) and  (N <> 0 ) and ( I <> 0 )
         and ( S = Postecipado) then
         Aux_PMT  :=  FV *  (  I  / (power((1 + I) , N ) - 1  ))
       { ----------------------------------------------------------------- }
  {   else If ( FV <> 0 ) and  (N <> 0 ) and ( I <> 0 )
     and  ( S = Antecipado )  then
        Aux_PMT  :=  FV * (1 / (1 + I)) *
                        ( I  / (power((1 + I) , N ) - 1  ));
    { ------------------------------------------------------------------- }
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   ValorPrestacao := Aux_PMT ;
end;
end; {Fim da Funcao PMT }
{ -----------------------------------------------------------------------}
Function ValorPresente(N,I,PMT,FV : Extended; S : TipoPgto ; C : Boolean ) : Extended ;
Var
   Aux_PV : Extended;
   Inicio : Integer ;
begin
  try
    try
    if ( I <= -100 ) then
       raise EOperacao.Create( ErrMatFin);

  If (S = Postecipado ) then
     Inicio := 0
  else
     Inicio := 1 ;

  I := I / 100 ;


//  Aux_PV := Raiz_PV(N,I,PMT,FV,S,C) ;
// ---------------------------------------------------------------------
// Utilizacao da Formula de Calculo
// Utiliza Juros Compostos  no periodo Simgular
//------------------------------------------------------------------------
    if ( C = True ) then
           Aux_PV  :=  -1 * (
                           (  (1 + Inicio * I )* PMT * FATU( Int(N) , I ) +
                              FV * power( 1 + I,Int( -N ) )
                           )
                          /  power( 1 + I , Frac(N) )
                         )
    else
// -----------------------------------------------------------------------
// Utiliza Juros Simples no periodo Simgular
//------------------------------------------------------------------------
          Aux_PV  := -1 * (
                        ( ( 1 + Inicio * I )* PMT * FATU ( Int(N), I ) +
                           FV * power( 1 + I,Int( -N ) )
                         )
                          / ( 1 + I * Frac(N) )
                       )

 // -----------------------------------------------------------------------
{
If ( N <> 0 ) and ( FV <> 0 ) and ( I <> 0 ) and ( PMT = 0 )  then
     Aux_PV := FV /  power (( 1 +  I),N )
   { ----------------------------------------------------------------- }
{
else If ( N <> 0 ) and  ( I <> 0 ) and ( PMT <>  0 )
     and ( S = Postecipado)   then
     Aux_PV :=  PMT * ( power(( 1 + I),N) - 1) /
                  ( power(( 1 + I),n) * I )
     { ----------------------------------------------------------------- }
{
else If ( N <> 0 ) and  ( I <> 0 ) and ( PMT <>  0 )
     and  ( S = Antecipado)    then
     Aux_PV :=  PMT * ( 1 +  I)
                    * ( power(( 1 + I),n) - 1) /
                      ( power(( 1 + I),n) * I )   ;
     { ----------------------------------------------------------------- }
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   ValorPresente := Aux_PV ;
end;
end; { Fim da funcao de Calcula PV }
{ --------------------------------------------------------------------}
Function ValorFuturo(N,I,PV,PMT  : Extended; S : TipoPgto; C : Boolean ) : Extended ;
Var
  Aux_FV : Extended ;
  Inicio : Integer;
begin
 try
  try
  if ( I <= -100 ) then
       raise EOperacao.Create(ErrMatFin);
// ----------------------------------------------------------------------
// Testa o Inicio de Pagamento
//-----------------------------------------------------------------------
  If (S = Postecipado ) then
     Inicio := 0
  else
     Inicio := 1 ;

  I := I / 100 ;


//  Aux_FV := Raiz_FV(N,I,PV,PMT,S,C) ;
// ---------------------------------------------------------------------
// Utilizacao da Formula de Calculo
// Utiliza Juros Compostos  no periodo Simgular
//------------------------------------------------------------------------
    if ( C = True ) then
           Aux_FV  :=  -1 * (  PV *  power( 1 + I , Frac(N) ) +
                               (1 + Inicio * I )* PMT * FATU( Int(N) , I )
                             )
                             /  power( 1 + I,Int( -N ) )
           else
// -----------------------------------------------------------------------
// Utiliza Juros Simples no periodo Simgular
//------------------------------------------------------------------------
           Aux_FV  :=  -1 * (  PV *  ( 1 + I *  Frac(N) ) +
                               (1 + Inicio * I )* PMT * FATU( Int(N) , I )
                             )
                             /  power( 1 + I,Int( -N ) ) ;

 // -----------------------------------------------------------------------

 {
If ( N <> 0 ) and ( PV <> 0 ) and ( I <> 0 ) and ( PMT = 0 )  then
    Aux_FV := PV *  power (( 1 +  I),N )
  { ----------------------------------------------------------------- }
{
else If ( N <> 0 ) and ( I <> 0 ) and ( PMT <>  0 )
    and not (S = Postecipado)  then
    Aux_FV :=  PMT * ( power((1 + I ), N ) - 1 ) / I
  { ----------------------------------------------------------------- }
{
else If ( N <> 0 ) and ( I <> 0 ) and ( PMT <>  0 )
    and (S = Antecipado)  then
    Aux_FV :=  PMT * (1 + I) * ( power((1 + I ) , N ) - 1 ) / I
else If ( N <> 0 ) and ( I <> 0 ) and ( PMT <>  0 )
    and (S = Postecipado)  then
    Aux_FV :=  PMT * ( power((1 + I ) , N ) - 1 ) / I ; }
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
finally
   ValorFuturo := Aux_FV ;
end;
end; { Fim da Funcao de Calcula FV }
{ --------------------------------------------------------------------}
function  IRR   : Extended ;
Var
 Taxa_A,Taxa_B,Taxa_C : Extended ;
 Nro : Integer;
begin
try
  Taxa_A := -99.99999999;
  Taxa_B := 30.01;
  Taxa_C := 0.0 ;
  Nro := 0 ;
try
{---------------- Testa Se Tenho FX (A) ou FX(B) Positivo e negativo -------}
   While ( ( NPV(Taxa_A) * NPV(TAXA_B) >   0) and ( Nro < MaxTen ) )  do
      begin
         Nro := Nro + 1 ;
//         Taxa_A := Taxa_A  * 10;
         Taxa_b := Taxa_b  * 100 ;
      end;
   if Nro >= MaxTen then raise EOperacao.Create (ErrMatFin) ;

//   while ( NPV(Taxa_B) >  0) do Taxa_B := Taxa_B +  10;
{ --------------------------------------------------------------------------}
 Nro := 0 ;
 while ( Abs(Taxa_A - Taxa_B ) > 0.000000001 ) and (Nro < MaxTen )   do
   begin
     Nro := Nro + 1 ;
     if NPV(Taxa_A) = 0 then Taxa_B := Taxa_A;
     if NPV(Taxa_B) = 0 then Taxa_A := Taxa_B;
     if ( NPV(Taxa_A) *  NPV(Taxa_B) )  < 0 then
     begin
        Taxa_C := (Taxa_A + Taxa_b)  / 2 ;
        if ( NPV(Taxa_A) * NPV(Taxa_C) ) < 0 then
            Taxa_B := Taxa_C
        else
            Taxa_A := Taxa_C ;
     end; { If  }
  end; { While }
  if Nro >= MaxTen then raise EOperacao.Create (ErrMatFin) ;
  Taxa_C  := ((Taxa_A + Taxa_B) / 2)  ;
  if Taxa_C  < 1E-8 then
      Taxa_C := 0 ;

Except
  On EZeroDivide do Raise ;
  On EInvalidOp  do Raise ;
  On EOverflow   do Raise ;
  On EUnderflow  do Raise ;
  On EInOutError do Raise ;
end; {Except }
finally
   IRR     := Taxa_C ;
end;

end; { Fim da Funcao IRR  }
{------------------------------------------------------------------------}
function NPV ( I : Extended )  : Extended ;
Var
  Ind,Count : Integer ;
  Aux,Total,AuxI,VNro : Extended;
begin
try
 try
  if ( I <= - 100 ) then
        raise EOperacao.Create( ErrMatFin);

  Aux := 0; Total := 0 ; VNro := 0;
  AuxI := (I / 100) + 1 ;
  for Ind := 0 to NumTermo do
    begin
    VNro := MatFin[Ind].PeriodoInicial;
    for Count := 1 to MatFin[Ind].Nro do
      begin
        Aux   :=  MatFin[Ind].Valor / power( AuxI, VNro);
        Total := Total + Aux;
        VNro  := VNro + MatFin[Ind].Intervalo ;
      end;
    end;
 Except
  On EZeroDivide do Raise ;
  On EInvalidOp  do Raise ;
  On EOverflow   do Raise ;
  On EUnderflow  do Raise ;
  On EInOutError do Raise ;
end; {Except }
finally
   NPV := Total ;
end;
end;     { fim da funcao de Valor Presente Liquido }
{------------------------------------------------------------------------}
function Amort ( N,T,I,PMT : Extended; S : TipoPgto ) : Extended ;
var
  A1,PV,Aux : extended ;
begin
 Aux := 0 ;
 try
  try
   PV := ValorPresente(N,I,PMT,0,S,C) ;
   if ( S = Antecipado ) then PV := (Abs(PV) - Abs(PMT)) * Sinal(PV)  ;
   I := I / 100 ;
   A1   := Abs(Abs(PMT) - Abs( PV * I )) * Sinal(PMT) ;
   if T <= 0 then
      Aux := 0
   else
      Aux  := A1  * power ( (1 + I ) , (T - 1) ) ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
  finally
     Amort  := Aux ;
  end;
end;     { Fim da Funcao de Amortizacao }
{------------------ Amortizacao passando Valor Presente  ----------}
function AmortPV ( N,PV,T,I,PMT : Extended; S : TipoPgto ) : Extended ;
var
  A1,Aux : extended ;
begin
 Aux := 0 ;
 try
  try
   if ( S = Antecipado ) then PV := (Abs(PV) - Abs(PMT)) * Sinal(PV)  ;
   I := I / 100 ;
   A1   := Abs(Abs(PMT) - Abs( PV * I )) * Sinal(PMT) ;
   if T <= 0 then
      Aux := 0
   else
      Aux  := A1  * power ( (1 + I ) , (T - 1) ) ;
   Except
  On EZeroDivide do MessageDlg(ErrZeroDivide,MtError,[MbOk],0) ;
  On EInvalidOp  do MessageDlg(ErrInvalidOp,MtError,[MbOk],0);
  On EOverflow   do MessageDlg(ErrOverFlow,MtError,[MbOk],0) ;
  On EUnderflow  do MessageDlg(ErrUnderFlow,MtError,[MbOk],0) ;
  On EInOutError do MessageDlg(ErrInOutError,MtError,[MbOk],0) ;
   end; {Except }
  finally
     AmortPv  := Aux ;
  end;
end;     { Fim da Funcao de Amortizacao }
{------------------------------------------------------------------------}
function SD ( N,T,I,PMT : Extended; S : TipoPgto) : Extended ;
Var
  FPV,Aux,Inicio : Extended ;
begin
 try
  try
  I := I / 100 ;
  Aux := 0; FPV := 0 ;

   If (S = Postecipado ) then
     Inicio := 0
   else
     Inicio := 1 ;

   FPV := (1 + Inicio * I) *  //  FVA ( I , N - T + 1 ) ;
         ( 1 - power ((1 + I),( -1 * (N - T) ) )) / I ;

  Aux := PMT * FPV ;
 Except
  On EZeroDivide do MessageDlg(ErrZeroDivide,MtError,[MbOk],0) ;
  On EInvalidOp  do MessageDlg(ErrInvalidOp,MtError,[MbOk],0);
  On EOverflow   do MessageDlg(ErrOverFlow,MtError,[MbOk],0) ;
  On EUnderflow  do MessageDlg(ErrUnderFlow,MtError,[MbOk],0) ;
  On EInOutError do MessageDlg(ErrInOutError,MtError,[MbOk],0) ;
end; {Except }
finally
   SD := Aux ;
end;
end;     { fim da funcao de Saldo Devedor }
{------------------------------------------------------------------------}
function Juro  ( N,T,I,PMT : Extended;S : TipoPgto ) : Extended ;
begin
   T := T - 1 ;
   if T < 0 then
      Juro := 0
   else
      Juro := SD ( N,T,I,PMT,S) * I / 100 ;
end;                      { Fim da Funcao de juros }
{------------------------------------------------------------------------}
function SomaJuro  ( N,T,I,PMT : Extended; S: TipoPgto ) : Extended ;
Var
  N_Ind,T_Ind,Ind : Integer;
  Aux,VJuro : Extended;
begin
   Aux := 0;
   N_Ind := Trunc(N);
   T_Ind := Trunc(T)  ;
   for Ind := 1  to T_Ind do
     begin
       Vjuro := Juro  ( N,Ind,I,PMT,S)  ;
       Aux := Aux + VJuro;
     end;
   SomaJuro := Aux ;
end;                      { Fim da Funcao de juros }
{------------------------------------------------------------------------}
function SomaAmort ( N,T,I,PMT : Extended;S : TipoPgto  ) : Extended ;
Var
  N_Ind,T_Ind,Ind : Integer;
  Aux,VAmort : Extended;
begin
   Aux := 0;
   N_Ind := Trunc(N);
   T_Ind := Trunc(T);
   for Ind := 1  to T_Ind do
     begin
       VAmort := Amort ( N,Ind,I,PMT,S) ;
       Aux  := Aux + VAmort;
     end;
   SomaAmort := Aux ;
end;                      { Fim da Funcao de juros }
{------------------------------------------------------------------------}
function SomaAmortPV ( N,PV,T,I,PMT : Extended;S : TipoPgto  ) : Extended ;
Var
  N_Ind,T_Ind,Ind : Integer;
  Aux,VAmort : Extended;
begin
   Aux := 0;
   N_Ind := Trunc(N);
   T_Ind := Trunc(T);
   for Ind := 1  to T_Ind do
     begin
       VAmort := AmortPV ( N,PV,Ind,I,PMT,S) ;
       Aux  := Aux + VAmort;
     end;
   SomaAmortPV := Aux ;
end;                      { Fim da Funcao de juros }
{------------------------------------------------------------------------}
function JuroSimples360 (N,I,PV : Extended ) : Extended;
var
  Aux : Extended ;
begin
try
 try
  Aux := 0 ;
  I := I / 100 ;
  Aux :=  (PV * I * N) / 360 ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
end; {Except }
  finally
    JuroSimples360  := Aux ;
  end;
end;
{------------------------------------------------------------------------}
function JuroSimples365 (N,I,PV : Extended ) : Extended;
var
  Aux : Extended ;
begin
try
 try
  Aux := 0 ;
  I := I / 100 ;
  Aux :=  (PV * I * N) / 365 ;
 Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
 end; {Except }
  finally
   JuroSimples365  := Aux ;
  end;
end;

{ -------------------------------------------------------------------
function NPV ( I : Extended )  : Extended ;
Var
  N,Ind,Count : Integer ;
  Aux : Extended;
begin
  Aux := 0;
  N := 0;
  I := I / 100 ;
  for Ind := 1 to NumTermo do
    for Count := 1 to Matfin[Ind].Nro do
      begin
        N := N + 1 ;
        Aux := Aux +
       ( MatFin[Ind].Valor / elevado ( ( 1 + I ), N));
    end;
  NPV := Aux ;
end;
-------------------------------------------------------------------}
Procedure PrecoAcao (
            DatCmp,
            DatVen,
            YTM,             // Taxa de Rentabilidade => Lucro
            IC : Extended ;  // taxa anual do cupom   => CPN
            Flag_Data : Boolean;
            Var preco,Juro : Extended );
Var
  I,Aux1,
  Nro,                   // Numero de Periodos
  K,                     // Numero de somatorios
  F  :  Integer ;        // Frequencia de Pgto de cupons

//  D,                     // Numero de Dias do Cupom onde ocorre a compra
  DCV,                   // Dias entre a data compra e vencimento do titulo
  DSC,                   // Dias entre a data compra e proximo pgto cupom
  A,                     // Dias entre a data compra e ultimo o pgto cupom ( E := DSC + A )
  E,                     // dias ultimo para o proximo pgto cupom ( E=DSC+A)
  P,                     // Preco de titulo ( Normalmente = 100 )
  Dic,       // Dias entre o inicio do cupom e data compra
  Aux   : Extended ;
  VdataVenda,                   // Data de Vencimento do Cupom
  VdataCmp,                     // Data de Compra do Cupom
  VdataProxCupom,               // Data do proximo pgto de Cupom depois compra
  VdataAntCupom : Data ;        // Data do ultimo pgto de cupom  antes compra
begin
   VDataVenda := Data.InitFloat(DatVen,Flag_Data);
   VdataCmp   := Data.InitFloat(DatCmp,Flag_Data);
   DCV        := ( VdataVenda.NroDia - VDataCmp.NroDia  ) ;
   if DCV < 0 then
      raise EOperacao.Create (ErrMatFin) ;
   F := 2 ;                           // Frequencia
   P := 100;                          // Preco do Titulo
//   YTM  := YTM / 100  ;
//   IC   := IC  / 100 ;
   VdataProxCupom  := Data.InitFloat(DatVen,Flag_Data);
   VdataCmp        := Data.InitFloat(DatCmp,Flag_Data);
{-----------------------------------------------------------------------}
{--- Calcula a Data do Proximo e do anterior pagamento do Cupom   ------}
{-----------------------------------------------------------------------}
     Nro := 1;
     Aux := VdataProxCupom.GetFloat(Flag_Data) ;
     VdataAntCupom  := Data.InitFloat( Aux , Flag_Data);
     VdataAntCupom.SubtraiMes( 6 );
     while ( VdataAntCupom.NroDia > VDataCmp.NroDia )  do
     begin
        Inc(Nro);
        VdataProxCupom.SubtraiMes( 6 ) ;
        VdataAntCupom.SubtraiMes( 6 ) ;
     end;
     if Not VdataProxCupom.ValData then
        raise EDataError.Create(ErrDataInvalida) ;
     DSC :=  VdataProxCupom.NroDia - VdataCmp.NroDia ;
     E := VdataProxCupom.NroDia  - VdataAntCupom.NroDia ;
     A := E - DSC ;
{-----------------------------------------------------------------------}
 if (Nro = 1) and (DCV < E ) then
    begin          // cupom menor que um semestre
       DIC   := E - DCV ;
       Preco := ( 100 * ( 100 + (IC / 2)  ) ) /
                 ( 100 + ( (DCV / E) * (YTM / 2) )) ;
       Juro  := (DIC / E)  * (IC / 2) ;   // Calcula o juros
       Preco := Preco - Juro;
    end
 else
     begin
        Preco :=   P  /
                 power( (1 + ( YTM / (F * 100))),(Trunc(Nro-1) + (DSC/ E )));
        for K := 1 to Nro  do
            Preco := Preco +  ( (IC / F ) /
                    power( 1 + ( YTM / ( F * 100 )), (K-1 + (DSC/ E ))));

        Juro := ( ( IC /  F ) * ( A / E )) ; // Calcula juros
        Preco := Preco - Juro ; // Preco da Acao sem o Juros
   end;
 end;
 { ------------------------------- FX Preco Acao ---------------------- }
Function  FXPrecoAcao (
   DCV,     // Dias entre a data compra e vencimento do titulo
   DSC,     // Dias entre a data compra e proximo pgto cupom
   E,       // dias ultimo para o proximo pgto cupom ( E=DSC+A)
   Nro,     // Numero de Periodos
   YTM,                           // Taxa de Rentabilidade
   IC,                            // taxa anual do cupom
   VPreco  : Extended ;           // Preco de compra
   Flag_Data : Boolean  ) : Extended ;
Var
  I,Aux1,
  K,                     // Numero de somatorios
  F  :  Integer ;        // Frequencia de Pgto de cupons

  A,                     // Dias entre a data compra e ultimo o pgto cupom ( E := DSC + A )
  P,                     // Preco de titulo ( Normalmente = 100 )
  Dic,       // Dias entre o inicio do cupom e data compra
  Preco,     // Variavel
  Juro ,
  Aux   : Extended ;
{//  VdataVenda,                   // Data de Vencimento do Cupom
  VdataCmp,                     // Data de Compra do Cupom
  VdataProxCupom,               // Data do proximo pgto de Cupom depois compra
  VdataAntCupom : Data ;        // Data do ultimo pgto de cupom  antes compra
 }
begin
 A := E - DSC ;
 F := 2;
 P := 100 ;
{-----------------------------------------------------------------------}
 if (Nro = 1) and (DCV < E ) then
     begin                           // cupom menor que um semestre
       DIC   := E - DCV ;
       Preco := ( 100 * ( 100 + (IC / 2)  ) ) /
                 ( 100 + ( (DCV / E) * (YTM / 2) )) ;
       Juro  := (DIC / E)  * (IC / 2) ;   // Calcula o juros
       Preco := Preco - Juro;
     end
 else
    begin
        Preco :=   P  /
                 power( (1 + ( YTM / (F * 100))),(Trunc(Nro-1) + (DSC/ E )));
        for K := 1 to Trunc(Nro) do 
            Preco := Preco +  ( (IC / F ) /
                    power( 1 + ( YTM / ( F * 100 )), (K-1 + (DSC/ E ))));

        Juro := ( ( IC /  F ) * ( A / E )) ; // Calcula juros
        Preco := Preco - Juro ; // Preco da Acao sem o Juros
   end;
   FXPrecoAcao := VPreco - Preco ;
 end;
{ -------------------------------------------------------------------- }
Function Lucro  (
            DatCmp,
            DatVen,
            IC ,   // taxa anual do cupom
            Preco  : Extended;
            Flag_Data : Boolean   )   : Extended ;
Var
  I,Aux1,
  NroLoop,
  Nro,                   // Numero de Periodos
  K,                     // Numero de somatorios
  F  :  Integer ;        // Frequencia de Pgto de cupons

  D,                     // Numero de Dias do Cupom onde ocorre a compra
  DCV,                   // Dias entre a data compra e vencimento do titulo
  DSC,                   // Dias entre a data compra e proximo pgto cupom
  A,                     // Dias entre a data compra e ultimo o pgto cupom ( E := DSC + A )
  E,                     // dias ultimo para o proximo pgto cupom ( E=DSC+A)
  P,                     // Preco de titulo ( Normalmente = 100 )
  Dic,       // Dias entre o inicio do cupom e data compra
  Aux   : Extended ;
  VdataVenda,                   // Data de Vencimento do Cupom
  VdataCmp,                     // Data de Compra do Cupom
  VdataProxCupom,               // Data do proximo pgto de Cupom depois compra
  VdataAntCupom : Data ;        // Data do ultimo pgto de cupom  antes compra
  Taxa_A,Taxa_B,Taxa_C : Extended ;
begin
 try
  try
    Taxa_A := 0.1;
    Taxa_B := 10.00;
    Taxa_C := 0.0 ;
    VDataVenda := Data.InitFloat(DatVen,Flag_Data);
    VdataCmp   := Data.InitFloat(DatCmp,Flag_Data);
    DCV        := ( VdataVenda.NroDia - VDataCmp.NroDia  ) ;
    if DCV < 0 then
       raise EOperacao.Create (ErrMatFin) ;
    F := 2 ;                           // Frequencia
    P := 100;                          // Preco do Titulo
//    IC   := IC  / 100 ;
    VdataProxCupom  := Data.InitFloat(DatVen,Flag_Data);
    VdataCmp        := Data.InitFloat(DatCmp,Flag_Data);
{-----------------------------------------------------------------------}
{--- Calcula a Data do Proximo e do anterior pagamento do Cupom   ------}
{-----------------------------------------------------------------------}
     Nro := 1;
     Aux := VdataProxCupom.GetFloat(Flag_Data) ;
     VdataAntCupom  := Data.InitFloat( Aux , Flag_Data);
     VdataAntCupom.SubtraiMes( 6 );
     while ( VdataAntCupom.NroDia > VDataCmp.NroDia )  do
     begin
        Inc(Nro);
        VdataProxCupom.SubtraiMes( 6 ) ;
        VdataAntCupom.SubtraiMes( 6 ) ;
     end;
     if Not VdataProxCupom.ValData then
        raise EDataError.Create(ErrDataInvalida) ;
     DSC :=  VdataProxCupom.NroDia - VdataCmp.NroDia ;
     E := VdataProxCupom.NroDia  - VdataAntCupom.NroDia ;
     A := E - DSC ;
{---------------- Testa Se Tenho FX (A) ou FX(B) Positivo e negativo -------}
NroLoop := 0 ;
 While ( FXPrecoAcao(DCV,DSC,E,Nro,Taxa_A,IC,Preco,Flag_Data) *
         FXPrecoAcao(DCV,DSC,E,Nro,Taxa_B,IC,Preco,Flag_Data)   >  0)
   and (Nro < MaxTen ) do
   begin
     Taxa_A := Taxa_A /  10 ;
     Taxa_B := Taxa_B *  10 ;
     Inc(NroLoop);
   end;
   if NroLoop >= MaxTen then raise EOperacao.Create (ErrMatFin) ;
{ --------------------------------------------------------------------------}
  NroLoop := 0 ;
  while ( Abs(Taxa_A - Taxa_B ) > 0.00000001 ) and (NroLoop < MaxTen )   do
   begin
     Inc(NroLoop) ;
     if ( FXPrecoAcao(DCV,DSC,E,Nro,Taxa_A,IC,Preco,Flag_Data) *
          FXPrecoAcao(DCV,DSC,E,Nro,Taxa_B,IC,Preco,Flag_Data) )  < 0 then
     begin
        Taxa_C := (Taxa_A + Taxa_b)  / 2 ;
        if ( FXPrecoAcao(DCV,DSC,E,Nro,Taxa_A,IC,Preco,Flag_Data) *
            FXPrecoAcao(DCV,DSC,E,Nro,Taxa_C,IC,Preco,Flag_Data) ) < 0 then
            Taxa_B := Taxa_C
        else
            Taxa_A := Taxa_C ;
     end; { If  }
  end; { While }
  Taxa_C  := ((Taxa_A + Taxa_B) / 2)  ;
 Except
  On EZeroDivide do MessageDlg(ErrZeroDivide,MtError,[MbOk],0) ;
  On EInvalidOp  do MessageDlg(ErrInvalidOp,MtError,[MbOk],0);
  On EOverflow   do MessageDlg(ErrOverFlow,MtError,[MbOk],0) ;
  On EUnderflow  do MessageDlg(ErrUnderFlow,MtError,[MbOk],0) ;
  On EInOutError do MessageDlg(ErrInOutError,MtError,[MbOk],0) ;
end; {Except }
finally
  Lucro := Taxa_C ;
end;
end;
{---------------------Retorna Total de Juros, Saldo Devedor => PV,---------------------------------------------}
Procedure Amortizacao ( T,I,PMT : Extended;
                        Var N,PV,Juros,TotalAmort : Extended;
                        Decimal :Integer ;
                        S : TipoPgto ) ;
var
  A1,VAux,VAmort,PV_Aux  : extended ;
  Inicio,Ind,Vdec : Integer;
begin
 VAux := 0 ; Inicio := 1;
 try
   Vdec := Decimal ;
   PMT := RND(PMT,VDec);
   PV  := RND(PV,VDec);
   I := I / 100 ;
   Juros := 0; TotalAmort := 0;
   PV_Aux := PV ;
   for Ind := Inicio to Trunc(T) do
   begin
     // -----------------------------------------------------------------
     // Calcula os Valor do juro, a primeira prestacao o juro e' zero
     // ----------------------------------------------------------------
     if  (S = Antecipado) and ( ( N + Ind ) = 1 ) then
         VAux := 0
     else
         VAux   := Sinal (PMT) * Abs(PV) * i ;      // Calcula Juros
     VAux   := RND(VAux,VDec);
     // -----------------------------------------------------------------
     // Soma os Juros e a Amortizacao da dividas
     // ----------------------------------------------------------------
     Juros      := Juros + VAux;
     Juros      := RND(Juros,Vdec);
     VAmort     := Abs(PMT) - ABS(VAux);
     VAmort     := Sinal(PMT) * RND(VAmort,VDec);
     TotalAmort := TotalAmort + VAmort;
     TotalAmort := RND(TotalAmort,Vdec);
     PV         := Sinal(PV) *
                   (Abs(PV) - Abs(VAmort)) ;
     PV         := RND(PV,VDec);
   end;
   N := N + T ;
   Juros      := RND(Juros,Decimal) ;
   TotalAmort := RND(TotalAmort,Decimal);
   PV         := Sinal(PV) * RND( Abs(PV),Decimal) ;
  Except
  On EZeroDivide do raise;
  On EInvalidOp  do raise;
  On EOverflow   do raise;
  On EUnderflow  do raise;
  On EInOutError do raise;
 end; {Except }
end;     { Fim da Funcao de Amortizacao }
{----------------------------- Grava o Fluxo de Dados  ----------------}
Procedure GravaFluxo ( Nome : String ) ;
var
   T,Ind    : Integer ;
   Aux,Temp : Extended ;
   Arq : File of Extended ;
begin
try
 try
   AssignFile(arq,Nome);
   Rewrite(Arq);
{----------------------- Salva os Registrares financeiros de Fluxo ----------}
  Aux := MFinac.NumTermo;
  Write(Arq,Aux );
  For Ind := 0 to Trunc(Aux) do
   begin
      Temp  := MFinac.MatFin[Ind].Valor;
      Write(Arq,Temp);
      Temp  := MFinac.MatFin[Ind].PeriodoInicial;
      Write(Arq,Temp);
//----------------------------------------------------------------
      if  MFinac.MatFin[Ind].FlagPI then
         Temp := 1
      else
         Temp := 0;
      write(Arq,Temp);
//-------------------------------------------------------------------
      Temp  := MatFin[Ind].Nro;
      Write(Arq,Temp);
      Temp  := MatFin[Ind].Intervalo;
      Write(Arq,Temp);
   end;
Except
  On EZeroDivide do MessageDlg(ErrZeroDivide,MtError,[MbOk],0) ;
  On EInvalidOp  do MessageDlg(ErrInvalidOp,MtError,[MbOk],0);
  On EOverflow   do MessageDlg(ErrOverFlow,MtError,[MbOk],0) ;
  On EUnderflow  do MessageDlg(ErrUnderFlow,MtError,[MbOk],0) ;
  On EInOutError do MessageDlg(ErrInOutError,MtError,[MbOk],0) ;
end; {Except }
finally
  CloseFile (Arq)  ;
end;
end ;

Procedure LeFluxo ( Nome : String ) ;
var
   t,Ind    : Integer ;
   Temp,Aux : Extended;
   Arq : File of Extended ;
begin
try
  try
   AssignFile(arq,Nome);
   Reset(Arq);
   Read (Arq,Aux );
   MFinac.NumTermo := Trunc(Aux) ;
  if Aux <> 0 then
  begin
     For Ind := 0 to Trunc(Aux) do
     begin
//       read(Arq,Temp); MFinac.MatFin[Ind].Valor := temp ;
       read(Arq,Temp);
       FCal.VPilha.Push(temp) ;
       FMenuCal.Insere_Memoria (Ind);
       read(Arq,Temp); MFinac.MatFin[Ind].PeriodoInicial := Trunc(temp) ;
//----------------------------------------------------------------
      Read (Arq,Temp);
      if  Temp = 1 then
          MFinac.MatFin[Ind].FlagPI := True
      else
          MFinac.MatFin[Ind].FlagPI := false;
//-------------------------------------------------------------------
       read(Arq,Temp); MatFin[Ind].Nro := Trunc(temp) ;
       read(Arq,Temp); MatFin[Ind].Intervalo := Trunc(temp) ;
     end;
  end;
  if MFinac.NumTermo < 0 then MFinac.NumTermo := 1 ;
Except
  On EZeroDivide do MessageDlg(ErrZeroDivide,MtError,[MbOk],0) ;
  On EInvalidOp  do MessageDlg(ErrInvalidOp,MtError,[MbOk],0);
  On EOverflow   do MessageDlg(ErrOverFlow,MtError,[MbOk],0) ;
  On EUnderflow  do MessageDlg(ErrUnderFlow,MtError,[MbOk],0) ;
  On EInOutError do MessageDlg(ErrInOutError,MtError,[MbOk],0) ;
  On EInvalidCast do MessageDlg(ErrInvalidCast,MtError,[MbOk],0) ;
end; {Except }
finally
  CloseFile (Arq)  ;
end;
end;
end.

 * 	
 */
	
	
}
