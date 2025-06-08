using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.model;

[Table("payment")]
public class Payment
{
    [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid Id {get; set;}
    public PaymentAccount ProviderPaymentAccount {get; set;}
    public Invoice Invoice {get; set;}
}