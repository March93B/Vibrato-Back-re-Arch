package vibrato.vibrato.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import vibrato.vibrato.dto.DtoLogin;
import vibrato.vibrato.entidades.Artista;
import vibrato.vibrato.entidades.Ouvinte;
import vibrato.vibrato.entidades.Usuario;
import vibrato.vibrato.security.Token;
import vibrato.vibrato.security.TokenUtil;
import vibrato.vibrato.services.UsuarioService;
import vibrato.vibrato.services.UsuarioServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;

    }
    @GetMapping
    public ResponseEntity<List<Usuario>> listaUsuarios() {
        return ResponseEntity.status(200).body(usuarioService.listarUsuario());
    }

    @PostMapping("/artista")
    public ResponseEntity<Usuario> criarArtista( @RequestBody Artista artista) {
        return ResponseEntity.status(201).body(usuarioService.criarArtista(artista));
    }

    @PostMapping("/ouvinte")
    public ResponseEntity<Usuario> criarOuvinte( @RequestBody Ouvinte ouvinte) {
        return ResponseEntity.status(201).body(usuarioService.criarOuvinte(ouvinte));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Usuario> editarUsuario( @RequestBody Usuario usuario) {
        return ResponseEntity.status(200).body(usuarioService.editarUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Integer id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.status(204).build();
    }
    private TokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<DtoLogin> logar(@Valid @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(usuario.getEmail());
        if (usuarioOpt.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        Token token = usuarioService.gerarToken(usuario);
        if(token==null){
            return ResponseEntity.status(401).build();

        }

        DtoLogin logado = new DtoLogin(usuarioOpt.get().getIdUsuario(),usuarioOpt.get().getUsername(),token, usuarioOpt.get().getTipoUsuario());
        return ResponseEntity.status(202).body(logado);

    }


    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Usuario> atualizarUsuario(
            @RequestParam(value = "imagem", required = false) MultipartFile imagem,
            @RequestPart("usuario") String novoUserJson
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Usuario usuario = objectMapper.readValue(novoUserJson, Usuario.class);

            Optional<Usuario> usuarioExistente = usuarioService.buscarUsuarioPorId(usuario.getIdUsuario());
            if (!usuarioExistente.isPresent()) {
                return ResponseEntity.status(404).build();
            }
            if (usuario.getUsername() != null && !usuario.getUsername().isEmpty()
                    && usuarioService.existsByUsername(usuario.getUsername())) {
                return ResponseEntity.status(409).build();
            }


            Usuario usuarioAtualizado = usuarioExistente.get();

            if (imagem != null && !imagem.isEmpty()) {
                byte[] imagemBytes = imagem.getBytes();
                String directory = "imagens";
                String archiveName = usuarioService.generateUniqueArchiveName(directory, imagem.getOriginalFilename());

                usuarioService.uploadToLocal(directory, archiveName,imagemBytes);

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


            return ResponseEntity.status(201).body(usuarioService.editarUsuario(usuarioAtualizado));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(400).build();
    }


    @GetMapping("/perfil/{id}")
    public ResponseEntity<Optional<Usuario>> buscarPorId(@PathVariable Integer id) {
        Optional<Usuario> busca = usuarioService.buscarId(id);
        if (busca.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(busca);
    }

    @GetMapping("/perfill/{username}")
    public ResponseEntity<Optional<Usuario>> buscarPorUser(@PathVariable String username) {
        Optional<Usuario> busca = usuarioService.buscarUsuarioPorUser(username);
        if (busca.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(busca);
    }

    @PatchMapping("/atualizar/perfil/email/{id}")
    public ResponseEntity<Usuario> atualizarEmail(@PathVariable Integer id, @RequestBody Usuario u){
        if (usuarioService.existsByEmail(u.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esse email");
        }
        if (usuarioService.buscarId(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            usuarioService.atualizarEmail(usuarioService.buscarId(id).get(), u.getEmail());
        }
        return ResponseEntity.ok(usuarioService.buscarId(id).get());
    }

    @PatchMapping("/atualizar/perfil/senha/{id}")
    public ResponseEntity<Usuario> atualizarSenha(@PathVariable Integer id, @RequestBody Usuario u){
        if (usuarioService.buscarId(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }else {
            usuarioService.atualizarSenha(usuarioService.buscarId(id).get(), u.getSenha());
        }
        return ResponseEntity.ok(usuarioService.buscarId(id).get());
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Integer id){
        if (usuarioService.buscarId(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        usuarioService.deletarConta(id);
        return ResponseEntity.ok().build();
    }


}


















