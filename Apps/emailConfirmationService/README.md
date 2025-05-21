## Some logic of:
1. consuming kafka message
2. when some message were consumed => ask MailService to do logic of confirmation 
3. mail service asks db service to create entity like (userEmail, hash <=> token) and save
4. create email with link to our endpoint with hash query param 
5. wait for endpoint call 
6. find in db email by hash
7. send this email back to reply topic