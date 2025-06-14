using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.model;

[Table("contact_person")]
public class ContactPerson
{
    [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id {get; set;}
    
    [Column(name: "surname")]
    public string Surname {get; set;}
    
    [Column(name: "name")]
    public string Name {get; set;}
    
    [Column(name: "patronymic")]
    public string Patronymic {get; set;}
    
    [Column(name: "phone_number")]
    public string PhoneNumber {get; set;}
    
    [Column(name: "email")]
    public string Email {get; set;}
    
    [Column(name: "business_client_id"), NotMapped]
    public BusinessClient LinledClient {get; set;}
}