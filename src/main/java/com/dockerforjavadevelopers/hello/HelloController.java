package com.dockerforjavadevelopers.hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

  private StringBuffer resposta;

  @RequestMapping("/")
  public String index() {
    try {
      // Cria uma URL de teste
      URL url = new URL("https://test.k6.io/contacts.php");

      // Cria uma conexão HTTP
      HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

      // Define o método HTTP da requisição
      conexao.setRequestMethod("GET");

      // Define o cabeçalho da requisição
      conexao.setRequestProperty("Accept", "application/json");

      // Conecta à URL e envia a requisição
      conexao.connect();

      // Lê a resposta do servidor
      BufferedReader leitor = new BufferedReader(
        new InputStreamReader(conexao.getInputStream())
      );

      String linha;

      resposta = new StringBuffer();

      while ((linha = leitor.readLine()) != null) {
        resposta.append(linha);
      }

      leitor.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return resposta.toString();
  }

  @GetMapping("/async")
  public Mono<String> asyncFunction() {
    WebClient webClient = WebClient.create("https://test.k6.io/contacts.php");

    return webClient.get().retrieve().bodyToMono(String.class);
  }

  @RequestMapping("/hello")
  public String hello() {
    return "hello world";
  }
}
