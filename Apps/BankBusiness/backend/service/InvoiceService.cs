using backend.dto.invoice;
using Grpc.Net.Client;
using Org.D3Javu.Backend.Grpc;

namespace backend.service;

public class InvoiceService
{
    private readonly GrpcChannel _channel = GrpcChannel.ForAddress("https://localhost:9090");
    private readonly Org.D3Javu.Backend.Grpc.InvoiceService.InvoiceServiceClient _grpcClient;
    private readonly PaymentAccountService _paymentAccountService;
    private readonly UtilService _utilService;
    
    public InvoiceService(PaymentAccountService paymentAccountService, 
        Org.D3Javu.Backend.Grpc.InvoiceService.InvoiceServiceClient grpcClient, UtilService utilService)
    {
        _grpcClient = grpcClient;
        _paymentAccountService = paymentAccountService;
        _utilService = utilService;
    }
    public string GenerateApiToken(string email)
    {
        var paymentAccountId = _paymentAccountService.Find(email).Id;
        return _grpcClient.InvoiceIssuingTokenCreate(
            new InvoiceIssuingTokenCreateRequest
            {
                PaymentAccountId = paymentAccountId
            }).Token;
    }

    public string InvoiceIssue(string email, InvoiceIssueDto dto)
    {
        var clientId = _paymentAccountService.Find(email).Id;
        return _utilService.formatInvoiceLink(_grpcClient.InvoiceIssue(
            new InvoiceIssueRequest
            {
                Amount = dto.amount,
                ProviderPaymentAccountId = clientId,
            }
        ).PaymentLink);
    }

    public string GetInvoiceCreateToken(string email) => _paymentAccountService.Find(email).InvoiceCreateToken.ToString();

}