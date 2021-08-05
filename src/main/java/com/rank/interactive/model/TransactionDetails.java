package com.rank.interactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.sql.Timestamp;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetails  {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
     private Long id;
    @Column
    private Double amount;
    @Column
    private Long transaction;
    @Column
    private Long player;
    @Column
    private TransactionDetailsStatus transactionDetailsStatus;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp dateCreated;
    @UpdateTimestamp
    private Timestamp lastModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTransaction() {
        return transaction;
    }

    public void setTransaction(Long transaction) {
        this.transaction = transaction;
    }

    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public TransactionDetailsStatus getTransactionDetailsStatus() {
        return transactionDetailsStatus;
    }

    public void setTransactionDetailsStatus(TransactionDetailsStatus transactionDetailsStatus) {
        this.transactionDetailsStatus = transactionDetailsStatus;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "TransactionDetails{" +
                "id=" + id +
                ", amount=" + amount +
                ", transaction=" + transaction +
                ", player=" + player +
                ", transactionDetailsStatus=" + transactionDetailsStatus +
                ", dateCreated=" + dateCreated +
                ", lastModified=" + lastModified +
                '}';
    }
}
