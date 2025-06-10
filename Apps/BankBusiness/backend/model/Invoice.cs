using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.model;

[Table("invoice")]
public class Invoice
{
    [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid Id {get; set;}
    
    [Column("amount")]
    public double amount {get; set;}
    
    [Column("provicer_payment_account_id")]
    public PaymentAccount ProviderPaymentAccount {get; set;}
}
