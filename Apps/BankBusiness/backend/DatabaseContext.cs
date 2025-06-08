using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend;

public class DatabaseContext : DbContext
{
    public DbSet<BusinessClient> BusinessClients { get; set; } = null!;
    public DbSet<PaymentAccount> PaymentAccounts {get; set;} = null!;
    public DbSet<Invoice> Invoices {get; set;} = null!;
    public DbSet<Payment> Payments {get; set;} = null!;
    public DbSet<ContactPerson> ContactPersons {get; set;} = null!;

    public DatabaseContext(){}
    
    public DatabaseContext(DbContextOptions<DbContext> options) : base(options)
    {
        
    }

    protected override void OnConfiguring(DbContextOptionsBuilder opts) {
        if (!opts.IsConfigured)
        {
            opts.UseNpgsql("server=localhost:5433;database=CoffeeBank;username=postgres;password=postgres;pooling=false;timeout=300;commandTimeout=300");
        }
    }
    
}