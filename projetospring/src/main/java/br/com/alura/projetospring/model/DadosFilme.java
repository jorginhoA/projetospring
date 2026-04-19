package br.com.alura.projetospring.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosFilme(@JsonAlias("Title") String titulo,
                         @JsonAlias("Year") String anoLancamento,
                         @JsonAlias("Runtime") String duracao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String atoresPrincipais,
                         @JsonAlias("Plot") String sinopse){
}
