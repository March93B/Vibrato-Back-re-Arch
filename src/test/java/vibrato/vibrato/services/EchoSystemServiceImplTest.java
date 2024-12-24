package vibrato.vibrato.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vibrato.vibrato.entidades.EchoSystem;
import vibrato.vibrato.repositories.EchoSystemRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class EchoSystemServiceImplTest {

    @Mock
    EchoSystemRepository echoSystemRepository;

    @InjectMocks
    EchoSystemServiceImpl echoSystemServiceImpl;

    @Test
    void testeFindAllEchoSystemGeneroRock() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroRock()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroRock();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void testeFindAllEchoSystemGeneroPop() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroPop()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroPop();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void testeFindAllEchoSystemGeneroFutureCore() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroFutureCore()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroFutureCore();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void testeFindAllEchoSystemGeneroKpop() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroKpop()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroKpop();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void testeFindAllEchoSystemGeneroJrock() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroJrock()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroJrock();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void testeFindAllEchoSystemGeneroHipHopRap() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroHipHopRap()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroHipHopRap();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroRbSoul() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroRbSoul()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroRbSoul();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroIndie() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroIndie()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroIndie();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroEdm() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroEdm()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroEdm();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroEdmKawaii() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroEdmKawaii()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroEdmKawaii();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroJazz() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroJazz()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroJazz();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroSertanejo() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroSertanejo()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroSertanejo();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroEletroSwing() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroEletroSwing()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroEletroSwing();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }

    @Test
    void findAllEchoSystemGeneroPagode() {
        List<EchoSystem> musicas = new ArrayList<>();

        Mockito.when(echoSystemRepository.buscarPorGeneroPagode()).thenReturn(musicas);

        List<EchoSystem> resposta = echoSystemServiceImpl.findAllEchoSystemGeneroPagode();

        assertTrue(resposta.isEmpty());
        assertTrue(resposta.size() == 0);
    }
}