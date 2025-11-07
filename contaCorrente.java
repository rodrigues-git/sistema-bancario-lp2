public class contaCorrente extends conta{
    private double limite;

    public contaCorrente(int numero, double saldo, cliente titular, double limite){
        super(numero, saldo, titular);
        this.limite = limite;
    }

    public double getLimite(){
        return limite;
    }

    public void setLimite(double limite){
        this.limite = limite;
    }

    @Override
    public void sacar(double valor){
        if(valor <= 0){
           System.out.println("Valor Inválido!");
        }
        else if(valor > (saldo + limite)){
            System.out.println("Saldo insuficiente!");
        }
        else{
            saldo -= valor;
            System.out.println("Operação Concluída");
        }
    }
}
