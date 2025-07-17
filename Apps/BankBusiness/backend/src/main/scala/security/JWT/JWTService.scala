package security.JWT

import configuration.JWTConfig
import io.jsonwebtoken.{Claims, JwtException, Jwts, SignatureAlgorithm}
import zio.{IO, RLayer, Task, ZIO, ZLayer}

import java.time.Duration
import java.util.Date

final case class JWTService(private val secrets: JWTConfig) {

  private val accessSecret = secrets.accessSecret
  private val refreshSecret = secrets.refreshSecret

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

  def getEmail(token: String, tokenType: TokenType): Task[Option[String]] = extractClaims(token, tokenType).map(p => Option(p.getSubject))
  def getExpiration(token: String, tokenType: TokenType): Task[Date] = extractClaims(token, tokenType).map(_.getExpiration)
  def getIssuedAt(token: String, tokenType: TokenType): Task[Date] = extractClaims(token, tokenType).map(_.getIssuedAt)

}

object JWTService {
  val layer: RLayer[JWTConfig, JWTService] = ZLayer.fromFunction(JWTService.apply _)
}