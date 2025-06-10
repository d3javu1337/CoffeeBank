using System.Security.Claims;
using System.Text;
using backend;
using backend.http;
using backend.kafka;
using backend.repository;
using backend.service;
using Grpc.Core;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using InvoiceService = Org.D3Javu.Backend.Grpc.InvoiceService;

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
builder.Services.AddTransient<backend.service.InvoiceService>();
builder.Services.AddTransient<PaymentService>();
builder.Services.AddTransient<PaymentAccountService>();
builder.Services.AddTransient<SecurityService>();
builder.Services.AddTransient<backend.service.InvoiceService>();
builder.Services.AddSingleton<UtilService>(_ => new UtilService(
    builder.Configuration.GetSection("Front:Host").Value
));
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


builder.Services.AddGrpc();
// wrong client
builder.Services.AddGrpcClient<Org.D3Javu.Backend.Grpc.InvoiceService.InvoiceServiceClient>(opt =>
        {
            opt.Address = new Uri(builder.Configuration.GetSection("Grpc:ServerHost").Value);
        }
    ).ConfigureChannel(c => c.Credentials = ChannelCredentials.Insecure)
    ;

builder.Services.AddAuthorization(opts => { opts.AddPolicy("", t => t.RequireAuthenticatedUser()); });

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