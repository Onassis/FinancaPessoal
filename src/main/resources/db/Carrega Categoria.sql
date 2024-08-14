insert into usuario(id,email,enabled,first_name,last_name,password,verificado) 
values(1,'onassis.tavares@gmail.com',1,'Onassis','Tavares','$2a$10$HS5mDrAcyX8dEGO2Vw6DGuTH42p0NySEgEODudAs0zPJaoztCAH.G',1)

insert into usuario_roles 
values(1,'USER')




insert into conta ( id,  dt_criacao, versao,agencia, apelido, dia_vencimento,
			   instituicao,  numero, saldo,  tipo_conta,
		        alterado_por_id, criado_por_id, moeda_codigo, inativo)
values  (1, CURRENT_TIMESTAMP, 1, '3444','Itau',  0,'Banco Itau', '55333', 0, 'CC', 1, 1, 'BRL','F');


insert into conta ( id,  dt_criacao, versao,agencia, apelido, dia_vencimento,
			   instituicao,  numero, saldo,  tipo_conta,
		        alterado_por_id, criado_por_id, moeda_codigo, inativo)
values  (2, CURRENT_TIMESTAMP, 1, '9111','BB',  0,'Banco Banco Brasil', '53331', 0, 'CC', 1, 1, 'BRL','F');

insert into moeda ( codigo, casa_decimal, moeda,numero)
values ('BRL',',','REAL','986');

insert into moeda ( codigo, casa_decimal, moeda,numero)
values ('USD','.','DOLAR','840');


insert into orcamento ( id, dt_alteracao, dt_criacao , ano , 
					    alterado_por_id , criado_por_id, 
					    categoria_id,
					   sub_categoria_id ,versao) 
SELECT  nextval('orcamento_seq') ,
	               s.dt_alteracao, 
	               s.dt_criacao, '2023', 
	               s.alterado_por_id , s.criado_por_id, c.id  , s.id , 0
			from sub_categoria s 
			inner join categoria c on c.id = s.categoria_id
      		where s.criado_por_id = 1



INSERT INTO categoria (id,descricao, tipo_lancamento, criado_por_id,alterado_por_id,dt_criacao, versao  )
SELECT id,descricao,tipo_Lancamento, 1,1, now(), 0
FROM modelo_categoria

INSERT INTO sub_categoria (id,descricao, tipo_lancamento, categoria_id, criado_por_id,alterado_por_id,dt_criacao, versao  )
SELECT id,descricao,tipo_Lancamento, modelo_categoria_id, 1,1, now(), 0
FROM modelo_Sub_categoria

insert into categoria ( id, descricao, tipo_Lancamento) values (1,'Proventos','C');
insert into categoria ( id, descricao, tipo_Lancamento) values (2,'Habitação','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (3,'Transporte','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (4,'Saúde','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (5,'Educação','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (6,'Impostos','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (7,'Alimentação','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (8,'Cuidados pessoais','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (9,'Lazer','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (10,'Vestuário','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (11,'Outros','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (12,'Pet','D');
insert into categoria ( id, descricao, tipo_Lancamento) values (13,'Financeiro','D');






insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (1,'Proventos','C');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (2,'Habitação','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (3,'Transporte','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (4,'Saúde','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (5,'Educação','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (6,'Impostos','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (7,'Alimentação','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (8,'Cuidados pessoais','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (9,'Lazer','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (10,'Vestuário','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (11,'Outros','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (12,'Pet','D');
insert into modelo_categoria ( id, descricao, tipo_Lancamento) values (13,'Financeiro','D');



insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (1,'Salário','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (2,'Aluguel','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (3,'Pensão','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (4,'Horas extras','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (5,'13º salário','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (6,'Férias','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (7,'Rendimentos/Resgate','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (8,'Restituição imposto','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (9,'Emprestimos','C',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (10,'Outros','D',1);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (11,'Água','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (12,'Aluguel','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (13,'Condomínio','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (14,'Prestação da casa','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (15,'Seguro da casa','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (16,'Empregadoa/Diarista','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (17,'Luz','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (18,'Telefone/Internet','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (19,'Streaming','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (20,'Gás','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (21,'Manutenção','D',2);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (22,'Prestação do carro','D',3);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (23,'Manutenção','D',3);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (24,'Seguro do carro','D',3);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (25,'Estacionamento','D',3);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (26,'Aplicativo','D',3);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (27,'Onibus/Metro','D',3);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (28,'Plano de saúde','D',4);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (29,'Médico','D',4);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (30,'Dentista','D',4);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (31,'Hospital','D',4);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (32,'Fisioterapia','D',4);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (33,'Fármacia','D',4);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (34,'Colégio','D',5);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (35,'Curso Inglês','D',5);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (36,'Material escolar','D',5);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (37,'IPTU','D',6);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (38,'IPVA','D',6);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (39,'Imposto de Renda','D',6);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (40,'Outros','D',6);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (41,'Supermercado','D',7);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (42,'Produto de Limpeza','D',7);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (43,'Bebida','D',7);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (44,'Feira','D',7);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (45,'Padaria','D',7);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (46,'Delivery','D',7);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (47,'Salão','D',8);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (48,'Estetica','D',8);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (49,'Academia','D',8);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (50,'Viagens','D',9);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (51,'Cinema/teatro','D',9);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (52,'Restaurantes/ bares','D',9);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (53,'Outros','D',9);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (54,'Roupas','D',10);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (55,'Calçados','D',10);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (56,'Acessórios','D',10);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (57,'Presentes','D',11);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (58,'Seguro de vida','D',11);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (59,'Alimentação','D',12);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (60,'Veterinario','D',12);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (61,'Remédios','D',12);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (62,'Empréstimos','D',13);
insert into modelo_Sub_categoria ( id, descricao, tipo_Lancamento,modelo_categoria_id) values (63,'Despesas bancarias','D',13);
