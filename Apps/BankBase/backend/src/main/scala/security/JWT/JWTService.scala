package security.JWT

import configuration.JWTConfig
import io.jsonwebtoken.{Claims, Jwts, SignatureAlgorithm}
import zio.{RLayer, Task, ZIO, ZLayer}

import java.time.Duration
import java.util.Date

case class JWTService(private val config: JWTConfig) {
  private val accessSecret = config.accessSecret
  private val refreshSecret = config.refreshSecret

  def generateAccessToken(email: String): Task[String] = {
    val issuedDate = new Date()
    val expiredDate = new Date(issuedDate.getTime + Duration.ofMinutes(15).toMillis)
    ZIO.from(Jwts.builder()
      .subject(email)
      .issuedAt(issuedDate)
      .expiration(expiredDate)
      .signWith(SignatureAlgorithm.HS256, accessSecret)
      .compact())
  }

  def generateRefreshToken(email: String): Task[String] = {
    val issuedDate = Date()
    val expiredDate = Date(issuedDate.getTime + Duration.ofDays(30).toMillis)
    ZIO.from(Jwts.builder()
      .subject(email)
      .issuedAt(issuedDate)
      .expiration(expiredDate)
      .signWith(SignatureAlgorithm.HS256, refreshSecret)
      .compact())
  }

  def generateTokens(email: String): Task[Tokens] = for {
    access <- generateAccessToken(email)
    refresh <- generateRefreshToken(email)
  } yield Tokens(access, refresh)

  private def extractClaims(token: String, tokenType: TokenType): Task[Claims] = tokenType match
    case TokenType.ACCESS => ZIO.from(Jwts.parser()
      .setSigningKey(accessSecret).build()
      .parseSignedClaims(token)
      .getPayload)
    case TokenType.REFRESH => ZIO.from(Jwts.parser()
      .setSigningKey(refreshSecret).build()
      .parseSignedClaims(token)
      .getPayload
    )

  def getEmail(token: String, tokenType: TokenType): Task[String] = extractClaims(token, tokenType).map(_.getSubject)

  def getExpiration(token: String, tokenType: TokenType): Task[Date] = extractClaims(token, tokenType).map(_.getExpiration)

  def getIssuedAt(token: String, tokenType: TokenType): Task[Date] = extractClaims(token, tokenType).map(_.getIssuedAt)
  
}

object JWTService {
  val layer: RLayer[JWTConfig, JWTService] = ZLayer.fromFunction(JWTService.apply _)
}