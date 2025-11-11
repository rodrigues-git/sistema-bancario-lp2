classDiagram
direction LR

class Pessoa {
<<Abstract>>
-String nome
-String cpf
-String endereco
-String telefone
+getDescricao()* String
}

class Cliente {
-int id
-String tipoCliente
-double renda
-List~Conta~ contas
+adicionarConta(Conta)
+getContas() List~Conta~
+getDescricao() String
}

class Funcionario {
-int matricula
-String cargo
-double salario
+calcularBonificacao() double
+getDescricao() String
}

class Conta {
<<Abstract>>
-int numero
-double saldo
-Cliente titular
+getSaldo() double
#setSaldo(double) void
+getTitular() Cliente
+depositar(double) void
+sacar(double)* void
}

class ContaCorrente {
-double limiteChequeEspecial
+setLimiteChequeEspecial(double) void
+sacar(double) void
}

class ContaPoupanca {
-double taxaRendimento
+setTaxaRendimento(double) void
+renderJuros() void
+sacar(double) void
}

class SaldoInsuficienteException {
<<Exception>>
+SaldoInsuficienteException(String msg)
}

class ValidacaoException {
<<Exception>>
+ValidacaoException(String msg)
}

class BancoService {
-List~Cliente~ clientes
-List~Conta~ contas
-List~Funcionario~ funcionarios
-HashSet~Integer~ numerosUsados
-Random random
-gerarNumeroContaUnico() int
+cadastrarCliente(String...) Cliente
+buscarClientePorCpf(String) Cliente
+removerCliente(String) void
+atualizarDadosCliente(String...) void
+listarClientes() List~Cliente~
+cadastrarFuncionario(String...) Funcionario
+buscarFuncionarioPorCpf(String) Funcionario
+buscarFuncionarioPorMatricula(int) Funcionario
+removerFuncionario(String) void
+atualizarDadosFuncionario(String...) void
+listarFuncionarios() List~Funcionario~
+criarContaCorrente(String, double) Conta
+criarContaPoupanca(String) Conta
+buscarContaPorNumero(int) Conta
+atualizarLimiteContaCorrente(int, double) void
+atualizarTaxaPoupanca(int, double) void
+listarContas() List~Conta~
+removerConta(int) void
+realizarDeposito(int, double) void
+realizarSaque(int, double) void
+realizarTransferencia(int, int, double) void
}

class Main {
+main(String[]) void
}

Pessoa <|-- Cliente
Pessoa <|-- Funcionario
Conta <|-- ContaCorrente
Conta <|-- ContaPoupanca

Cliente "1" o--> "0..*" Conta : titular
BancoService "1" o--> "0..*" Cliente : gerencia
BancoService "1" o--> "0..*" Conta : gerencia
BancoService "1" o--> "0..*" Funcionario : gerencia

Main ..> BancoService : utiliza
Main ..> SaldoInsuficienteException : trata
Main ..> ValidacaoException : trata

BancoService ..> SaldoInsuficienteException : lança
BancoService ..> ValidacaoException : lança

Conta ..> SaldoInsuficienteException : lança
Conta ..> ValidacaoException : lança
