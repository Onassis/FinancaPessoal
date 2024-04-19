Alter table public.lancamento 
add column transferencia BOOLEAN NULL;

Alter table conta 
add column diaComp integer ;

Alter table conta 
drop column diaComp  ;

update conta 
set diaComp = 0; 

  
  
  
CREATE OR REPLACE FUNCTION public.ultimo_saldo(
	usuario bigint,
	conta bigint,
	datasaldo date)
    RETURNS date
    LANGUAGE 'sql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
    select a.data  
    FROM saldo_conta as a  
	where a.criado_por_id = $1 and  
	   a.conta_id = $2 and 
        a.data in 
		 ( SELECT  max(data) 
		   FROM saldo_conta as b 
  		   where b.criado_por_id = a.criado_por_id and 
                  b.conta_id       = a.conta_id   and
                  b.data < $3 );
$BODY$;

ALTER FUNCTION public.ultimo_saldo(bigint, bigint, date)
    OWNER TO postgres;



CREATE or REPLACE  view totalMesDetalhe as 
  SELECT detalhe_lancamento.conta_lancamento_id,
    detalhe_lancamento.criado_por_id,
    to_date(to_char(detalhe_lancamento.data_lanc::timestamp with time zone, 'YYYYMM'::text) || '01'::text, 'YYYYMMDD'::text) AS data,
    sum(detalhe_lancamento.valor) AS valor
   FROM detalhe_lancamento
  WHERE detalhe_lancamento.conta_lancamento_id IS NOT NULL
  GROUP BY detalhe_lancamento.conta_lancamento_id, detalhe_lancamento.criado_por_id, data;

CREATE or REPLACE  view totalMesDetalhe as 
  SELECT d.conta_lancamento_id,
    	 d.ano, 
    	 d.mes,   
         sum(d.valor) AS valor
   FROM detalhe_lancamento as d
   WHERE d.conta_lancamento_id IS NOT NULL
  GROUP BY d.conta_lancamento_id, d.ano,d.mes;
  
  CREATE or REPLACE  view totalMesSaldo as 
  SELECT t.conta_lancamento_id,
    	 t.ano, 
    	 t.mes,   
         t.valor,
         s.saldo_inicial         
   FROM totalMesDetalhe as t
   left join Saldo_Conta as s on s.conta_id = t.conta_lancamento_id and s.ano = t.ano and s.mes = t.ano; 
  
    
  