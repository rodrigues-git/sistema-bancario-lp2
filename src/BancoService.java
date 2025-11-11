import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.HashSet;

public class BancoService {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Conta> contas = new ArrayList<>();
    private List<Funcionario> funcionarios = new ArrayList<>();

    private HashSet<Integer> numerosUsados = new HashSet<>();
    private Random random = new Random();

    private int gerarNumeroContaUnico() {
        int numero;
        do {
            numero = random.nextInt(9000) + 1000;
        } while (numerosUsados.contains(numero));
        numerosUsados.add(numero);
        return numero;
    }

    // ========================= CLIENTES =========================
    public Cliente cadastrarCliente(String nome, String cpf, String endereco, String telefone,
                                    String tipoCliente, double renda) throws ValidacaoException {

        if (buscarClienteExistente(cpf).isPresent()) {
            throw new ValidacaoException("CPF já cadastrado.");
        }

        Cliente cliente = new Cliente(nome, cpf, endereco, telefone, tipoCliente, renda);
        clientes.add(cliente);
        return cliente;
    }

    public Cliente buscarClientePorCpf(String cpf) throws ValidacaoException {
        return buscarClienteExistente(cpf)
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado."));
    }

    private Optional<Cliente> buscarClienteExistente(String cpf) {
        return clientes.stream().filter(c -> c.getCpf().equals(cpf)).findFirst();
    }

    public void removerCliente(String cpf) throws ValidacaoException {
        Cliente cliente = buscarClientePorCpf(cpf);

        boolean temConta = contas.stream()
                .anyMatch(c -> c.getTitular().getCpf().equals(cpf));

        if (temConta) {
            throw new ValidacaoException("Cliente não pode ser removido. Contas ativas encontradas. Remova as contas primeiro.");
        }

        clientes.remove(cliente);
    }

