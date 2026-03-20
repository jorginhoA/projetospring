package br.com.alura.projetospring.service;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe);
}
