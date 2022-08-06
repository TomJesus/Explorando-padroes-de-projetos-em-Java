package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação da <b>Strategy</b> {@linkClienteService}, a qual pode ser injetada pelo Spring (via @linkAutowired).
 * Com isso, como essa classe é uma {@linkService},ela será tratada como um <b>Singleton</b>.
 */

@Service
public class ClienteServiceImpl implements ClienteService {

    //Singleton: Injeta os componentesdo spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepService viaCepService;

    //Strategy: Implementar os métodos definidos na interface.
    //Facede: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Cliente> buscartodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);


    }


    @Override
    public void atualizar(Long id, Cliente cliente) {
        //Buscar clientes po id, caso exista:
        Optional<Cliente> clienteBD = clienteRepository.findById(id);
        if (clienteBD.isPresent()) {
            salvarClienteComCep(cliente);
        }

    }

    @Override
    public void deletar(Long id) {
        //Deletar cliente por id.
        clienteRepository.deleteById(id);

    }

    private void salvarClienteComCep(Cliente cliente) {
        //Verifica se o endereco do cliente já existe (pelo cep)
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            //Casonão exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        //Inserie cliente, vincular o endereco( novo ou existente).
        clienteRepository.save(cliente);
    }
}
