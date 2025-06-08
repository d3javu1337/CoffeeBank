namespace backend.http;

public static class InvoiceEndpoints
{
    /*
     * 1. generate token for invoice issuing
     * 2. invoice issuing
     */
    public static void MapInvoiceEndpoints(this WebApplication app)
    {
        app.MapGet("/api/token", () =>
        {

        });
        app.MapPost("/invoice", () =>
        {

        });
    }
}