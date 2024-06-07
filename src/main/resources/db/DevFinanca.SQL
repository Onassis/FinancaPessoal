/*
	Criar Historico de saldo por PL_SQL 

	1) Criar view de total por dia 
	2) Criar função que busca ultimo saldo 
	3) Criar trigger que gera historico do saldo   
	4) Criar função para atualizar saldo 

*/
/************************************************************************************************************************************************/
/*                                                      View                                                                                   */
/************************************************************************************************************************************************/

SELECT  a.lancamento_id,l.data_doc, l.informacao, l.Nro_prestacao,l.total,min(prestacao), abs(sum(a.valor)) as vr_devido 
fROM detalhe_lancamento as a
inner join lancamento as l on l.id = a.lancamento_id
where data_venc >= '2024-06-01' and l.Nro_prestacao > 1
group by  a.lancamento_id,l.data_doc, l.informacao, l.Nro_prestacao,l.total
order by l.total desc

SELECT  a.lancamento_id,a.ano,a.mes, a.tipo_lancamento, sum(a.valor)  
fROM detalhe_lancamento as a
group by a.lancamento_id,a.ano,a.mes, a.tipo_lancamento
 


CREATE or REPLACE  view view_lanc as
SELECT l.data_doc, l.informacao,a.ano,a.mes, a.tipo_lancamento, a.valor, a.prestacao, l.Nro_prestacao, l.id as lancamento_id,a.id as detalhe_id 
fROM detalhe_lancamento as a 
inner join lancamento as l on l.id = a.lancamento_id



CREATE or REPLACE  view view_total_dia_lanc as  
  select conta_lancamento_id, criado_por_id, data_venc,
	sum ( CASE  WHEN tipo_lancamento = 'D' THEN valor  else 0  END ) as debito,
    sum ( CASE  WHEN tipo_lancamento = 'C' THEN valor  else 0  END ) as credito,
    sum (valor) as total
from detalhe_lancamento
where conta_id is not null and conciliado = true 
group by conta_lancamento_id, criado_por_id, data_venc

CREATE or REPLACE  view view_saldo  as  
 select s.conta_id, s.criado_por_id, s.data, s.saldo_inicial , v.total , s.saldo_inicial + v.total as saldo_final 
   from saldo_conta as s 
   left join  view_total_dia_lanc  as v on v.criado_por_id =  s.criado_por_id and 
               v.conta_lancamento_id = s.conta_id and v.data_venc = s.data 
 order by s.conta_id, s.criado_por_id, s.data;

CREATE or REPLACE  view view_SaldoAtual  as  
    select a.conta_id, a.criado_por_id, a.data, a.saldo_inicial,a.total, a.saldo_final 
    from view_saldo as a  
	  where      a.data in 
		 ( SELECT  max(data) 
		   FROM saldo_conta as c 
  		   where  c.conta_id  = a.conta_id  );

CREATE or REPLACE  view view_TotalAnoMes   as             
SELECT detalhe_lancamento.conta_lancamento_id,
    detalhe_lancamento.criado_por_id,
    detalhe_lancamento.ano,
    detalhe_lancamento.mes,
    sum(
        CASE
            WHEN detalhe_lancamento.tipo_lancamento::text = 'D'::text THEN detalhe_lancamento.valor
            ELSE 0::numeric
        END) AS debito,
    sum(
        CASE
            WHEN detalhe_lancamento.tipo_lancamento::text = 'C'::text THEN detalhe_lancamento.valor
            ELSE 0::numeric
        END) AS credito,
    sum(detalhe_lancamento.valor) AS total
   FROM detalhe_lancamento
  WHERE detalhe_lancamento.conta_lancamento_id IS NOT NULL AND detalhe_lancamento.conciliado = true
  GROUP BY  detalhe_lancamento.conta_lancamento_id,
            detalhe_lancamento.criado_por_id,
            detalhe_lancamento.ano,
            detalhe_lancamento.mes;
/************************************************************************************************************************************************/
/*                                                      Funcao                                                                                  */
/************************************************************************************************************************************************/
CREATE OR REPLACE FUNCTION public.f_ultimo_saldo(	usuario bigint,	conta bigint, 	datasaldo date)
                   RETURNS table (conta_id bigInt, criado_por_id bigInt, data date, saldo_inicial numeric, saldo_final numeric)   
