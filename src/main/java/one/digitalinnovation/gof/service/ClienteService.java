package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente.Com isso, se necessario, podemos ter multiplas
 * implantações da mesma interface.
 */

public interface ClienteService {

    Iterable<Cliente> buscartodos();

    Cliente buscarPorId(Long id);

    void  inserir(Cliente cliente);

    void atualizar(Long id, Cliente cliente);

    void deletar(Long id);
}