    public void atualizarDadosCliente(String cpf, String novoEndereco, String novoTelefone,
                                      String novoTipoCliente, double novaRenda) throws ValidacaoException {

        Cliente cliente = buscarClientePorCpf(cpf);
        if(novoEndereco != null && !novoEndereco.isEmpty()) cliente.setEndereco(novoEndereco);
        if(novoTelefone != null && !novoTelefone.isEmpty()) cliente.setTelefone(novoTelefone);
        if(novoTipoCliente != null && !novoTipoCliente.isEmpty()) cliente.setTipoCliente(novoTipoCliente);
        if(novaRenda >= 0) cliente.setRenda(novaRenda);
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

    // ========================= FUNCIONÁRIOS =========================
    public Funcionario cadastrarFuncionario(String nome, String cpf, String endereco, String telefone,
                                            int matricula, String cargo, double salario) throws ValidacaoException {

        if (buscarFuncionarioExistente(cpf).isPresent() || buscarFuncionarioExistente(matricula).isPresent()) {
            throw new ValidacaoException("CPF ou Matrícula já cadastrado.");
        }

        Funcionario funcionario = new Funcionario(nome, cpf, endereco, telefone, matricula, cargo, salario);
        funcionarios.add(funcionario);
        return funcionario;
    }

    public Funcionario buscarFuncionarioPorCpf(String cpf) throws ValidacaoException {
        return buscarFuncionarioExistente(cpf)
                .orElseThrow(() -> new ValidacaoException("Funcionário não encontrado."));
    }

    public Funcionario buscarFuncionarioPorMatricula(int matricula) throws ValidacaoException {
        return buscarFuncionarioExistente(matricula)
                .orElseThrow(() -> new ValidacaoException("Funcionário não encontrado."));
    }

    private Optional<Funcionario> buscarFuncionarioExistente(String cpf) {
        return funcionarios.stream().filter(f -> f.getCpf().equals(cpf)).findFirst();
    }
    private Optional<Funcionario> buscarFuncionarioExistente(int matricula) {
        return funcionarios.stream().filter(f -> f.getMatricula() == matricula).findFirst();
    }


    public void removerFuncionario(String cpf) throws ValidacaoException {
        Funcionario f = buscarFuncionarioPorCpf(cpf);
        funcionarios.remove(f);
    }

    public void atualizarDadosFuncionario(String cpf, String novoEndereco, String novoTelefone,
                                          String novoCargo, double novoSalario) throws ValidacaoException {

        Funcionario funcionario = buscarFuncionarioPorCpf(cpf);
        if(novoEndereco != null && !novoEndereco.isEmpty()) funcionario.setEndereco(novoEndereco);
        if(novoTelefone != null && !novoTelefone.isEmpty()) funcionario.setTelefone(novoTelefone);
        if(novoCargo != null && !novoCargo.isEmpty()) funcionario.setCargo(novoCargo);
        if(novoSalario >= 0) funcionario.setSalario(novoSalario);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarios;
    }

    // ========================= CONTAS =========================
    public Conta criarContaCorrente(String cpfCliente, double limiteInicial) throws ValidacaoException {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        boolean jaTemCC = contas.stream().anyMatch(c -> c instanceof ContaCorrente &&
                c.getTitular().getCpf().equals(cpfCliente));
        if (jaTemCC) {
            throw new ValidacaoException("Cliente já possui uma conta corrente.");
        }

        int numero = gerarNumeroContaUnico();
        Conta conta = new ContaCorrente(numero, 0.0, cliente, limiteInicial);
        contas.add(conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public Conta criarContaPoupanca(String cpfCliente) throws ValidacaoException {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        boolean jaTemCP = contas.stream().anyMatch(c -> c instanceof ContaPoupanca &&
                c.getTitular().getCpf().equals(cpfCliente));
        if (jaTemCP) {
            throw new ValidacaoException("Cliente já possui uma conta poupança.");
        }

        int numero = gerarNumeroContaUnico();
        double taxaRendimentoPadrao = 0.5; // 0.5%
        Conta conta = new ContaPoupanca(numero, 0.0, cliente, taxaRendimentoPadrao);
        contas.add(conta);
        cliente.adicionarConta(conta);
        return conta;
    }

    public Conta buscarContaPorNumero(int numero) throws ValidacaoException {
        return contas.stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Conta não encontrada."));
    }

    public void atualizarLimiteContaCorrente(int numeroConta, double novoLimite) throws ValidacaoException {
        if (novoLimite < 0) {
            throw new ValidacaoException("O limite não pode ser negativo.");
        }
        Conta conta = buscarContaPorNumero(numeroConta);
        if (conta instanceof ContaCorrente cc) {
            cc.setLimiteChequeEspecial(novoLimite);
        } else {
            throw new ValidacaoException("Esta conta não é uma Conta Corrente.");
        }
    }

    public void atualizarTaxaPoupanca(int numeroConta, double novaTaxa) throws ValidacaoException {
        if (novaTaxa <= 0) {
            throw new ValidacaoException("A taxa de rendimento deve ser positiva.");
        }
        Conta conta = buscarContaPorNumero(numeroConta);
        if (conta instanceof ContaPoupanca cp) {
            cp.setTaxaRendimento(novaTaxa);
        } else {
            throw new ValidacaoException("Esta conta não é uma Conta Poupança.");
        }
    }


    public List<Conta> listarContas() {
        return contas;
    }

    public void removerConta(int numeroConta) throws ValidacaoException {
        Conta conta = buscarContaPorNumero(numeroConta);

        if (conta.getSaldo() > 0) {
            throw new ValidacaoException("Não é possível remover a conta. Saldo precisa ser 0.");
        }

        Cliente titular = conta.getTitular();
        if (titular != null && titular.getContas() != null) {
            titular.getContas().remove(conta);
        }

        contas.remove(conta);
        numerosUsados.remove(conta.getNumero());
    }

    // ========================= OPERAÇÕES =========================

    public void realizarDeposito(int numeroConta, double valor) throws ValidacaoException {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.depositar(valor);
    }

    public void realizarSaque(int numeroConta, double valor)
            throws SaldoInsuficienteException, ValidacaoException {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.sacar(valor);
    }

    public void realizarTransferencia(int numeroContaOrigem, int numeroContaDestino, double valor)
            throws SaldoInsuficienteException, ValidacaoException {

        if (numeroContaOrigem == numeroContaDestino) {
            throw new ValidacaoException("Conta de origem e destino não podem ser iguais.");
        }

        if (valor <= 0) {
            throw new ValidacaoException("Valor da transferência deve ser positivo.");
        }

        Conta contaOrigem = buscarContaPorNumero(numeroContaOrigem);
        Conta contaDestino = buscarContaPorNumero(numeroContaDestino);

        contaOrigem.sacar(valor);

        try {
            contaDestino.depositar(valor);
        } catch (ValidacaoException e) {
            contaOrigem.depositar(valor);
            throw new ValidacaoException("Erro no depósito de destino. Transferência cancelada.");
        }
    }
}