AS $$
BEGIN
    RETURN QUERY  select a.conta_id, a.criado_por_id, a.data, a.saldo_inicial,a.saldo_final 
            FROM view_saldo as a  
	          where a.criado_por_id = $1 and    a.conta_id = $2 and 
            a.data in  	 ( SELECT  max(b.data)          FROM saldo_conta as b 
  		                      where b.criado_por_id = a.criado_por_id and 
                            b.conta_id       = a.conta_id   and
                            b.data <= $3 );
END;				  
$$  LANGUAGE plpgsql;
select * from f_ultimo_saldo(2,1 , '2024-04-02');

CREATE OR REPLACE FUNCTION public.f_atualiza_saldo(usuario bigint,	conta bigint,	datasaldo date, saldoInicial numeric ) 
                   RETURNS BOOLEAN   
AS $$ 
DECLARE
        v_saldoConta numeric; 
	    	rd_View_saldo     record; 
        v_flag BOOLEAN; 
BEGIN          
        v_flag = true; 
        FOR  rd_View_saldo  IN
            SELECT * FROM view_saldo  WHERE  criado_por_id = $1 and conta_id = $2 and data >= $3 
        LOOP  
           if saldoInicial is null then
              saldoInicial = rd_View_saldo.saldo_inicial ; 
              v_flag = false;
           end if;
           update saldo_Conta   
                  set saldo_inicial = saldoInicial, 
                      flag_compensacao = v_flag, 
                      versao = versao + 1 
                  where conta_id =   rd_View_saldo.conta_id and 
                        data     =  rd_View_saldo.data and 
                        versao = versao;
               saldoInicial   = saldoInicial  +  rd_View_saldo.total; 
        END LOOP;
    return true;     
END;				  
$$  LANGUAGE plpgsql         

call f_atualiza_saldo(1,1 , '2024-04-01',null);
/************************************************************************************************************************************************/
/*                                                      Trigger                                                                                 */
/************************************************************************************************************************************************/
CREATE TRIGGER tr_detalhe_lancamento
    AFTER insert
        ON detalhe_lancamento
    FOR EACH ROW
    WHEN (new.conta_lancamento_id is NOT null and new.conciliado = true )
    EXECUTE FUNCTION f_gerar_saldo();

CREATE TRIGGER tr_detalhe_lancamento_upd
    AFTER update 
        ON detalhe_lancamento
    FOR EACH ROW
    WHEN (new.conta_lancamento_id is NOT null and new.conciliado = true )
    EXECUTE FUNCTION f_gerar_saldo();

*    
CREATE OR REPLACE FUNCTION f_gerar_saldo ()
RETURNS trigger AS
$$
   declare
  	  rt_saldo          saldo_conta%rowtype;
      rd_saldoAnterior  record;
      v_saldoConta     numeric; 
    begin
        select * into rt_saldo from saldo_conta where  conta_id = NEW."conta_lancamento_id" and data = NEW."data_venc" ;
         IF NOT FOUND THEN          
              select * into rd_saldoAnterior from f_ultimo_saldo(NEW."criado_por_id",NEW."conta_lancamento_id", NEW."data_venc"); 
              if FOUND then 
                  insert into saldo_conta (conta_id, data,flag_compensacao,saldo_inicial,criado_por_id) 
                                values  (NEW.conta_lancamento_id, NEW.data_venc ,false, rd_saldoAnterior.saldo_final,NEW.criado_por_id);                                               
              else 
                select conta.saldo into v_saldoConta from conta where id = NEW."conta_lancamento_id";                                 
                insert into saldo_conta (conta_id, data,flag_compensacao,saldo_inicial,criado_por_id) 
                                values  (NEW.conta_lancamento_id, NEW.data_venc ,false, v_saldoConta,NEW.criado_por_id);                                               
              END IF;                 
        END IF;         
        RETURN NEW;
     end;
 $$ LANGUAGE 'plpgsql'; 

