using System.Security.Claims;
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


//repos start
builder.Services.AddScoped<BusinessClientRepository>();
builder.Services.AddScoped<PaymentAccountRepository>();
builder.Services.AddScoped<PaymentRepository>();
//repos end

builder.Services.AddHttpContextAccessor();

//service start
builder.Services.AddTransient<BusinessClientService>();
builder.Services.AddTransient<AuthService>();
builder.Services.AddTransient<InvoiceService>();
builder.Services.AddTransient<PaymentService>();
builder.Services.AddTransient<PaymentAccountService>();
builder.Services.AddTransient<SecurityService>();
builder.Services.AddSingleton(new JwtService(
    builder.Configuration.GetSection("JWTSecret:Key").Value
));
//service end

// builder.Services.AddProducer(builder.Configuration.GetSection("Kafka:BootstrapServers"));
builder.Services.AddSingleton(_ => new KafkaProducer(
    builder.Configuration.GetSection("Kafka:BootstrapServers").Value
));

builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(x =>
        {
            x.SaveToken = true;
            x.TokenValidationParameters = new TokenValidationParameters()
            {
                IssuerSigningKey =
                    new SymmetricSecurityKey(
                        Encoding.UTF8.GetBytes(builder.Configuration.GetSection("JWTSecret:Key").Value)),
                ValidateIssuer = false,
                ValidateAudience = false,
                ValidateLifetime = true,
                ClockSkew = TimeSpan.Zero
            };
            x.Events = new JwtBearerEvents()
            {
                OnTokenValidated = async context =>
                {
                    var claimsPrincipal = context.Principal;
                    var email = claimsPrincipal.FindFirstValue(ClaimTypes.Email);

                    var claimIdentity = context.Principal.Identity as ClaimsIdentity;
                    claimIdentity.AddClaim(new Claim(ClaimTypes.Email, email));
                }
            };
        }
    );
        

builder.Services.AddAuthorization(opts =>
{
    opts.AddPolicy("", t => t.RequireAuthenticatedUser());
});

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
app.UseAuthorization();
// app.UseCors(cors => cors
        // .AllowAnyOrigin()
        // .AllowAnyMethod()
        // .AllowAnyHeader());

// app.UseHttpsRedirection();

app.Run();