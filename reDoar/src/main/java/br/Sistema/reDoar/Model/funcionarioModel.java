package br.Sistema.reDoar.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios")
public class funcionarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSenha(){
        return senha;
    }
    public void seSenha(String senha){
        this.senha = senha;
    }
}
