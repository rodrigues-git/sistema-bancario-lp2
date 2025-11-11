import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BancoService banco = new BancoService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Criar Conta Corrente");
            System.out.println("3. Criar Conta Poupança");
            System.out.println("4. Realizar Saque");
            System.out.println("5. Realizar Depósito");
            System.out.println("6. Listar Clientes e Contas");
            System.out.println("7. Aplicar Rendimento (Poupança)");
            System.out.println("8. Consultar Saldo da Conta");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Erro: digite apenas números inteiros.");
                scanner.nextLine(); // limpa buffer
                continue;
            }

            try {
                switch (opcao) {

                    // ==================== CADASTRAR CLIENTE ====================
                    case 1 -> {
                        scanner.nextLine(); // limpar buffer

                        System.out.print("Nome: ");
                        String nome = scanner.nextLine().trim();
                        if (nome.isEmpty())
                            throw new ValidacaoException("Nome não pode ser vazio.");
                        if (!nome.matches("^[A-Za-zÀ-ÿ\\s]+$"))
                            throw new ValidacaoException("Nome deve conter apenas letras e espaços.");

                        System.out.print("CPF (11 dígitos): ");
                        String cpf = scanner.nextLine().trim();
                        if (!cpf.matches("\\d{11}"))
                            throw new ValidacaoException("CPF deve conter exatamente 11 dígitos numéricos.");

                        System.out.print("Endereço: ");
                        String endereco = scanner.nextLine().trim();
                        if (endereco.isEmpty()) throw new ValidacaoException("Endereço não pode ser vazio.");

                        System.out.print("Telefone (8 a 13 dígitos): ");
                        String telefone = scanner.nextLine().trim();
                        if (!telefone.matches("\\d{8,13}"))
                            throw new ValidacaoException("Telefone deve conter entre 8 e 13 dígitos numéricos.");

                        System.out.print("Tipo de cliente (comum/premium): ");
                        String tipo = scanner.nextLine().trim().toLowerCase();
                        if (!(tipo.equals("comum") || tipo.equals("premium")))
                            throw new ValidacaoException("Tipo de cliente deve ser 'comum' ou 'premium'.");

                        System.out.print("Renda mensal: ");
                        double renda = scanner.nextDouble();
                        if (renda < 0) throw new ValidacaoException("Renda não pode ser negativa.");

                        banco.cadastrarCliente(nome, cpf, endereco, telefone, tipo, renda);
                        System.out.println(" Cliente cadastrado com sucesso!");
                    }

                    // ==================== CRIAR CONTA CORRENTE ====================
                    case 2 -> {
                        scanner.nextLine();
                        System.out.print("CPF do cliente: ");
                        String cpf = scanner.nextLine().trim();

                        System.out.print("Limite do cheque especial: ");
                        double limite = scanner.nextDouble();
                        if (limite < 0)
                            throw new ValidacaoException("Limite não pode ser negativo.");

                        var conta = banco.criarContaCorrente(cpf, limite);
                        System.out.printf(" Conta Corrente criada com sucesso! Número: %04d%n", conta.getNumero());
                    }

                    // ==================== CRIAR CONTA POUPANÇA ====================
                    case 3 -> {
                        scanner.nextLine();
                        System.out.print("CPF do cliente: ");
                        String cpf = scanner.nextLine().trim();

                        var conta = banco.criarContaPoupanca(cpf);
                        System.out.printf(" Conta Poupança criada com sucesso! Número: %04d%n", conta.getNumero());
                    }

                    // ==================== REALIZAR SAQUE ====================
                    case 4 -> {
                        System.out.print("Número da conta (4 dígitos): ");
                        int numero = scanner.nextInt();

                        System.out.print("Valor do saque: ");
                        double valor = scanner.nextDouble();
                        if (valor <= 0)
                            throw new ValidacaoException("Valor do saque deve ser positivo.");

                        banco.realizarSaque(numero, valor);
                        System.out.println(" Saque realizado com sucesso!");
                    }

                    // ==================== REALIZAR DEPÓSITO ====================
                    case 5 -> {
                        System.out.print("Número da conta (4 dígitos): ");
                        int numero = scanner.nextInt();

                        System.out.print("Valor do depósito: ");
                        double valor = scanner.nextDouble();
                        if (valor <= 0)
                            throw new ValidacaoException("Valor do depósito deve ser positivo.");

                        banco.realizarDeposito(numero, valor);
                        System.out.println(" Depósito realizado com sucesso!");
                    }

                    // ==================== LISTAR CLIENTES E CONTAS ====================
                    case 6 -> {
                        System.out.println("\n=== CLIENTES CADASTRADOS ===");
                        var clientes = banco.listarClientes();

                        for (var cliente : clientes) {
                            System.out.println("\n Cliente: " + cliente.getNome() +
                                    " (" + cliente.getCpf() + ")");
                            System.out.println("Tipo: " + cliente.getTipoCliente());
                            System.out.printf("Renda: R$ %.2f%n", cliente.getRenda());

                            boolean temConta = false;

                            var contas = banco.listarContas();
                            for (var conta : contas) {
                                if (conta.getTitular().getCpf().equals(cliente.getCpf())) {
                                    String tipoConta = (conta instanceof ContaCorrente)
                                            ? "Conta Corrente"
                                            : "Conta Poupança";
                                    System.out.printf(" - %s Nº %04d | Saldo: R$ %.2f%n",
                                            tipoConta, conta.getNumero(), conta.getSaldo());
                                    temConta = true;
                                }
                            }

                            if (!temConta) {
                                System.out.println(" - Nenhuma conta cadastrada.");
                            }
                        }
                    }

                    // ==================== APLICAR RENDIMENTO ====================
                    case 7 -> {
                        System.out.print("Número da conta poupança (4 dígitos): ");
                        int numero = scanner.nextInt();
                        var conta = banco.buscarContaPorNumero(numero);

                        if (conta instanceof ContaPoupanca poup) {
                            poup.renderJuros();
                            System.out.println(" Rendimento aplicado com sucesso!");
                            System.out.printf("Novo saldo: R$ %.2f%n", poup.getSaldo());
                        } else {
                            throw new ValidacaoException("Essa conta não é do tipo poupança.");
                        }
                    }

                    // ==================== SAIR ====================
                    case 0 -> {
                        System.out.println(" Encerrando o sistema...");
                        return;
                    }

                    // ==================== INVÁLIDA ====================
                    default -> System.err.println("Opção inválida. Tente novamente!");
                }

            } catch (SaldoInsuficienteException e) {
                System.err.println(" Erro de saldo: " + e.getMessage());
            } catch (ValidacaoException e) {
                System.err.println(" Erro de validação: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println(" Erro: valor inválido digitado.");
                scanner.nextLine(); // limpar buffer
            } catch (Exception e) {
                System.err.println(" Erro inesperado: " + e.getMessage());
            }
        }
    }
}
