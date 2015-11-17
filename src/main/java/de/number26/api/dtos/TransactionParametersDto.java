package de.number26.api.dtos;

/**
 * @author vasiliy
 */
public class TransactionParametersDto {
    private double amount;
    private String type;
    private Long parentId;

    public TransactionParametersDto() {}

    public TransactionParametersDto(Long parentId, String type, double amount) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "TransactionParams{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
