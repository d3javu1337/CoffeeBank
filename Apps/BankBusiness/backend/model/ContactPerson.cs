using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.model;

[Table("contact_person")]
public class ContactPerson
{
    [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id {get; set;}
    public string Surname {get; set;}
    public string Name {get; set;}
    public string Patronymic {get; set;}
    public string PhoneNumber {get; set;}
    public string Email {get; set;}
    public BusinessClient LinledClient {get; set;}
}