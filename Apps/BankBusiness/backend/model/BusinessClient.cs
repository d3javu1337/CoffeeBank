using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace backend.model;

[Table("business_client")]
public class BusinessClient
{

    [Key, Column(name: "id"), DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id { get; set; }
    
    [Column(name: "official_name")]
    public string OfficialName { get; set; }
    
    [Column(name: "brand")]
    public string Brand { get; set; }
    
    [Column(name: "email")]
    public string Email { get; set; }
    
    [Column(name: "password_hash")]
    public string PasswordHash { get; set; }

    public BusinessClient()
    {
    }

}