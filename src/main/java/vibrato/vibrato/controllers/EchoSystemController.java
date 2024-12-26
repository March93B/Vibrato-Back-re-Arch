package vibrato.vibrato.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vibrato.vibrato.dto.DtoExplore;
import vibrato.vibrato.entidades.EchoSystem;
import vibrato.vibrato.services.EchoSystemService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/echo")
public class EchoSystemController {

    private final EchoSystemService echoSystemService;

    public EchoSystemController( EchoSystemService echoSystemServiceImpl) {
            this.echoSystemService = echoSystemServiceImpl;
    }
    @GetMapping("/buscar-por-texto/{termo}")
    public ResponseEntity<List<EchoSystem>> buscarPorTexto(@PathVariable String termo) {
        List<EchoSystem> echoSystems = echoSystemService.buscarPorTexto(termo);

        if (echoSystems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(echoSystems, HttpStatus.OK);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EchoSystem> criarEcho(
            @RequestParam("imagem" ) MultipartFile imagem,
            @RequestPart("novoEcho") String novoEchoJson
    ) {
        return echoSystemService.createEcho(imagem, novoEchoJson);
    }


    @PatchMapping("/visualizacao/{id}")
    public ResponseEntity<EchoSystem> atualizarVisualizacao(@PathVariable Integer id, @RequestBody EchoSystem echoSystem) {
        Optional<EchoSystem> echoSystemExistente = echoSystemService.buscarId(id);

        if (echoSystemExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EchoSystem echoSystemAtualizado = echoSystemExistente.get();

        if (echoSystem.getVisualizacao() != null) {
            echoSystemAtualizado.setVisualizacao(echoSystem.getVisualizacao());
        }

        try {
            EchoSystem echoSystemEditado = echoSystemService.editarEchoSystem(echoSystemAtualizado);
            return ResponseEntity.ok(echoSystemEditado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/streams/{id}")
    public ResponseEntity<EchoSystem> atualizarPlays(@PathVariable Integer id, @RequestBody EchoSystem echoSystem) {
        Optional<EchoSystem> echoSystemExistente = echoSystemService.buscarId(id);

        if (echoSystemExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EchoSystem echoSystemAtualizado = echoSystemExistente.get();

        if (echoSystem.getStreams() != null) {
            echoSystemAtualizado.setStreams(echoSystem.getStreams());
        }

        try {
            EchoSystem echoSystemEditado = echoSystemService.editarEchoSystem(echoSystemAtualizado);
            return ResponseEntity.ok(echoSystemEditado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/curtidas/{id}")
    public ResponseEntity<EchoSystem> curtidasPlays(@PathVariable Integer id, @RequestBody EchoSystem echoSystem) {
        Optional<EchoSystem> echoSystemExistente = echoSystemService.buscarId(id);

        if (echoSystemExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EchoSystem echoSystemAtualizado = echoSystemExistente.get();

        if (echoSystem.getCurtidas() != null) {
            echoSystemAtualizado.setCurtidas(echoSystem.getCurtidas());
        }

        try {
            EchoSystem echoSystemEditado = echoSystemService.editarEchoSystem(echoSystemAtualizado);
            return ResponseEntity.ok(echoSystemEditado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/redirecionamento/{id}")
    public ResponseEntity<EchoSystem> redirecionamentoPlays(@PathVariable Integer id, @RequestBody EchoSystem echoSystem) {
        Optional<EchoSystem> echoSystemExistente = echoSystemService.buscarId(id);

        if (echoSystemExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EchoSystem echoSystemAtualizado = echoSystemExistente.get();

        if (echoSystem.getRedirecionamento() != null) {
            echoSystemAtualizado.setRedirecionamento(echoSystem.getRedirecionamento());
        }

        try {
            EchoSystem echoSystemEditado = echoSystemService.editarEchoSystem(echoSystemAtualizado);
            return ResponseEntity.ok(echoSystemEditado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EchoSystem>> listaEchos() {
        return ResponseEntity.status(200).body(echoSystemService.echoSystemList());
    }

    @GetMapping("/desc-list")
    public ResponseEntity<List<EchoSystem>> listaEchoesDisc() {
        return ResponseEntity.status(200).body(echoSystemService.echoSystemListDisc());
    }

    @GetMapping("/top5")
    public ResponseEntity<List<EchoSystem>> listarTop5() {
        List<EchoSystem> musicas = echoSystemService.listarTop5GenericoPerfil();
        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        echoSystemService.atualizarPilha(musicas);
        return ResponseEntity.status(200).body(new ArrayList<>(EchoSystemService.pilha));
    }


    @GetMapping("/artista/{userId}")
    public ResponseEntity<List<EchoSystem>> getAllEchoSystemByArtistaId(@PathVariable Integer userId) {
        List<EchoSystem> echoSystems = echoSystemService.findAllEchoSystemByArtistaId(userId);
        return ResponseEntity.ok(echoSystems);
    }

    @GetMapping("/visu/{userId}")
    public ResponseEntity<List<EchoSystem>> getTop3EchoSystemByArtistaId(
            @PathVariable Integer userId
    ) {
        List<EchoSystem> echoSystems = echoSystemService.visualizacaoDesc(userId, 0, 3);
        return ResponseEntity.ok(echoSystems);
    }

    @GetMapping("/visu100/{userId}")
    public ResponseEntity<List<EchoSystem>> get100EchoSystemByArtistaId(@PathVariable Integer userId) {
        List<EchoSystem> echoSystems = echoSystemService.visualizacaoDesc(userId, 0, 100);
        if (!echoSystems.isEmpty()) {
            echoSystemService.atualizarFila(echoSystems);
        }
        return ResponseEntity.ok(new ArrayList<>(EchoSystemService.fila));

    }

    @GetMapping("/visuu/{username}")
    public ResponseEntity<List<EchoSystem>> getTop3EchoSystemByArtistausername(
            @PathVariable String username
    ) {
        List<EchoSystem> echoSystems = echoSystemService.visualizacaoDescUsername(username, 0, 3);
        return ResponseEntity.ok(echoSystems);
    }

    @GetMapping("/streams/{userId}")
    public ResponseEntity<List<EchoSystem>> getTop3MusicasByPlay(
            @PathVariable Integer userId
    ) {
        List<EchoSystem> echoSystems = echoSystemService.streamDesc(userId, 0, 3);
        return ResponseEntity.ok(echoSystems);
    }

    @GetMapping("/likes/{userId}")
    public ResponseEntity<List<EchoSystem>> getTop3MusicasByCurtida(
            @PathVariable Integer userId
    ) {
        List<EchoSystem> echoSystems = echoSystemService.curtidasDesc(userId, 0, 3);
        return ResponseEntity.ok(echoSystems);
    }

    @GetMapping("/redirec/{userId}")
    public ResponseEntity<List<EchoSystem>> getTop3MusicasByRedirecionamento(
            @PathVariable Integer userId
    ) {
        List<EchoSystem> echoSystems = echoSystemService.redirecionamentoDesc(userId, 0, 3);
        return ResponseEntity.ok(echoSystems);
    }


    @GetMapping("/echolink/{id}")
    public ResponseEntity<Optional<EchoSystem>> buscarPorId(@PathVariable Integer id) {
        Optional<EchoSystem> busca = echoSystemService.buscarId(id);
        if (busca.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(busca);
    }

    @GetMapping("/explore")
    public ResponseEntity<List<DtoExplore>> listarTodos() {
        List<DtoExplore> musicas = echoSystemService.listarTudo();

        if (musicas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(echoSystemService.listarTudo());
    }


    @GetMapping("/last3/{userId}")
    public ResponseEntity<List<EchoSystem>> getTop3EchoSystemsByArtistaId(@PathVariable Integer userId) {
        List<EchoSystem> echoSystems = echoSystemService.getTop3EchoSystemByArtistaId(userId);
        if (echoSystems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(echoSystems, HttpStatus.OK);
        }
    }

    @DeleteMapping("/deletar/echo/{id}")
    public ResponseEntity<Void> deleteEcho(@PathVariable Integer id) {
        Optional<EchoSystem> echoSystemExistente = echoSystemService.buscarId(id);

        if (echoSystemExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            echoSystemService.deletarEchoSystem(echoSystemExistente.get().getIdEcho());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/last33/{username}")
    public ResponseEntity<List<EchoSystem>> getTop3EchoSystemsByArtistaUsername(@PathVariable String username) {
        List<EchoSystem> echoSystems = echoSystemService.getTop3EchoSystemByArtistaUsername(username);
        if (echoSystems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(echoSystems, HttpStatus.OK);
        }
    }

    @GetMapping("/genero/rock")
    public ResponseEntity<List<EchoSystem>> listarGeneroRock() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroRock();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroRock());
    }

    @GetMapping("/genero/pop")
    public ResponseEntity<List<EchoSystem>> listarGeneroPop() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroPop();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroPop());
    }

    @GetMapping("/genero/future-core")
    public ResponseEntity<List<EchoSystem>> listarGeneroFuteCore() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroFutureCore();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroFutureCore());
    }

    @GetMapping("/genero/kpop")
    public ResponseEntity<List<EchoSystem>> listarGeneroKpop() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroKpop();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroKpop());
    }

    @GetMapping("/genero/hip-hop-rap")
    public ResponseEntity<List<EchoSystem>> listarGeneroHipHopRap() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroHipHopRap();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroHipHopRap());
    }

    @GetMapping("/genero/r&b-soul")
    public ResponseEntity<List<EchoSystem>> listarGeneroRbSoul() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroRbSoul();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroRbSoul());
    }

    @GetMapping("/genero/indie")
    public ResponseEntity<List<EchoSystem>> listarGeneroIndie() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroIndie();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroIndie());
    }

    @GetMapping("/genero/jrock")
    public ResponseEntity<List<EchoSystem>> listarGeneroJrock() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroJrock();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroJrock());
    }

    @GetMapping("/genero/edm")
    public ResponseEntity<List<EchoSystem>> listarGeneroEdm() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroEdm();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroEdm());
    }

    @GetMapping("/genero/edm-kawaii")
    public ResponseEntity<List<EchoSystem>> listarGeneroEdmKawaii() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroEdmKawaii();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroEdmKawaii());
    }

    @GetMapping("/genero/jazz")
    public ResponseEntity<List<EchoSystem>> listarGeneroJazz() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroJazz();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroJazz());
    }

    @GetMapping("/genero/sertanejo")
    public ResponseEntity<List<EchoSystem>> listarGeneroSertanejo() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroSertanejo();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroSertanejo());
    }

    @GetMapping("/genero/eletro-swing")
    public ResponseEntity<List<EchoSystem>> listarGeneroEletroSwing() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroEletroSwing();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroEletroSwing());
    }

    @GetMapping("/genero/pagode")
    public ResponseEntity<List<EchoSystem>> listarGeneroPagode() {
        List<EchoSystem> musicas = echoSystemService.findAllEchoSystemGeneroPagode();

        if (musicas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(echoSystemService.findAllEchoSystemGeneroPagode());
    }

    @GetMapping("/visu-csv/{userId}")
    public ResponseEntity<byte[]> get100EchoSystemByArtistaIdCsv(@PathVariable Integer userId) throws IOException {
    return echoSystemService.getByArtistaCsv(userId);
    }

    @GetMapping("/gerarArquivoTxt/{userId}")
    public ResponseEntity<ByteArrayResource> gerarArquivoTxt(@PathVariable Integer userId) {
        return echoSystemService.gerarTxt(userId);
    }
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            return echoSystemService.getFileAsResource("imagens", fileName);
        } catch (IOException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}

