using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.model;

[Table("payment")]
public class Payment
{
    [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid Id {get; set;}
    
    [Column("payment_account_id"), NotMapped]
    public PaymentAccount ProviderPaymentAccount {get; set;}
    
    [Column(name: "personal_account_id"), NotMapped]
    public object personal_account {get; set;}
    
    [Column(name: "transaction_id") , NotMapped]
    public object transaction {get; set;}
    
    [Column(name : "invoice_id")]
    public Invoice? Invoice {get; set;}
}