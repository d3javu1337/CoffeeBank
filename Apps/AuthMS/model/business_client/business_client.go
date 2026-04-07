package business_client

type BusinessClient struct {
	id           string
	officialName string
	brand        string
	email        string
	passwordHash string
}

func NewBusinessClient(id string, officialName string, brand string, email string, passwordHash string) *BusinessClient {
	return &BusinessClient{
		id:           id,
		officialName: officialName,
		brand:        brand,
		email:        email,
		passwordHash: passwordHash,
	}
}

func (b *BusinessClient) Id() string           { return b.id }
func (b *BusinessClient) OfficialName() string { return b.officialName }
func (b *BusinessClient) Brand() string        { return b.brand }
func (b *BusinessClient) Email() string        { return b.email }
func (b *BusinessClient) PasswordHash() string { return b.passwordHash }
