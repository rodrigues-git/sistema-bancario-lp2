import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.HashSet;

public class BancoService {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Conta> contas = new ArrayList<>();
    private List<Funcionario> funcionarios = new ArrayList<>();

    // controle de números de conta
    private HashSet<Integer> numerosUsados = new HashSet<>();
    private Random random = new Random();

    // gera número aleatório de 4 dígitos sem repetir
    private int gerarNumeroContaUnico() {
        int numero;
        do {
            numero = random.nextInt(10000); // 0 a 9999
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

    public void removerCliente(String cpf) {
        clientes.removeIf(c -> c.getCpf().equals(cpf));
    }

    public void atualizarDadosCliente(String cpf, String novoEndereco, String novoTelefone,
                                      String novoTipoCliente, double novaRenda) throws ValidacaoException {

        Cliente cliente = buscarClientePorCpf(cpf);
        cliente.setEndereco(novoEndereco);
        cliente.setTelefone(novoTelefone);
        cliente.setTipoCliente(novoTipoCliente);
        cliente.setRenda(novaRenda);
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

    // ========================= FUNCIONÁRIOS =========================
    public Funcionario cadastrarFuncionario(String nome, String cpf, String endereco, String telefone,
                                            int matricula, String cargo, double salario) throws ValidacaoException {

        if (buscarFuncionarioExistente(cpf).isPresent()) {
            throw new ValidacaoException("CPF já cadastrado.");
        }

        Funcionario funcionario = new Funcionario(nome, cpf, endereco, telefone, matricula, cargo, salario);
        funcionarios.add(funcionario);
        return funcionario;
    }

    public Funcionario buscarFuncionarioPorCpf(String cpf) throws ValidacaoException {
        return buscarFuncionarioExistente(cpf)
                .orElseThrow(() -> new ValidacaoException("Funcionário não encontrado."));
    }

    private Optional<Funcionario> buscarFuncionarioExistente(String cpf) {
        return funcionarios.stream().filter(f -> f.getCpf().equals(cpf)).findFirst();
    }

    public void removerFuncionario(String cpf) {
        funcionarios.removeIf(f -> f.getCpf().equals(cpf));
    }

    public void atualizarDadosFuncionario(String cpf, String novoEndereco, String novoTelefone,
                                          String novoCargo, double novoSalario) throws ValidacaoException {

        Funcionario funcionario = buscarFuncionarioPorCpf(cpf);
        funcionario.setEndereco(novoEndereco);
        funcionario.setTelefone(novoTelefone);
        funcionario.setCargo(novoCargo);
        funcionario.setSalario(novoSalario);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarios;
    }

    // ========================= CONTAS =========================
    public Conta criarContaCorrente(String cpfCliente, double limiteInicial) throws ValidacaoException {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        // impede duplicação de conta corrente para o mesmo CPF
        boolean jaTemCC = contas.stream().anyMatch(c -> c instanceof ContaCorrente &&
                c.getTitular().getCpf().equals(cpfCliente));
        if (jaTemCC) {
            throw new ValidacaoException("Cliente já possui uma conta corrente.");
        }

        int numero = gerarNumeroContaUnico();
        double saldoInicial = 0.0;

        Conta conta = new ContaCorrente(numero, saldoInicial, cliente, limiteInicial);
        contas.add(conta);
        return conta;
    }

    public Conta criarContaPoupanca(String cpfCliente) throws ValidacaoException {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        // impede duplicação de conta poupança para o mesmo CPF
        boolean jaTemCP = contas.stream().anyMatch(c -> c instanceof ContaPoupanca &&
                c.getTitular().getCpf().equals(cpfCliente));
        if (jaTemCP) {
            throw new ValidacaoException("Cliente já possui uma conta poupança.");
        }

        int numero = gerarNumeroContaUnico();
        double saldoInicial = 0.0;
        double taxaRendimentoPadrao = 0.5;

        Conta conta = new ContaPoupanca(numero, saldoInicial, cliente, taxaRendimentoPadrao);
        contas.add(conta);
        return conta;
    }



    public Conta buscarContaPorNumero(int numero) throws ValidacaoException {
        return contas.stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Conta não encontrada."));
    }

    public void realizarSaque(int numeroConta, double valor)
            throws SaldoInsuficienteException, ValidacaoException {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.sacar(valor);
    }

    public void realizarDeposito(int numeroConta, double valor) throws ValidacaoException {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.depositar(valor);
    }

    public List<Conta> listarContas() {
        return contas;
    }

    public void removerConta(int numeroConta) throws ValidacaoException {
        Conta conta = buscarContaPorNumero(numeroConta);

        if (conta.getSaldo() > 0) {
            throw new ValidacaoException("Não é possível remover a conta. Saldo precisa ser 0.");
        }

        // Remove a conta da lista geral do banco
        contas.remove(conta);

        // Remove a conta da lista interna do cliente
        Cliente titular = conta.getTitular();
        if (titular != null && titular.getContas() != null) {
            titular.getContas().remove(conta);
        }

        // Libera o número da conta para ser usado novamente
        numerosUsados.remove(conta.getNumero());
    }
}

