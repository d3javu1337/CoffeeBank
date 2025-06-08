using backend;
using backend.http;
using backend.repository;
using backend.service;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton(new JwtService(
    builder.Configuration.GetSection("JWTSecrets.Access").Value,
    builder.Configuration.GetSection("JWTSecrets.Refresh").Value
    ));

builder.Services.AddScoped<BusinessClientRepository>();
builder.Services.AddHttpContextAccessor();
builder.Services.AddTransient<BusinessClientService>();

var connection = builder.Configuration.GetConnectionString("Postgres");

builder.Services.AddDbContext<DatabaseContext>(options => options.UseNpgsql(connection));
var app = builder.Build();


app.MapBusinessClientEndpoints();
app.MapAuthEndpoints();
app.MapInvoiceEndpoints();
app.MapPaymentAccountEndpoints();
app.MapPaymentEndpoints();
app.MapContactPersonEndpoints();

// app.UseHttpsRedirection();

app.Run();