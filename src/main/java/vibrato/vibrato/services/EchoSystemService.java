package vibrato.vibrato.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vibrato.vibrato.dto.DtoExplore;
import vibrato.vibrato.entidades.EchoSystem;

import java.io.IOException;
import java.util.*;

public interface EchoSystemService {
    void atualizarFila(List<EchoSystem> echoSystems);
    void atualizarPilha(List<EchoSystem> musicas);
    EchoSystem addMusica(EchoSystem novaMusica);
    List<EchoSystem> echoSystemList();
    List<EchoSystem> echoSystemListDisc();
    List<EchoSystem> listarTop5GenericoPerfil();
    List<EchoSystem> buscarPorTexto(String termo);
    Optional<EchoSystem> buscarId(Integer id);
    Optional<EchoSystem> buscarPorId(Integer id);
    List<DtoExplore> listarTudo();
    List<EchoSystem> getTop3EchoSystemByArtistaId(Integer userId);
    List<EchoSystem> getTop3EchoSystemByArtistaUsername(String username);
    EchoSystem editarEchoSystem(EchoSystem echoSystem);
    List<EchoSystem> visualizacaoDesc(Integer userId, int page, int size);
    void deletarEchoSystem(Integer id);
    List<EchoSystem> visualizacaoDescUsername(String username, int page, int size);
    List<EchoSystem> streamDesc(Integer userId, int page, int size);
    List<EchoSystem> curtidasDesc(Integer userId, int page, int size);
    List<EchoSystem> redirecionamentoDesc(Integer userId, int page, int size);
    List<EchoSystem> findAllEchoSystemByArtistaId(Integer userId);
    List<EchoSystem> findAllEchoSystemGeneroRock();
    List<EchoSystem> findAllEchoSystemGeneroPop();
    List<EchoSystem> findAllEchoSystemGeneroFutureCore();
    List<EchoSystem> findAllEchoSystemGeneroKpop();
    List<EchoSystem> findAllEchoSystemGeneroJrock();
    List<EchoSystem> findAllEchoSystemGeneroHipHopRap();
    List<EchoSystem> findAllEchoSystemGeneroRbSoul();
    List<EchoSystem> findAllEchoSystemGeneroIndie();
    List<EchoSystem> findAllEchoSystemGeneroEdm();
    List<EchoSystem> findAllEchoSystemGeneroEdmKawaii();
    List<EchoSystem> findAllEchoSystemGeneroJazz();
    List<EchoSystem> findAllEchoSystemGeneroSertanejo();
    List<EchoSystem> findAllEchoSystemGeneroEletroSwing();
    List<EchoSystem> findAllEchoSystemGeneroPagode();
    void uploadToLocal(String directoryPath, String fileName, byte[] data) throws IOException;
    String generateUniqueArchiveName(String directory, String originalFilename);
    byte[] exportarCSV(List<EchoSystem> echoSystems);
    Stack<EchoSystem> pilha = new Stack<>();
    Queue<EchoSystem> fila = new LinkedList<>();
    ResponseEntity<Resource> getFileAsResource(String directoryPath, String fileName) throws IOException;
    ResponseEntity<EchoSystem> createEcho(MultipartFile imagem, String  novoEchoJson);
    ResponseEntity<byte[]> getByArtistaCsv(Integer userId) throws IOException;
    ResponseEntity<ByteArrayResource> gerarTxt(Integer userId);
}
