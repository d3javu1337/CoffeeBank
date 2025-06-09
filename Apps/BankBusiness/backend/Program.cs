using System.Text;
using backend;
using backend.http;
using backend.kafka;
using backend.repository;
using backend.service;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton(new JwtService(
    builder.Configuration.GetSection("JWTSecret:Key").Value
));

builder.Services.AddScoped<BusinessClientRepository>();
builder.Services.AddHttpContextAccessor();
builder.Services.AddTransient<BusinessClientService>();
builder.Services.AddTransient<AuthService>();
builder.Services.AddTransient<InvoiceService>();
// builder.Services.AddTransient<JwtService>();
builder.Services.AddTransient<PaymentService>();
builder.Services.AddTransient<PaymentAccountService>();
builder.Services.AddTransient<SecurityService>();

// builder.Services.AddProducer(builder.Configuration.GetSection("Kafka:BootstrapServers"));
builder.Services.AddSingleton(_ => new KafkaProducer(
    builder.Configuration.GetSection("Kafka:BootstrapServers").Value
));

builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(x => x.TokenValidationParameters = new TokenValidationParameters()
        {
            IssuerSigningKey =
                new SymmetricSecurityKey(
                    Encoding.UTF8.GetBytes(builder.Configuration.GetSection("JWTSecret:Key").Value)),
            ValidateIssuer = false,
            ValidateAudience = false,
            ValidateLifetime = true,
        }
    );


var connection = builder.Configuration.GetConnectionString("Postgres");

builder.Services.AddDbContext<DatabaseContext>(options => options.UseNpgsql(connection));
var app = builder.Build();


app.MapBusinessClientEndpoints();
app.MapAuthEndpoints();
app.MapInvoiceEndpoints();
app.MapPaymentAccountEndpoints();
app.MapPaymentEndpoints();
app.MapContactPersonEndpoints();

app.UseAuthentication();
// app.UseCors(cors => cors
        // .AllowAnyOrigin()
        // .AllowAnyMethod()
        // .AllowAnyHeader());

// app.UseHttpsRedirection();

app.Run();