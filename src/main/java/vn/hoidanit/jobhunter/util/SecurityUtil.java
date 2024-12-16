package vn.hoidanit.jobhunter.util;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

@Service
public class SecurityUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${cheesethank.jwt.base64-secret}")
    public String jwtKey;

    @Value("${cheesethank.jwt.token-validity-in-seconds}")
    private String jwtExpiration;

    public void createToken(Authentication authentication) {

    }
}
