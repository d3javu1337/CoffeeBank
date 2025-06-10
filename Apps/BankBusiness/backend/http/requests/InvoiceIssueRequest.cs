namespace backend.http.requests;

public record InvoiceIssueRequest(Guid token, double amount);