call f_atualiza_saldo(NEW."criado_por_id",NEW."conta_lancamento_id", NEW."data_venc",null); 
/**************************************************************** Trigger Delete  *************************************************************/
CREATE TRIGGER tr_detalhe_lancamento_del
    AFTER delete 
        ON detalhe_lancamento
    FOR EACH ROW
    WHEN (new.conta_lancamento_id is NOT null and new.conciliado = true )
    EXECUTE FUNCTION f_gerar_saldo_del();

CREATE OR REPLACE FUNCTION f_gerar_saldo_del ()
RETURNS trigger AS
$$
  begin
      call f_atualiza_saldo(NEW."criado_por_id",NEW."conta_lancamento_id", NEW."data_venc",null); 
      RETURN NEW;   
$$ LANGUAGE 'plpgsql';    

CREATE TRIGGER tr_saldo_conta_insr 
    before insert  ON saldo_conta 
    FOR EACH ROW
    EXECUTE FUNCTION f_before_saldo_insr();

CREATE OR REPLACE FUNCTION f_before_saldo_insr ()
RETURNS trigger AS
$$
   declare
    begin
        new.Id = nextval('saldo_conta_seq');
        new.dt_criacao = now(); 
        new.versao = 0; 
        new.Ano = extract( year from new.data);
        new.mes = extract( month from new.data);
        new.dt_alteracao = now();
        new.alterado_por_id = new.criado_por_id;
        RETURN NEW;
     end;
 $$ LANGUAGE 'plpgsql'; 

CREATE TRIGGER tr_saldo_conta_upd
    before update  ON saldo_conta 
    FOR EACH ROW
    EXECUTE FUNCTION f_before_saldo_upd();    

CREATE OR REPLACE FUNCTION f_before_saldo_upd ()
RETURNS trigger AS
$$
   declare
    begin
        new.Ano = extract( year from new.data);
        new.mes = extract( month from new.data);
        new.dt_alteracao = now();
        RETURN NEW;
     end;
 $$ LANGUAGE 'plpgsql'; 

 
/************************************************************************************************************************************************/
/*                                                      alter table                                                                             */
/************************************************************************************************************************************************/
Alter table saldo_conta  
add column saldo_final numeric NULL;


 insert into saldo_conta (id,dt_alteracao,dt_criacao,versao,ano,data,flag_compensacao,flag_manual,mes,saldo_inicial,alterado_por_id,criado_por_id,conta_id) 
                                values ( nextval(saldo_conta_seq), NEW.dt_alteracao,NEW.dt_criacao,0, NEW.ano,NEW."data_venc" ,false,flase,NEW.mes,50,NEW.alterado_por_id,NEW.criado_por_id,NEW.conta_lancamento_id); 



Alter table public.lancamento 
add column transferencia BOOLEAN NULL;

Alter table conta 
add column diaComp integer ;

Alter table conta 
drop column diaComp  ;


Alter table saldo_Conta 
drop column flag_manual;

Alter table saldo_Conta 
drop column saldo;

update conta 
set diaComp = 0; 


/************************************************************************************************************************************************/
/*                                                      teste de SQL                                                                            */
/************************************************************************************************************************************************/

SELECT *
FROM crosstab(select  ano,mes,total from view_totalanomes) as 
AS final_result (
  ano numeric,	
  mes numeric,
  total numeric); 



SELECT * FROM crosstab(
  'SELECT product_category, country, sum(sales)
   FROM sales_data
   GROUP BY product_category, country
   ORDER BY product_category, country')
AS final_result (
  product_category text,
  usa_sales numeric,
  canada_sales numeric); 

select * from crosstab (


  'select ano, mes, valor from detalhe_lancamento order by 1',
  'select m from generate_series(1,12) m'
) as (
  year int,
  "Jan" int,
  "Feb" int,
  "Mar" int,
  "Apr" int,
  "May" int,
  "Jun" int,
  "Jul" int,
  "Aug" int,
  "Sep" int,
  "Oct" int,
  "Nov" int,
  "Dec" int
);


select m from generate_series(1,12) m'
 as (
  year int,
  "Jan" int,
  "Feb" int,
  "Mar" int,
  "Apr" int,
  "May" int,
  "Jun" int,
  "Jul" int,
  "Aug" int,
  "Sep" int,
  "Oct" int,
  "Nov" int,
  "Dec" int
);