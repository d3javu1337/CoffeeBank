service will work like:
1. kafka consumer which will listen requests to create or change and doing it
2. grpc server which will operate transactions
3. kafka producer for util kafka cluster (mail sender, etc.)
4. will have permissions to read and write db