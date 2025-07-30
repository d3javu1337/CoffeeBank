package org.d3javu.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.model.base.card.CardType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;


@Slf4j
@RequiredArgsConstructor
@Service
public class CoreSecurityUtilService {

    private final String cardNumberBase = "999999";

    public String generateCardNumber(Long id, CardType type) {
        var sb = new StringBuilder(this.cardNumberBase); // 6
        var t = switch (type) {
            case CardType.CREDIT -> "1";
            case CardType.DEBIT -> "2";
            case CardType.OVERDRAFT -> "3";
            case CardType.PREPAID -> "4";
            default -> {
                log.warn("Unrecognised cardType: {}", type);
                yield null;
            }
        };
        sb.append(t);
        sb.append(String.format("%7s",id).replace(' ', '0'));
        sb.append(this.luhnSignature(sb.toString()));
        return sb.toString();
    }

    private char luhnSignature(String number) {
        var acc = 0;
        for (int i = number.length()-1; i >= 0; i--) {
            if (i%2 == 0) {
                var num =  (number.charAt(i) - '0')*2;
                if(num > 9){
                    acc ++;
                    acc += num%10;
                }else{
                    acc += num;
                }
            }else{
                acc += number.charAt(i) - '0';
            }
        }
        return (char) ('0'+((10 - acc%10)%10));
    }

    public String generateSecurityCode(){
        var t = String.valueOf(new Random(Instant.now().toEpochMilli()).nextLong(1000));
        return switch (t.length()){
            case 1 -> "00"+t;
            case 2 -> "0"+t;
            case 3 -> t;
            default -> {
                log.error("Length of generated security code is bigger than 3");
                yield null;
            }
        };
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCrypt
//    }

}
