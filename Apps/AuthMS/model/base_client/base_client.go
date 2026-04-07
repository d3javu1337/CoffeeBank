package base_client

type BaseClient struct {
	id           string
	email        string
	phoneNumber  string
	passwordHash string
	isEnabled    bool
}

func NewBaseClient(id string, email string, phoneNumber string, passwordHash string, isEnabled bool) *BaseClient {
	return &BaseClient{
		id:           id,
		email:        email,
		phoneNumber:  phoneNumber,
		passwordHash: passwordHash,
		isEnabled:    isEnabled,
	}
}

func (c *BaseClient) GetId() string           { return c.id }
func (c *BaseClient) GetEmail() string        { return c.email }
func (c *BaseClient) GetPhoneNumber() string  { return c.phoneNumber }
func (c *BaseClient) GetPasswordHash() string { return c.passwordHash }
func (c *BaseClient) GetIsEnabled() bool      { return c.isEnabled }
