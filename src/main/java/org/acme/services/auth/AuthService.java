package org.acme.services.auth;

import org.acme.dtos.auth.AuthResponseDTO;
import org.acme.dtos.auth.LoginRequestDTO;
import org.acme.dtos.auth.RegisterRequestDTO;

public interface AuthService {

    AuthResponseDTO login(LoginRequestDTO dto);

    AuthResponseDTO register(RegisterRequestDTO dto);
}
