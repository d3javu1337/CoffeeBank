using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.model;

[Table("invoice")]
public class Invoice
{
    [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid Id {get; set;}
    public double amount {get; set;}
    public PaymentAccount ProviderPaymentAccount {get; set;}
}
