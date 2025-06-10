namespace backend.service;

public class UtilService
{
    private readonly string _frontHost;
    
    public UtilService(string frontHost)
    {
        _frontHost = frontHost;
    }
    
    public string formatInvoiceLink(string invoiceLink) => _frontHost+invoiceLink;
}