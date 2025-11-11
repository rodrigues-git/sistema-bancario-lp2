# Projeto: Sistema Bancário (Programação Orientada a Objetos - Unidade II)

Este projeto implementa um sistema bancário básico via console, focado em aplicar os conceitos fundamentais da Programação Orientada a Objetos (POO), conforme especificado na disciplina do Professor Jerffeson Gomes Dutra.

## 1. Integrantes da Equipe

- Iury Fredson Germano Miranda

- Ricson Miranda da Nóbrega

- José Kauã de Lima Souza

- Fábio Félix Rodrigues


## 2. Visão Geral do Sistema

O sistema foi desenvolvido em Java e simula as operações de um banco, permitindo o gerenciamento de clientes, funcionários e contas (correntes e poupança).

A arquitetura do projeto cumpre os seguintes requisitos da disciplina:

- **Herança (2x):** `Pessoa` (Abstrata) -> `Cliente` / `Funcionario`. E `Conta` (Abstrata) -> `ContaCorrente` / `ContaPoupanca`.

- **Polimorfismo (2x):** O método `getDescricao()` é implementado por `Cliente` e `Funcionario`. O método `sacar()` é implementado por `ContaCorrente` (com limite) e `ContaPoupanca` (sem limite).

- **Encapsulamento:** Todos os atributos de entidade são `private`, com acesso via getters/setters.

- **Exceções Personalizadas:** O sistema usa `SaldoInsuficienteException` e `ValidacaoException` para tratar erros de negócio e de entrada de dados.

- **CRUD:** O `BancoService` implementa o CRUD completo para Clientes, Funcionários e Contas.

- **Interação:** A classe `Main` fornece um menu interativo completo via console.


## 3. Instruções para Compilar o Sistema (Via Terminal)

Todos os arquivos-fonte `.java` estão localizados na pasta `src/`.

1. Clone o repositório:

    ```
    git clone https://github.com/rodrigues-git/sistema-bancario-lp2.git
    cd sistema-bancario-lp2
    ```

2. Crie um diretório para os arquivos compilados (ex: `bin`):

    ```
    mkdir bin
    ```

3. Compile todos os arquivos `.java` da pasta `src/`, direcionando os `.class` para o diretório `bin`:

    ```
    javac -d bin -cp src src/*.java
    ```


## 4. Instruções para Executar o Sistema

1. No diretório raiz do projeto (onde está a pasta `bin`), execute o comando `java`.

2. Use a flag `-cp` (classpath) para indicar onde estão os arquivos `.class` (dentro da pasta `bin`).

3. Especifique o nome da classe que contém o método `main` (que, no nosso caso, é `Main`, pois não estamos usando pacotes).

    ```
    java -cp bin Main
    ```

4. O menu interativo do sistema será iniciado no terminal.