public class ContaPoupanca extends Conta {
    private double taxaRendimento;

    public ContaPoupanca(int numero, double saldo, Cliente titular, double taxaRendimento) {
        super(numero, saldo, titular);
        this.taxaRendimento = taxaRendimento;
    }

    public double getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    public void renderJuros() throws ValidacaoException {
        if (this.taxaRendimento <= 0) {
            throw new ValidacaoException("Taxa de rendimento deve ser positiva para aplicar.");
        }

        double rendimento = this.getSaldo() * (this.taxaRendimento / 100);
        this.setSaldo(this.getSaldo() + rendimento);
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException, ValidacaoException {
        if (valor <= 0) {
            throw new ValidacaoException("O valor do saque deve ser positivo.");
        }

        if (valor > this.getSaldo()) {
            throw new SaldoInsuficienteException("Saldo insuficiente para este saque.");
        }

        this.setSaldo(this.getSaldo() - valor);
    }
}