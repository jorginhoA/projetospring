package br.com.alura.projetospring.model;

public class Filme {
    private String titulo;
    private String anoLancamento;
    private String duracao;
    private String genero;
    private String atoresPrincipais;
    private String sinopse;


    public Filme(DadosFilme dadosFilme){
        this.titulo = dadosFilme.titulo();
        this.anoLancamento = dadosFilme.anoLancamento();
        this.duracao = dadosFilme.duracao();
        this.genero = dadosFilme.genero();
        this.atoresPrincipais = dadosFilme.atoresPrincipais();
        this.sinopse = dadosFilme.sinopse();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(String anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAtoresPrincipais() {
        return atoresPrincipais;
    }

    public void setAtoresPrincipais(String atoresPrincipais) {
        this.atoresPrincipais = atoresPrincipais;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return "{" +
                " Título: " + titulo +
                "; Ano de Lançamento: " + anoLancamento +
                "; Duracao: " + duracao +
                "; Genero: " + genero + "}";
    }
}
