package de.number26.api.dtos;

/**
 * @author vasiliy
 */
public class TransactionSumDto {
    private double sum;

    public TransactionSumDto() {}

    public TransactionSumDto(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
