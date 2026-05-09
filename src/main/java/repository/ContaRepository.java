package repository;

import model.Conta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContaRepository {

    private Map<String, Conta> contas = new HashMap<>();

    public void salvar(Conta conta){
        contas.put(conta.getUsuario().getCpf(), conta);
    }

    public Conta buscarPorCpf(String cpf){
        return contas.get(cpf);
    }

    public boolean existeConta(String cpf){
        return contas.containsKey(cpf);
    }

    public Collection<Conta> listarTodas(){
        return contas.values();
    }
}