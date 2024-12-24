package vibrato.vibrato.services;

import vibrato.vibrato.entidades.Artista;
import vibrato.vibrato.entidades.EchoSystem;
import vibrato.vibrato.entidades.Ouvinte;
import vibrato.vibrato.entidades.Usuario;
import vibrato.vibrato.security.Token;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public interface UsuarioService {
    Artista criarArtista(Artista artista);
    List<Usuario> listarUsuario();
    Ouvinte criarOuvinte(Ouvinte ouvinte);
    Usuario editarUsuario(Usuario usuario);
    Boolean excluirUsuario(Integer id);
    Token gerarToken(Usuario usuario);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> buscarUsuarioPorId(Integer id);
    Optional<Usuario> buscarUsuarioPorUser(String username);
    Optional<Usuario> buscarId(Integer id);
    Usuario atualizarEmail(Usuario u, String email);
    Usuario atualizarSenha(Usuario u, String senha);
    void deletarConta(Integer id);
    void uploadToLocal(String directoryPath, String fileName, byte[] data) throws IOException;
    String generateUniqueArchiveName(String directory, String originalFilename);

}
