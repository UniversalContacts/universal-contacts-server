package piotrzin.uc.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issuedAt = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;

    public JwtAuthenticationResponse(String accessToken, int tokenExpirationTime) {
        this.accessToken = accessToken;
        expirationDate = new Date(issuedAt.getTime() + tokenExpirationTime);
    }
}
