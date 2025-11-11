public class ContaCorrente extends Conta {
    private double limiteChequeEspecial;

    public ContaCorrente(int numero, double saldo, Cliente titular, double limite) {
        super(numero, saldo, titular);
        this.limiteChequeEspecial = limite;
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(double limite) {
        this.limiteChequeEspecial = limite;
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException, ValidacaoException {
        if (valor <= 0) {
            throw new ValidacaoException("O valor do saque deve ser positivo.");
        }

        double saldoDisponivel = this.getSaldo() + this.limiteChequeEspecial;

        if (valor > saldoDisponivel) {
            throw new SaldoInsuficienteException("Saldo e limite insuficientes para este saque.");
        }

        this.setSaldo(this.getSaldo() - valor);
    }
}