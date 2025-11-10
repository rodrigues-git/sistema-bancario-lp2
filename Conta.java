public abstract class conta {
    private int numero;
    protected double saldo;
    private cliente titular;

    public conta(int numero, double saldo, cliente titular){
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }

    public int getNumero(){
        return numero;
    }

    public void setNumero(int numero){
        this.numero = numero;
    }

    public double getSaldo(){
        return saldo;
    }

    public void depositar(double valor){
        if(valor > 0){
            saldo += valor;
            System.out.println("Operação Concluída");
        }

        else{
            System.out.println("Valor Inválido!");
        }
    }

    public abstract void sacar(double valor);
    
}

   


