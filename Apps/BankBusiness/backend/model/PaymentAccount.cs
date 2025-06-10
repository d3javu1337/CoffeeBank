using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.model;

[Table("payment_account")]
public class PaymentAccount
{
    [Key, Column(name: "id"), DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id {get; set;}
    
    [Column(name: "name")]
    public string name {get; set;}
    
    [Column(name: "deposit")]
    public double deposit {get; set;}
    
    [ForeignKey("business_client_id")]
    public BusinessClient LinkedClient {get; set;}
    
    [Column("invoice_create_token")]
    public Guid? InvoiceCreateToken {get; set;}
}