package vibrato.vibrato.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vibrato.vibrato.dto.DtoExplore;
import vibrato.vibrato.entidades.EchoSystem;
import vibrato.vibrato.repositories.EchoSystemRepository;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EchoSystemServiceImpl implements EchoSystemService {

    private EchoSystemRepository echoSystemRepository;

    public EchoSystemServiceImpl(EchoSystemRepository echoSystemRepository){
        this.echoSystemRepository = echoSystemRepository;
    }
    public void atualizarFila(List<EchoSystem> echoSystems) {
        fila.clear();
        int tamanhoMaximo = 100;
        int tamanhoAtual = Math.min(echoSystems.size(), tamanhoMaximo);

        for (int i = 0; i < tamanhoAtual; i++) {
            fila.offer(echoSystems.get(i));
        }
    }
    public void atualizarPilha(List<EchoSystem> musicas) {
        pilha.clear();
        int tamanhoMaximo = 5;
        int tamanhoAtual = Math.min(musicas.size(), tamanhoMaximo);

        for (int i = 0; i < tamanhoAtual; i++) {
            pilha.push(musicas.get(i));
        }
    }

    public EchoSystem addMusica(EchoSystem novaMusica){
        EchoSystem musicBanco = echoSystemRepository.save(novaMusica);
        return musicBanco;
    }
    public List<EchoSystem> echoSystemList() {
        return echoSystemRepository.findAll();
    }
    public List<EchoSystem> echoSystemListDisc() {
        return echoSystemRepository.findAllByOrderByIdEchoDesc();
    }
    public List<EchoSystem> listarTop5GenericoPerfil(){
        List<EchoSystem> lista = echoSystemRepository.findTop5ByOrderByIdEchoDesc();
        return lista;
    }
    public List<EchoSystem> buscarPorTexto(String termo) {
        return echoSystemRepository.buscarPorTexto(termo);
    }

    public Optional<EchoSystem> buscarId(Integer id){
        Optional<EchoSystem> busca =  echoSystemRepository.findById(id);
        return busca;
        }
    public Optional<EchoSystem> buscarPorId(Integer id) {
        return echoSystemRepository.findById(id);
    }
    public List<DtoExplore> listarTudo(){
        List<EchoSystem> lista = echoSystemRepository.findAllByOrderByIdEchoDesc();
        List<DtoExplore> listaFiltrada = new ArrayList<>();
        for (EchoSystem e:
             lista) {
            DtoExplore d = new DtoExplore(e.getTituloMusica(),e.getCompositor(),e.getIdEcho(),e.getBlob());
            listaFiltrada.add(d);
        }
        return listaFiltrada;
    }

    public List<EchoSystem> getTop3EchoSystemByArtistaId(Integer userId) {
        return echoSystemRepository.findTop3EchoSystemByArtistaId(userId);
    }

    public List<EchoSystem> getTop3EchoSystemByArtistaUsername(String username) {
        return echoSystemRepository.findTop3EchoSystemByArtistaUsername(username);
    }

    public EchoSystem editarEchoSystem(EchoSystem echoSystem) {
        Optional<EchoSystem> echoSystemExistente = echoSystemRepository.findById(echoSystem.getIdEcho());

        if (echoSystemExistente.isEmpty()) {
            throw new IllegalArgumentException("EchoSystem não encontrado");
        }

        EchoSystem echoSystemAtualizado = echoSystemExistente.get();

        if (echoSystem.getVisualizacao() != null) {
            echoSystemAtualizado.setVisualizacao(echoSystem.getVisualizacao());
        }
        if (echoSystem.getStreams() != null){
            echoSystemAtualizado.setStreams(echoSystem.getStreams());
        }
        if (echoSystem.getCurtidas() != null){
            echoSystemAtualizado.setCurtidas(echoSystem.getCurtidas());
        }
        if (echoSystem.getRedirecionamento() != null){
            echoSystemAtualizado.setRedirecionamento(echoSystem.getRedirecionamento());
        }

        return echoSystemRepository.save(echoSystemAtualizado);
    }


    public List<EchoSystem> visualizacaoDesc(Integer userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return echoSystemRepository.findTop3TituloMusicaByOrderByVisualizacaoDesc(userId, pageRequest);
    }

    public void deletarEchoSystem(Integer id) {
        Optional<EchoSystem> echoSystem = echoSystemRepository.findById(id);
        if (echoSystem.isPresent()) {
            echoSystemRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("EchoSystem não encontrado");
        }
    }

    public List<EchoSystem> visualizacaoDescUsername(String username, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return echoSystemRepository.findTop3TituloMusicaUsernameByOrderByVisualizacaoDesc(username, pageRequest);
    }
    public List<EchoSystem> streamDesc(Integer userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return echoSystemRepository.findTop3TituloMusicaByOrderByStreamDesc(userId, pageRequest);
    }

    public List<EchoSystem> curtidasDesc(Integer userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return echoSystemRepository.findTop3TituloMusicaByOrderByCurtidasDesc(userId, pageRequest);
    }

    public List<EchoSystem> redirecionamentoDesc(Integer userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return echoSystemRepository.findTop3TituloMusicaByOrderByRedirecionamentoDesc(userId, pageRequest);
    }

    public List<EchoSystem> findAllEchoSystemByArtistaId(Integer userId) {
        return echoSystemRepository.findAllEchoSystemByArtistaId(userId);
    }

    public List<EchoSystem> findAllEchoSystemGeneroRock() {
        return echoSystemRepository.buscarPorGeneroRock();
    }

    public List<EchoSystem> findAllEchoSystemGeneroPop() {
        return echoSystemRepository.buscarPorGeneroPop();
    }

    public List<EchoSystem> findAllEchoSystemGeneroFutureCore() {
        return echoSystemRepository.buscarPorGeneroFutureCore();
    }

    public List<EchoSystem> findAllEchoSystemGeneroKpop() {
        return echoSystemRepository.buscarPorGeneroKpop();
    }

    public List<EchoSystem> findAllEchoSystemGeneroJrock() {
        return echoSystemRepository.buscarPorGeneroJrock();
    }

    public List<EchoSystem> findAllEchoSystemGeneroHipHopRap() {
        return echoSystemRepository.buscarPorGeneroHipHopRap();
    }

    public List<EchoSystem> findAllEchoSystemGeneroRbSoul() {
        return echoSystemRepository.buscarPorGeneroRbSoul();
    }

    public List<EchoSystem> findAllEchoSystemGeneroIndie() {
        return echoSystemRepository.buscarPorGeneroIndie();
    }

    public List<EchoSystem> findAllEchoSystemGeneroEdm() {
        return echoSystemRepository.buscarPorGeneroEdm();
    }

    public List<EchoSystem> findAllEchoSystemGeneroEdmKawaii() {
        return echoSystemRepository.buscarPorGeneroEdmKawaii();
    }

    public List<EchoSystem> findAllEchoSystemGeneroJazz() {
        return echoSystemRepository.buscarPorGeneroJazz();
    }


    public List<EchoSystem> findAllEchoSystemGeneroSertanejo() {
        return echoSystemRepository.buscarPorGeneroSertanejo();
    }


    public List<EchoSystem> findAllEchoSystemGeneroEletroSwing() {
        return echoSystemRepository.buscarPorGeneroEletroSwing();
    }


    public List<EchoSystem> findAllEchoSystemGeneroPagode() {
        return echoSystemRepository.buscarPorGeneroPagode();
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

        System.out.println("Arquivo salvo com sucesso em: " + file.getAbsolutePath());
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


    public byte[] exportarCSV(List<EchoSystem> echoSystems) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("id;titulo;visu;share;redirec;plays\n");

        for (EchoSystem echoSystem : echoSystems) {
            csvBuilder.append(String.format("%d;%s;%d;%d;%d;%d\n", echoSystem.getIdEcho(), echoSystem.getTituloMusica(), echoSystem.getVisualizacao(), echoSystem.getCurtidas(), echoSystem.getRedirecionamento(), echoSystem.getStreams()));
        }

        return csvBuilder.toString().getBytes(Charset.forName("UTF-8"));
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

    public ResponseEntity<EchoSystem> createEcho(MultipartFile imagem, String  novoEchoJson){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EchoSystem novoEcho = objectMapper.readValue(novoEchoJson, EchoSystem.class);

            if (!imagem.isEmpty()) {
                try {
                    byte[] imagemBytes = imagem.getBytes();
                    String directory = "imagens";
                    String imageName = generateUniqueArchiveName(directory, imagem.getOriginalFilename());

                  uploadToLocal(directory,imageName , imagemBytes);

                    novoEcho.setBlob(imageName);

                    return ResponseEntity.status(201).body(addMusica(novoEcho));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return ResponseEntity.status(400).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(400).build();
    }
    public ResponseEntity<byte[]> getByArtistaCsv(Integer userId) throws IOException {
        List<EchoSystem> echoSystems =visualizacaoDesc(userId, 0, 100);

        if (!echoSystems.isEmpty()) {
            atualizarFila(echoSystems);
            byte[] csvBytes = exportarCSV(echoSystems);

            String directory = "arquivos";
            String archiveName = generateUniqueArchiveName(directory, "metricas"+userId+".csv");
            uploadToLocal(directory, archiveName, csvBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "metricas"+userId+".csv");
            headers.setContentLength(csvBytes.length);

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        }

        return ResponseEntity.noContent().build();
    }
    public ResponseEntity<ByteArrayResource> gerarTxt(Integer userId){
        List<EchoSystem> echoSystems = visualizacaoDesc(userId, 0, 100);

        if (!echoSystems.isEmpty()) {
            atualizarFila(echoSystems);
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

            String header = String.format("%-10s%-30s%-19s%-12s%-12s%-11s", "idEcho", "tituloMusica", "visualizacoes", "plays", "redirect", "share");
            bufferedWriter.write("00 Métricas" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "00 05");

            bufferedWriter.newLine();

            bufferedWriter.write(header);
            bufferedWriter.newLine();
            int contaRegDadosGravados = 0;

            for (EchoSystem echoSystem : EchoSystemService.fila) {
                contaRegDadosGravados++;
                String linha = String.format("%-8d%-30s%-19d%-12d%-12d%-11d",
                        echoSystem.getIdEcho(),
                        echoSystem.getTituloMusica(),
                        echoSystem.getVisualizacao(),
                        echoSystem.getStreams(),
                        echoSystem.getRedirecionamento(),
                        echoSystem.getCurtidas()

                );

                bufferedWriter.write("02" + linha);
                bufferedWriter.newLine();
            }

            String trailer = "03Total de músicas do artista ";
            bufferedWriter.write(trailer + contaRegDadosGravados);

            bufferedWriter.close();

            byte[] fileContent = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "arquivo.txt");

            String directory = "arquivos";
            String archiveName = generateUniqueArchiveName(directory,"arquivo"+userId+".txt");

            uploadToLocal(directory, archiveName, fileContent);



            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileContent.length)
                    .body(new ByteArrayResource(fileContent));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}






















