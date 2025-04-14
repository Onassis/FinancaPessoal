package br.com.fenix.abstrato.dto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "id", ignore = true)
@Mapping(target = "criadoPor", ignore = true)
@Mapping(target = "dtCriacao", ignore = true)
@Mapping(target = "alteradoPor", ignore = true)
@Mapping(target = "dtAlteracao", ignore = true)
@Mapping(target = "versao", ignore = true)
@Mapping(target = "ajuda", ignore = true)
public @interface ToEntity { }
