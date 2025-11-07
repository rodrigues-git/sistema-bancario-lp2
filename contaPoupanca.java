public class contaPoupanca extends conta{
    private double taxaRendimento;

    public contaPoupanca(int numero, double saldo, cliente titular, double taxaRendimento){
        super(numero, saldo, titular);
        this.taxaRendimento = taxaRendimento;
    }

    public double getTaxaRendimento(){
        return taxaRendimento;
    }

    public void setTaxaRendimento(double taxaRendimento){
        this.taxaRendimento = taxaRendimento;
    }

    public void renderJuros(){
        double i = taxaRendimento / 100;
        saldo += saldo*i; 
    }
}
