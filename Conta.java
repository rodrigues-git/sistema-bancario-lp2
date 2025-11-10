public abstract class Conta {
    private int numero;
    private double saldo;
    private Cliente titular;

    public Conta(int numero, double saldo, Cliente titular) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getSaldo() {
        return this.saldo;
    }

    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cliente getTitular() {
        return this.titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public void depositar(double valor) throws ValidacaoException {
        if (valor <= 0) {
            throw new ValidacaoException("O valor do depósito deve ser positivo.");
        }
        this.saldo += valor;
    }

    public abstract void sacar(double valor) throws SaldoInsuficienteException, ValidacaoException;
}