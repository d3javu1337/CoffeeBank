package mongodb

import (
	"context"
	"errors"
	"log/slog"
	"time"

	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
)

type SessionsRepository interface {
	RegisterSession(ctx context.Context, email string, session *SessionDto) (bool, error)
	CloseSessionByAccessToken(ctx context.Context, email string, accessToken string) (bool, error)
	CloseSessionById(ctx context.Context, email string, sessionId string) (bool, error)
	CloseAllSessionsExclude(ctx context.Context, email string, accessToken string) (bool, error)
	UpdateSessionOnTokenRefresh(ctx context.Context, email string, oldRefreshToken string, newAccessToken string, newRefreshToken string) (bool, error)
	UpdateSessionOnInteraction(ctx context.Context, email string, accessToken string) (bool, error)
	FindAllSessions(ctx context.Context, email string) (*SessionsDto, error)
	FindSession(ctx context.Context, email string, sessionId string) (*SessionDto, error)
	ExistSessionByAccessToken(ctx context.Context, email string, accessToken string) (bool, error)
}

type SessionsRepositoryImpl struct {
	collection *mongo.Collection
}

func NewSessionsRepositoryImpl(coll *mongo.Collection) *SessionsRepositoryImpl {
	return &SessionsRepositoryImpl{collection: coll}
}

func (s *SessionsRepositoryImpl) RegisterSession(ctx context.Context, email string, session *SessionDto) (bool, error) {
	res, err := s.collection.UpdateOne(ctx, bson.M{
		"_id": email,
	}, bson.M{
		"$push": bson.M{
			"sessions": session,
		},
	},
		options.UpdateOne().SetUpsert(true),
	)
	if err != nil {
		slog.Error("Session registration error", "error", err)
		panic("ЯЕБЛАН")
		return false, err
	}
	return res.Acknowledged, nil
}

func (s *SessionsRepositoryImpl) CloseSessionByAccessToken(ctx context.Context, email string, accessToken string) (bool, error) {
	res, err := s.collection.UpdateByID(ctx, email, bson.M{
		"$pull": bson.M{
			"sessions": bson.M{
				"accessToken": accessToken,
			},
		},
	})
	if err != nil {
		slog.Error("Session close by access token error", "accessToken", accessToken, "error", err)
		panic("ЯЕБЛАН")
		return false, err
	}
	return res.Acknowledged, nil
}

func (s *SessionsRepositoryImpl) CloseSessionById(ctx context.Context, email string, sessionId string) (bool, error) {
	res, err := s.collection.UpdateByID(ctx, email, bson.M{
		"$pull": bson.M{
			"sessions": bson.M{"_id": sessionId},
		},
	})
	if err != nil {
		slog.Error("Session close by id error", "error", err, "id", sessionId)
		panic("ЯЕБЛАН")
		return false, err
	}
	return res.Acknowledged, nil
}

func (s *SessionsRepositoryImpl) CloseAllSessionsExclude(ctx context.Context, email string, accessToken string) (bool, error) {
	res, err := s.collection.UpdateByID(ctx, email, bson.M{
		"$pull": bson.M{
			"sessions": bson.M{
				"accessToken": bson.M{
					"$ne": accessToken,
				},
			},
		},
	})
	if err != nil {
		slog.Error("Closing all sessions error exclude", "error", err, "accessToken", accessToken)
		panic("ЯЕБЛАН")
		return false, err
	}
	return res.Acknowledged, nil
}

func (s *SessionsRepositoryImpl) UpdateSessionOnTokenRefresh(ctx context.Context, email string, oldRefreshToken string, newAccessToken string, newRefreshToken string) (bool, error) {
	res, err := s.collection.UpdateOne(ctx, bson.M{
		"_id": email,
		"sessions": bson.M{
			"refreshToken": oldRefreshToken,
		},
	}, bson.M{
		"$set": bson.M{
			"sessions.$.accessToken":         newAccessToken,
			"sessions.$.refreshToken":        newRefreshToken,
			"sessions.$.lastInteractionTime": time.Now(),
		},
	})
	if err != nil {
		slog.Error("Session update on token refresh error", "error", err)
		panic("ЯЕБЛАН")
		return false, err
	}
	return res.Acknowledged, nil
}

func (s *SessionsRepositoryImpl) UpdateSessionOnInteraction(ctx context.Context, email string, accessToken string) (bool, error) {
	res, err := s.collection.UpdateOne(ctx, bson.M{
		"_id":                  email,
		"sessions.accessToken": accessToken,
	}, bson.M{
		"$set": bson.M{
			"sessions.$.lastInteractionTime": time.Now(),
		},
	})
	if err != nil {
		slog.Error("Session update on interaction failed", "error", err)
		panic("ЯЕБЛАН")
		return false, nil
	}
	return res.Acknowledged, nil
}

func (s *SessionsRepositoryImpl) FindAllSessions(ctx context.Context, email string) (*SessionsDto, error) {
	res := s.collection.FindOne(ctx, bson.E{Key: "_id", Value: email})
	var sess *SessionsDto
	switch err := res.Decode(sess); {
	case errors.Is(err, bson.ErrNilRegistry):
		return nil, nil
	case err == nil:
		return sess, nil
	default:
		{
			slog.Error("Mongo decode error: ", err)
			return nil, err
		}
	}
}

func (s *SessionsRepositoryImpl) FindSession(ctx context.Context, email string, sessionId string) (*SessionDto, error) {
	res := s.collection.FindOne(ctx, bson.M{
		"_id":          email,
		"sessions._id": sessionId,
	})
	var sess *SessionDto
	err := res.Decode(sess)
	if err != nil {
		slog.Error("Mongo decode error", "error", err)
		return nil, err
	}
	return sess, nil
}
