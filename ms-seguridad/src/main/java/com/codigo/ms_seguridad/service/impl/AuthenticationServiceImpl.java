package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.constants.Constants;
import com.codigo.ms_seguridad.aggregates.request.SignInRequest;
import com.codigo.ms_seguridad.aggregates.request.SignUpRequest;
import com.codigo.ms_seguridad.aggregates.response.SignInResponse;
import com.codigo.ms_seguridad.entity.Rol;
import com.codigo.ms_seguridad.entity.Role;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.repository.RolRepository;
import com.codigo.ms_seguridad.repository.UsuarioRepository;
import com.codigo.ms_seguridad.service.AuthenticationService;
import com.codigo.ms_seguridad.service.JwtService;
import com.codigo.ms_seguridad.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    @Override
    public Usuario signUpUser(SignUpRequest signUpRequest) {
        //validar el token x segunda vez,
        //abrir el token para extrer los permisos
        // validar que cuente con el scope de crear

        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRoles(Collections.singleton(getRoles(Role.USER)));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario signUpAdmin(SignUpRequest signUpRequest) {
        Usuario usuario = getUsuarioEntity(signUpRequest);
        Set<Rol> roles = new HashSet<>();
        roles.add(getRoles(Role.USER));
        roles.add(getRoles(Role.ADMIN));
        usuario.setRoles(roles);
        //usuario.setRoles(Collections.singleton(getRoles(Role.ADMIN)));
        //usuario.setRoles(Collections.singleton(getRoles(Role.USER)));
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> todos() {
        return usuarioRepository.findAll();
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),signInRequest.getPassword()));

        var usuario = usuarioRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("ERROR NO SE ENCONTRO AL USUARIO"));

        var token = jwtService.generateToken(usuario,usuario);
        var refresh = jwtService.generateRefreshToken(new HashMap<>(),usuario);

        return SignInResponse.builder()
                .accessToken(token)
                .refreshToken(refresh)
                .build();
    }

    @Override
    public SignInResponse getTokenByRefreshToken(String refreshToken) throws IllegalAccessException {
        //Validar que es unn refreshtoken
        if(!jwtService.isRefreshToken(refreshToken)){
            throw new RuntimeException("Eror el token ingresado no es un Refresh");
        }
        //extraer el usuario
        String userEMail = jwtService.extractUserName(refreshToken);

        //buscamos al usuario en BD
        Usuario usuario = usuarioRepository.findByEmail(userEMail).orElseThrow(
                () -> new UsernameNotFoundException("Error el usuario del token no se encontro en la BD"));

        //Obteniendo UserDetail para la generación del access
        UserDetails userDetails = usuarioService.userDetailsService().loadUserByUsername(usuario.getUsername());


        //Validar que el refresh le pertenece a un usuario y validar la exp
        if(!jwtService.validateToken(refreshToken,usuario)){
            throw new IllegalAccessException("Error el Refreshtoken no le pertenece al usuario o esta vencido");
        }

        //generar el access
        String newAccess = jwtService.generateToken(userDetails,usuario);
        return SignInResponse.builder()
                .accessToken(newAccess)
                .refreshToken(refreshToken)
                .build();
    }


    //GENERAR UN ACCESS TOKEN A PARTIR DE UN REFRESHTOKEN


    private Usuario getUsuarioEntity(SignUpRequest signUpRequest){
        return Usuario.builder()
                .nombres(signUpRequest.getNombres())
                .apellidos(signUpRequest.getApellidos())
                .email(signUpRequest.getEmail())
                .password(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()))
                .tipoDoc(signUpRequest.getTipoDoc())
                .numDoc(signUpRequest.getNumDoc())
                .isAccountNonExpired(Constants.STATUS_ACTIVE)
                .isAccountNonLocked(Constants.STATUS_ACTIVE)
                .isCredentialsNonExpired(Constants.STATUS_ACTIVE)
                .isEnabled(Constants.STATUS_ACTIVE)
                .build();
    }

    private Rol getRoles(Role rolBuscado){
        return rolRepository.findByNombreRol(rolBuscado.name())
                .orElseThrow(() -> new RuntimeException("Error el rol no exixte: " + rolBuscado.name()));
    }
}
