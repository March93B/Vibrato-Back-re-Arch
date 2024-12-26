package vibrato.vibrato.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import vibrato.vibrato.entidades.Artista;
import vibrato.vibrato.entidades.Ouvinte;
import vibrato.vibrato.entidades.Usuario;
import vibrato.vibrato.repositories.UsuarioRepository;
import vibrato.vibrato.security.Token;
import vibrato.vibrato.security.TokenUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    public Artista criarArtista(Artista artista) {
        if (usuarioRepository.existsByUsername(artista.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esse username");
        }
        if (usuarioRepository.existsByEmail(artista.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esse email");
        }
        String encoder = this.passwordEncoder.encode(artista.getSenha());
        artista.setSenha(encoder);
        return usuarioRepository.save(artista);
    }

    public Ouvinte criarOuvinte(Ouvinte ouvinte) {
        if (usuarioRepository.existsByUsername(ouvinte.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esse username");
        }
        if (usuarioRepository.existsByEmail(ouvinte.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esse email");
        }
        String encoder = this.passwordEncoder.encode(ouvinte.getSenha());
        ouvinte.setSenha(encoder);
        return usuarioRepository.save(ouvinte);
    }

    public Usuario editarUsuario(Usuario usuario) {

        return usuarioRepository.save(usuario);
    }

    public Boolean excluirUsuario(Integer id) {
        usuarioRepository.deleteById(id);
        return true;
    }

    public Token gerarToken(Usuario usuario) {
        Optional<Usuario> user = usuarioRepository.findByEmail(usuario.getEmail());
        if (user.isPresent()) {
            Boolean valid = passwordEncoder.matches(usuario.getSenha(), user.get().getSenha());
            if (valid) {
                return new Token(TokenUtil.createToken(user.get()));
            }
        }
        return null;
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarUsuarioPorUser(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> buscarId(Integer id){
        Optional<Usuario> busca =  usuarioRepository.findById(id);
        return busca;
    }

    public Usuario atualizarEmail(Usuario u, String email){
        usuarioRepository.atualizarEmail(email, u.getIdUsuario());
        return buscarId(u.getIdUsuario()).get();
    }

    public Usuario atualizarSenha(Usuario u, String senha){
        String encoder = this.passwordEncoder.encode(senha);
        usuarioRepository.atualizarSenha(encoder, u.getIdUsuario());
        return buscarId(u.getIdUsuario()).get();
    }

    public void deletarConta(Integer id){
        usuarioRepository.deletarConta(id);
    }

    public void uploadToLocal(String directoryPath, String fileName, byte[] data) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directoryPath + File.separator + fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            fos.flush();
        }

    }

    public String generateUniqueArchiveName(String directory, String originalFilename) {
        int counter = 1;
        String archiveName = originalFilename;
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
            archiveName = originalFilename.substring(0, dotIndex);
        }

        String newBlobName = archiveName + extension;

        while (imageExists(directory, newBlobName)) {
            newBlobName = archiveName + " "+counter + extension;
            counter++;
        }

        return newBlobName;
    }
    public boolean imageExists(String directory, String fileName) {
        try {
            File file = new File(directory + File.separator + fileName);
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public ResponseEntity<Resource> getFileAsResource(String directoryPath, String fileName) throws IOException {
        Path path = Paths.get(directoryPath).resolve(fileName).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new FileNotFoundException("Arquivo não encontrado: " + fileName);
        }
    }
    public ResponseEntity<Usuario> atualizarImg(MultipartFile imagem,String novoUserJson){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Usuario usuario = objectMapper.readValue(novoUserJson, Usuario.class);

            Optional<Usuario> usuarioExistente = buscarUsuarioPorId(usuario.getIdUsuario());
            if (!usuarioExistente.isPresent()) {
                return ResponseEntity.status(404).build();
            }
            if (usuario.getUsername() != null && !usuario.getUsername().isEmpty()
                    && existsByUsername(usuario.getUsername())) {
                return ResponseEntity.status(409).build();
            }


            Usuario usuarioAtualizado = usuarioExistente.get();

            if (imagem != null && !imagem.isEmpty()) {
                byte[] imagemBytes = imagem.getBytes();
                String directory = "imagens";
                String archiveName = generateUniqueArchiveName(directory, imagem.getOriginalFilename());

                uploadToLocal(directory, archiveName,imagemBytes);

                usuarioAtualizado.setBlob(archiveName);
            }

            if (usuario.getNome() != null && !usuario.getNome().isEmpty()) {
                usuarioAtualizado.setNome(usuario.getNome());
            }

            if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                usuarioAtualizado.setEmail(usuario.getEmail());
            }

            if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                usuarioAtualizado.setNovaSenha(usuario.getSenha());
            } else {
                usuarioAtualizado.setNovaSenha(usuarioAtualizado.getSenha());
            }

            if (usuario.getUsername() != null && !usuario.getUsername().isEmpty()) {
                usuarioAtualizado.setUsername(usuario.getUsername());
            }

            if (usuario.getTwitter() != null && !usuario.getTwitter().isEmpty()) {
                usuarioAtualizado.setTwitter(usuario.getTwitter());
            }

            if (usuario.getInstagram() != null && !usuario.getInstagram().isEmpty()) {
                usuarioAtualizado.setInstagram(usuario.getInstagram());
            }

            if (usuario.getSpotify() != null && !usuario.getSpotify().isEmpty()) {
                usuarioAtualizado.setSpotify(usuario.getSpotify());
            }

            if (usuario.getSoundcloud() != null && !usuario.getSoundcloud().isEmpty()) {
                usuarioAtualizado.setSoundcloud(usuario.getSoundcloud());
            }

            if (usuario.getGenero() != null && !usuario.getGenero().isEmpty()) {
                usuarioAtualizado.setGenero(usuario.getGenero());
            }

            if (usuario.getBiografia() != null && !usuario.getBiografia().isEmpty()) {
                usuarioAtualizado.setBiografia(usuario.getBiografia());
            }
            if (usuario.getVisualizacao() != null) {
                usuarioAtualizado.setVisualizacao(usuario.getVisualizacao());
            }


            return ResponseEntity.status(201).body(editarUsuario(usuarioAtualizado));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(400).build();
    }
}
