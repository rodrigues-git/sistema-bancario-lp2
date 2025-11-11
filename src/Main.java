import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BancoService banco = new BancoService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== PAINEL DE CONTROLE DO BANCO ===");
            System.out.println("--- Gestão de Clientes ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes (e suas Contas)");
            System.out.println("3. Atualizar Cliente");
            System.out.println("4. Remover Cliente");
            System.out.println("--- Gestão de Contas ---");
            System.out.println("5. Criar Conta Corrente");
            System.out.println("6. Criar Conta Poupança");
            System.out.println("7. Consultar Saldo da Conta");
            System.out.println("8. Remover Conta");
            System.out.println("--- Operações Bancárias ---");
            System.out.println("9. Realizar Depósito");
            System.out.println("10. Realizar Saque");
            System.out.println("11. Realizar Transferência");
            System.out.println("12. Aplicar Rendimento (Poupança)");
            System.out.println("13. Atualizar Limite (Conta Corrente)");
            System.out.println("14. Atualizar Taxa (Conta Poupança)");
            System.out.println("--- Gestão de Funcionários (Interno) ---");
            System.out.println("15. Cadastrar Funcionário");
            System.out.println("16. Listar Funcionários");
            System.out.println("17. Atualizar Funcionário");
            System.out.println("18. Remover Funcionário");
            System.out.println("-------------------------------------");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.err.println("Erro: digite apenas números inteiros.");
                scanner.nextLine();
                continue;
            }

            try {
                switch (opcao) {

                    case 1: {
                        System.out.println("--- Cadastro de Novo Cliente ---");
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
                        scanner.nextLine();

                        banco.cadastrarCliente(nome, cpf, endereco, telefone, tipo, renda);
                        System.out.println(" Cliente cadastrado com sucesso!");
                        break;
                    }

                    case 2: {
                        System.out.println("\n--- CLIENTES E CONTAS CADASTRADOS ---");
                        List<Cliente> clientes = banco.listarClientes();
                        if (clientes.isEmpty()) {
                            System.out.println("Nenhum cliente cadastrado.");
                            break;
                        }

                        for (Cliente cliente : clientes) {
                            System.out.println("\n Cliente: " + cliente.getNome() +
                                    " (CPF: " + cliente.getCpf() + ")");
                            System.out.println("Tipo: " + cliente.getTipoCliente());
                            System.out.printf("Renda: R$ %.2f%n", cliente.getRenda());

                            List<Conta> contasDoCliente = cliente.getContas();
                            if (contasDoCliente.isEmpty()) {
                                System.out.println(" - Nenhuma conta cadastrada.");
                            } else {
                                for (Conta conta : contasDoCliente) {
                                    String tipoConta = (conta instanceof ContaCorrente)
                                            ? "Conta Corrente"
                                            : "Conta Poupança";
                                    System.out.printf(" - %s Nº %04d | Saldo: R$ %.2f%n",
                                            tipoConta, conta.getNumero(), conta.getSaldo());
                                }
                            }
                        }
                        break;
                    }

                    case 3: {
                        System.out.println("--- Atualização de Cliente ---");
                        System.out.print("CPF do cliente a ser atualizado: ");
                        String cpf = scanner.nextLine().trim();

                        Cliente cliente = banco.buscarClientePorCpf(cpf);
                        System.out.println("Cliente encontrado: " + cliente.getNome());

                        System.out.print("Novo Endereço (ou deixe em branco para manter: '" + cliente.getEndereco() + "'): ");
                        String endereco = scanner.nextLine().trim();

                        System.out.print("Novo Telefone (ou deixe em branco para manter: '" + cliente.getTelefone() + "'): ");
                        String telefone = scanner.nextLine().trim();

                        System.out.print("Novo Tipo (comum/premium) (ou deixe em branco para manter: '" + cliente.getTipoCliente() + "'): ");
                        String tipo = scanner.nextLine().trim().toLowerCase();

                        System.out.print("Nova Renda (ou deixe -1 para manter: '" + cliente.getRenda() + "'): ");
                        double renda = scanner.nextDouble();
                        scanner.nextLine();

                        banco.atualizarDadosCliente(cpf, endereco, telefone, tipo, renda);
                        System.out.println(" Cliente atualizado com sucesso!");
                        break;
                    }

                    case 4: {
                        System.out.println("--- Remoção de Cliente ---");
                        System.out.print("CPF do cliente a ser removido: ");
                        String cpf = scanner.nextLine().trim();

                        System.out.print("Tem certeza que deseja remover o cliente " + banco.buscarClientePorCpf(cpf).getNome() + "? (S/N): ");
                        String confirmacao = scanner.nextLine().trim().toUpperCase();

                        if (confirmacao.equals("S")) {
                            banco.removerCliente(cpf);
                            System.out.println(" Cliente removido com sucesso!");
                        } else {
                            System.out.println(" Operação cancelada.");
                        }
                        break;
                    }

                    case 5: {
                        System.out.print("CPF do cliente titular: ");
                        String cpf = scanner.nextLine().trim();

                        System.out.print("Limite do cheque especial: ");
                        double limite = scanner.nextDouble();
                        if (limite < 0)
                            throw new ValidacaoException("Limite não pode ser negativo.");
                        scanner.nextLine();

                        Conta conta = banco.criarContaCorrente(cpf, limite);
                        System.out.printf(" Conta Corrente criada com sucesso! Número: %04d%n", conta.getNumero());
                        break;
                    }

                    case 6: {
                        System.out.print("CPF do cliente titular: ");
                        String cpf = scanner.nextLine().trim();

                        Conta conta = banco.criarContaPoupanca(cpf);
                        System.out.printf(" Conta Poupança criada com sucesso! Número: %04d%n", conta.getNumero());
                        break;
                    }

                    case 7: {
                        System.out.print("Número da conta (4 dígitos): ");
                        int numero = scanner.nextInt();
                        scanner.nextLine();

                        Conta conta = banco.buscarContaPorNumero(numero);
                        System.out.printf(" Saldo da conta %04d (%s): R$ %.2f%n",
                                conta.getNumero(),
                                conta.getTitular().getNome(),
                                conta.getSaldo());
                        break;
                    }

                    case 8: {
                        System.out.print("Número da conta a ser removida (4 dígitos): ");
                        int numero = scanner.nextInt();
                        scanner.nextLine();

                        Conta conta = banco.buscarContaPorNumero(numero);

                        System.out.print("Tem certeza que deseja remover a conta " + conta.getNumero() + " do cliente " + conta.getTitular().getNome() + "? (S/N): ");
                        String confirmacao = scanner.nextLine().trim().toUpperCase();

                        if (confirmacao.equals("S")) {
                            banco.removerConta(numero);
                            System.out.println(" Conta removida com sucesso!");
                        } else {
                            System.out.println(" Operação cancelada.");
                        }
                        break;
                    }

                    case 9: {
                        System.out.print("Número da conta (4 dígitos): ");
                        int numero = scanner.nextInt();

                        System.out.print("Valor do depósito: ");
                        double valor = scanner.nextDouble();
                        scanner.nextLine();

                        banco.realizarDeposito(numero, valor);
                        System.out.println(" Depósito realizado com sucesso!");
                        break;
                    }

                    case 10: {
                        System.out.print("Número da conta (4 dígitos): ");
                        int numero = scanner.nextInt();

                        System.out.print("Valor do saque: ");
                        double valor = scanner.nextDouble();
                        scanner.nextLine();

                        banco.realizarSaque(numero, valor);
                        System.out.println(" Saque realizado com sucesso!");
                        break;
                    }

                    case 11: {
                        System.out.print("Número da conta de ORIGEM (4 dígitos): ");
                        int numOrigem = scanner.nextInt();

                        System.out.print("Número da conta de DESTINO (4 dígitos): ");
                        int numDestino = scanner.nextInt();

                        System.out.print("Valor da transferência: ");
                        double valor = scanner.nextDouble();
                        scanner.nextLine();

                        banco.realizarTransferencia(numOrigem, numDestino, valor);
                        System.out.println(" Transferência realizada com sucesso!");
                        break;
                    }

                    case 12: {
                        System.out.print("Número da conta poupança (4 dígitos): ");
                        int numero = scanner.nextInt();
                        scanner.nextLine();

                        Conta conta = banco.buscarContaPorNumero(numero);

                        if (conta instanceof ContaPoupanca poup) {
                            poup.renderJuros();
                            System.out.println(" Rendimento aplicado com sucesso!");
                            System.out.printf("Taxa atual: %.2f%% | Novo saldo: R$ %.2f%n", poup.getTaxaRendimento(), poup.getSaldo());
                        } else {
                            throw new ValidacaoException("Essa conta não é do tipo poupança.");
                        }
                        break;
                    }

                    case 13: {
                        System.out.print("Número da conta corrente (4 dígitos): ");
                        int numero = scanner.nextInt();

                        System.out.print("Novo limite de cheque especial: ");
                        double limite = scanner.nextDouble();
                        scanner.nextLine();

                        banco.atualizarLimiteContaCorrente(numero, limite);
                        System.out.println(" Limite da Conta Corrente atualizado com sucesso!");
                        break;
                    }

                    case 14: {
                        System.out.print("Número da conta poupança (4 dígitos): ");
                        int numero = scanner.nextInt();

                        System.out.print("Nova taxa de rendimento (em %, ex: 0.7): ");
                        double taxa = scanner.nextDouble();
                        scanner.nextLine();

                        banco.atualizarTaxaPoupanca(numero, taxa);
                        System.out.println(" Taxa da Conta Poupança atualizada com sucesso!");
                        break;
                    }

                    case 15: {
                        System.out.println("--- Cadastro de Novo Funcionário ---");
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine().trim();

                        System.out.print("CPF (11 dígitos): ");
                        String cpf = scanner.nextLine().trim();

                        System.out.print("Endereço: ");
                        String endereco = scanner.nextLine().trim();

                        System.out.print("Telefone: ");
                        String telefone = scanner.nextLine().trim();

                        System.out.print("Matrícula (ex: 12345): ");
                        int matricula = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Cargo (ex: Gerente): ");
                        String cargo = scanner.nextLine().trim();

                        System.out.print("Salário (ex: 3500.00): ");
                        double salario = scanner.nextDouble();
                        scanner.nextLine();

                        banco.cadastrarFuncionario(nome, cpf, endereco, telefone, matricula, cargo, salario);
                        System.out.println(" Funcionário cadastrado com sucesso!");
                        break;
                    }

                    case 16: {
                        System.out.println("\n--- FUNCIONÁRIOS CADASTRADOS ---");
                        List<Funcionario> funcionarios = banco.listarFuncionarios();
                        if (funcionarios.isEmpty()) {
                            System.out.println("Nenhum funcionário cadastrado.");
                            break;
                        }

                        for (Funcionario f : funcionarios) {
                            System.out.println("\nNome: " + f.getNome());
                            System.out.println("Matrícula: " + f.getMatricula() + " | Cargo: " + f.getCargo());
                            System.out.printf("Salário: R$ %.2f%n", f.getSalario());
                        }
                        break;
                    }

                    case 17: {
                        System.out.println("--- Atualização de Funcionário ---");
                        System.out.print("CPF do funcionário a ser atualizado: ");
                        String cpf = scanner.nextLine().trim();

                        Funcionario f = banco.buscarFuncionarioPorCpf(cpf);
                        System.out.println("Funcionário encontrado: " + f.getNome());

                        System.out.print("Novo Endereço (ou deixe em branco para manter): ");
                        String endereco = scanner.nextLine().trim();

                        System.out.print("Novo Telefone (ou deixe em branco para manter): ");
                        String telefone = scanner.nextLine().trim();

                        System.out.print("Novo Cargo (ou deixe em branco para manter): ");
                        String cargo = scanner.nextLine().trim();

                        System.out.print("Novo Salário (ou deixe -1 para manter): ");
                        double salario = scanner.nextDouble();
                        scanner.nextLine();

                        banco.atualizarDadosFuncionario(cpf, endereco, telefone, cargo, salario);
                        System.out.println(" Funcionário atualizado com sucesso!");
                        break;
                    }

                    case 18: {
                        System.out.println("--- Remoção de Funcionário ---");
                        System.out.print("CPF do funcionário a ser removido: ");
                        String cpf = scanner.nextLine().trim();

                        System.out.println("Funcionário encontrado: " + banco.buscarFuncionarioPorCpf(cpf).getNome());
                        System.out.print("Tem certeza que deseja remover o funcionário? (S/N): ");
                        String confirmacao = scanner.nextLine().trim().toUpperCase();

                        if (confirmacao.equals("S")) {
                            banco.removerFuncionario(cpf);
                            System.out.println(" Funcionário removido com sucesso!");
                        } else {
                            System.out.println(" Operação cancelada.");
                        }
                        break;
                    }

                    case 0: {
                        System.out.println(" Encerrando o sistema...");
                        scanner.close();
                        return;
                    }

                    default:
                        System.err.println("Opção inválida. Tente novamente!");
                }

            } catch (SaldoInsuficienteException e) {
                System.err.println(" Erro de Operação: " + e.getMessage());
            } catch (ValidacaoException e) {
                System.err.println(" Erro de Validação: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println(" Erro de Entrada: valor inválido digitado. Use o formato correto.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println(" Erro Inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}