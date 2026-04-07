package config

import (
	"context"
	"log"
	"log/slog"
	"os"
	"sync"
	"time"

	"github.com/go-playground/validator/v10"
	"github.com/spf13/viper"
	"golang.org/x/crypto/sha3"
)

type AppConfigWrapper struct {
	appConfig *AppConfig
	mu        sync.RWMutex
}

func (wrapper *AppConfigWrapper) GetAppConfig() *AppConfig {
	wrapper.mu.RLock()
	defer wrapper.mu.RUnlock()
	return wrapper.appConfig
}

type AppConfig struct {
	WebServerConfig *WebServerConfig `mapstructure:"webserver" validate:"required"`
	PostgresConfig  *PostgresConfig  `mapstructure:"postgres" validate:"required"`
	MongoConfig     *MongoConfig     `mapstructure:"mongodb" validate:"required"`
	JWTConfig       *JWTConfig       `mapstructure:"jwt" validate:"required"`
	KafkaConfig     *KafkaConfig     `mapstructure:"kafka" validate:"required"`
}

type WebServerConfig struct {
	Port int `mapstructure:"port" validate:"required"`
}

type PostgresConfig struct {
	Username string `mapstructure:"username" validate:"required"`
	Password string `mapstructure:"password" validate:"required"`
	Host     string `mapstructure:"host" validate:"required"`
	Port     int    `mapstructure:"port" validate:"required"`
	Database string `mapstructure:"database" validate:"required"`
}

type MongoConfig struct {
	Username   string `mapstructure:"username" validate:"required"`
	Password   string `mapstructure:"password" validate:"required"`
	Host       string `mapstructure:"host" validate:"required"`
	Port       int    `mapstructure:"port" validate:"required"`
	AuthDB     string `mapstructure:"auth-database" validate:"required"`
	Database   string `mapstructure:"database" validate:"required"`
	Collection string `mapstructure:"collection" validate:"required"`
}

type JWTConfig struct {
	AccessSecret  string `mapstructure:"access-secret" validate:"required"`
	RefreshSecret string `mapstructure:"refresh-secret" validate:"required"`
}

type KafkaConfig struct {
	BootstrapServers string             `mapstructure:"bootstrap-servers" validate:"required"`
	Topics           *KafkaTopicsConfig `mapstructure:"topics" validate:"required"`
	ProducerGroup    string             `mapstructure:"producer-group" validate:"required"`
	ConsumerGroup    string             `mapstructure:"consumer-group" validate:"required"`
}

type KafkaTopicsConfig struct {
	BaseRegistrationRequestTopic      string `mapstructure:"base-registration-request-topic" validate:"required"`
	BaseRegistrationResponseTopic     string `mapstructure:"base-registration-response-topic" validate:"required"`
	BusinessRegistrationRequestTopic  string `mapstructure:"business-registration-request-topic" validate:"required"`
	BusinessRegistrationResponseTopic string `mapstructure:"business-registration-response-topic" validate:"required"`
}

var validate = validator.New()
var hash = sha3.New256()

const configName = "config"
const configType = "yaml"

func LoadConfig(ctx context.Context) (*AppConfigWrapper, error) {
	var conf AppConfig
	config := AppConfigWrapper{appConfig: &conf}
	config.mu.Lock()
	defer config.mu.Unlock()
	return loadConfig(ctx, &config)
}

func loadConfig(ctx context.Context, configInstance *AppConfigWrapper) (*AppConfigWrapper, error) {
	log.Println("started config load")
	viper.SetConfigName(configName)
	viper.SetConfigType(configType)
	viper.AddConfigPath(".")

	err := viper.ReadInConfig()

	if err != nil {
		log.Panicln("bad config", err)
	}
	if err := viper.Unmarshal(configInstance.appConfig); err != nil {
		return nil, err
	}

	if err := validate.Struct(configInstance.appConfig); err != nil {
		log.Panicln("config parse error: ", err)
		return nil, err
	}

	if hashedConfig, err := hashConfigFile(configName + "." + configType); err != nil {
		slog.Error("Failed to count hash of config. App will work in static config mode")
		return configInstance, nil
	} else {
		function := func() (*string, error) {
			return hashConfigFile(configName + "." + configType)
		}
		go callConfigReload(ctx, configInstance, hashedConfig, function)
		return configInstance, nil
	}
}

func hashConfigFile(name string) (*string, error) {
	configFile, err := os.ReadFile(name)
	if err != nil {
		slog.Warn("Config file not found")
		return nil, err
	}
	hashed := string(hash.Sum(configFile))
	hash.Reset()
	return &hashed, nil

}

func callConfigReload(ctx context.Context, config *AppConfigWrapper, hash *string, hashFunc func() (*string, error)) {
	ticker := time.NewTicker(5 * time.Second)
	defer ticker.Stop()
	for {
		select {
		case <-ctx.Done():
			slog.Error("Config reloading context done")
			return
		case <-ticker.C:
			{
				reloadConfig(ctx, config, hash, hashFunc)
			}
		}
	}
}

func reloadConfig(ctx context.Context, config *AppConfigWrapper, hash *string, hashFunc func() (*string, error)) {
	if newHash, err := hashFunc(); newHash != nil && *newHash != *hash {
		slog.Info("Reloading config")

		config.mu.Lock()
		defer config.mu.Unlock()

		if cfg, err := loadConfig(ctx, config); err != nil {
			slog.Warn("Error while rereading config: ", err)
		} else {
			*config.appConfig = *(cfg.appConfig)
			*hash = *newHash
		}
	} else if err != nil {
		slog.Warn("err: ", err)
	}
}
