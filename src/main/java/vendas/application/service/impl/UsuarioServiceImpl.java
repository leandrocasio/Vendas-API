package vendas.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vendas.application.domain.entity.Usuario;
import vendas.application.domain.repository.UsuarioRepository;
import vendas.application.exception.SenhaInvalidaException;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    @Lazy
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository repository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario) throws SenhaInvalidaException {
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());
        if (senhasBatem) {
            return user;
        }

        throw new SenhaInvalidaException();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     Usuario usuario = repository.findByLogin(username)
             .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));

     String[] roles = usuario.isAdmin() ?
             new String[]{"ADMIN", "USER"} : new String[]{"USER"};

     return User
             .builder()
             .username(usuario.getLogin())
             .password(usuario.getSenha())
             .roles(roles)
             .build();
    }
}
